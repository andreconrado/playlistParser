package com.playlist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.ValueEventListener;
import com.playlist.domain.Episode;
import com.playlist.domain.Logs;
import com.playlist.domain.M3UChannel;
import com.playlist.domain.M3USerie;
import com.playlist.domain.Preferences;
import com.playlist.domain.PreferencesChannel;
import com.playlist.domain.TvShow;
import com.playlist.domain.TvShow_;
import com.playlist.domain.xml.Channel;
import com.playlist.domain.xml.Items;

@RestController
public class ParserController
{
	private final Logger			logger		= LoggerFactory.getLogger( getClass() );
	@Value( "classpath:config.properties" )
	private Resource				configuration;
	@Value( "classpath:preferences.json" )
	private Resource				preferencesFile;
	@Value( "classpath:application.properties" )
	private Resource				propertiesFile;
	@Value( "classpath:tv_channels_AndreConrado.m3u" )
	private Resource				channelFile;
	private Preferences				preferences;
	private Map< String, TvShow_ >	mapTvShows	= new HashMap<>();
	private Logs					clsLogs		= new Logs();
	private final DatabaseReference	firebaseSeries;
	private final DatabaseReference	firebaseLogs;

	public ParserController( @Autowired FirebaseConfig firebaseConfig )
	{
		super();
		DatabaseReference database = firebaseConfig.firebaseDatabse();
		firebaseSeries = database.child( "series" );
		firebaseLogs = database.child( "logs" );
		firebaseSeries.addValueEventListener( new ValueEventListener()
		{
			@Override
			public void onDataChange( DataSnapshot snapshot )
			{
				Map< String, Object > map = ( Map< String, Object > ) snapshot.getValue();
				for ( Entry< String, Object > entry : map.entrySet() )
				{
					String key = entry.getKey();
					try
					{
						TvShow_ value = snapshot.child( key ).getValue( TvShow_.class );
						mapTvShows.put( key, value );
					}
					catch ( Exception e )
					{
						logger.error( key + " :: " + e.getMessage() );
					}
				}
				logger.info( "Loaded " + mapTvShows.size() + " series from firebase" );
			}

			@Override
			public void onCancelled( DatabaseError error )
			{
				// TODO Auto-generated method stub
				System.out.println( "onDataChange" );
			}
		} );
		clsLogs.addLog( "Start App" );
		firebaseLogs.setValue( clsLogs.getLogs(), new CompletionListener()
		{
			@Override
			public void onComplete( DatabaseError error, DatabaseReference ref )
			{
				logger.info( error.getMessage() );
			}
		} );
	}

	@RequestMapping( value = "/version", method = RequestMethod.GET )
	public String version()
	{
		String version = "?";
		try
		{
			Properties properties = new Properties();
			properties.load( propertiesFile.getInputStream() );
			version = properties.getProperty( "application.version" );
		}
		catch ( IOException e )
		{
			logger.error( e.getMessage() );
		}
		clsLogs.addLog( "Check version" );
		firebaseLogs.setValue( clsLogs.getLogs(), new CompletionListener()
		{
			@Override
			public void onComplete( DatabaseError error, DatabaseReference ref )
			{
				logger.info( error.getMessage() );
			}
		} );
		return version;
	}

	@RequestMapping( value = "/username/{userId}/password/{pass}/output/{out}", method = RequestMethod.GET )
	public void process( HttpServletResponse response, @PathVariable String userId, @PathVariable String pass, @PathVariable String out )
	{
		parser( response, userId, pass, "playlist." + out, false );
	}

	@RequestMapping( value = "/series/username/{userId}/password/{pass}/output/{out}", method = RequestMethod.GET )
	public void processSeries( HttpServletResponse response, @PathVariable String userId, @PathVariable String pass, @PathVariable String out )
	{
		seriesParser( response, userId, pass, "playlist." + out, false );
	}

	@RequestMapping( value = "/movies/username/{userId}/password/{pass}/output/{out}", method = RequestMethod.GET )
	public void processMovies( HttpServletResponse response, @PathVariable String userId, @PathVariable String pass, @PathVariable String out )
	{
		moviesParser( response, userId, pass, "playlist." + out, false );
	}

	/**
	 *
	 * The <b>parser</b> method returns {@link ResponseEntity<InputStreamResource>} <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-12-29
	 *
	 * <br>
	 * url example: /parser?username=AndreConrado&password=teste&output=newPlaylist.m3u Heroku example:
	 * https://iptv-playlist-parser.herokuapp.com/parser?username=name&password=pass&output=playlist.m3u8
	 *
	 * @param username
	 * @param password
	 * @param output
	 * @return
	 */
	@RequestMapping( value = "/parser", method = RequestMethod.GET )
	public void parser(	HttpServletResponse response,
						@RequestParam( value = "username", required = true ) String username,
						@RequestParam( value = "password", required = true ) String password,
						@RequestParam( value = "output", required = false, defaultValue = "playlist.m3u8" ) String output,
						@RequestParam( value = "debug", required = false, defaultValue = "false" ) Boolean isDebug )
	{
		try
		{
			final List< String > lines = readFile( isDebug, username, password );
			// read file into stream, try-with-resources
			List< M3UChannel > m3uList = new ArrayList<>();
			String groupName = "Sem grupo";
			int idCount = 1;
			String strRegex = System.getenv( "seriesRegex" );
			if ( StringUtils.isBlank( strRegex ) )
			{
				strRegex = "(.*?)[.\\s][sS](\\d{2}) [eE](\\d{2}).*";
			}
			Pattern p = Pattern.compile( strRegex );
			for ( Iterator< String > iterator = lines.iterator(); iterator.hasNext(); )
			{
				String line = iterator.next();
				if ( line.startsWith( "#EXTINF:" ) )
				{
					Matcher m = p.matcher( StringUtils.substringBetween( line, "tvg-name=\"", "\"" ).trim() );
					if ( !m.matches() )
					{
						if ( line.contains( "►" ) )
						{
							groupName = StringUtils.substringBetween( line, "\"►►►", "◄◄◄\"" ).trim();
						}
						else
						{
							M3UChannel m3uChannel = new M3UChannel();
							String strChannelName = StringUtils.substringBetween( line, "tvg-name=\"", "\"" ).trim();
							String channelUrl = iterator.next();
							m3uChannel.setName( strChannelName );
							m3uChannel.setUrl( channelUrl );
							m3uChannel.setGroupName( groupName );
							m3uChannel.setId( idCount++ );
							m3uList.add( m3uChannel );
						}
					}
				}
			}
			File newPlaylistFile = new File( output );
			newPlaylistFile.delete();
			newPlaylistFile.createNewFile();
			m3uList.sort( new Comparator< M3UChannel >()
			{
				@Override
				public int compare( M3UChannel o1, M3UChannel o2 )
				{
					return StringUtils.compare( o1.getName(), o2.getName() );
				}
			} );
			if ( output.endsWith( ".m3u" ) )
			{
				PrintWriter printWriter = new PrintWriter( newPlaylistFile );
				printWriter.println( "#EXTM3U" );
				int id = 1;
				for ( M3UChannel m3uChannel : m3uList )
				{
					Optional< PreferencesChannel > findPreferenceChannel = getPreferences().findPreferenceChannel( m3uChannel );
					if ( findPreferenceChannel.isPresent() && !findPreferenceChannel.get().isDisable() )
					{
						StringBuilder sb = new StringBuilder();
						// id
						sb.append( "#EXTINF:" );
						if ( findPreferenceChannel.isPresent() && ( findPreferenceChannel.get().getId() > 0 ) )
						{
							sb.append( findPreferenceChannel.get().getId() );
						}
						else
						{
							sb.append( id );
						}
						id++;
						sb.append( " " );
						// Name
						sb.append( "tvg-name=\"" );
						if ( findPreferenceChannel.isPresent() && ( findPreferenceChannel.get().getName().length() > 0 ) )
						{
							sb.append( findPreferenceChannel.get().getName() );
						}
						else
						{
							sb.append( m3uChannel.getName() );
						}
						sb.append( "\" " );
						// Logo
						if ( findPreferenceChannel.isPresent() && ( findPreferenceChannel.get().getLogo().length() > 0 ) )
						{
							sb.append( "tvg-logo=\"" );
							sb.append( findPreferenceChannel.get().getLogo() );
							sb.append( "\" " );
						}
						// Group
						sb.append( "group-title=\"" );
						if ( findPreferenceChannel.isPresent() && ( findPreferenceChannel.get().getGroup().length() > 0 ) )
						{
							sb.append( findPreferenceChannel.get().getGroup() );
						}
						else
						{
							sb.append( m3uChannel.getGroupName() );
						}
						sb.append( "\" " );
						sb.append( ", " );
						sb.append( m3uChannel.getName() );
						printWriter.println( sb.toString() );
						printWriter.println( m3uChannel.getUrl() );
					}
					id++;
				}
				printWriter.flush();
				printWriter.close();
			}
			else
			{
				// #EXTM3U
				// #EXTINF:0,RTP MADEIRA
				// #EXTGRP:groupTest
				// http://megaiptv.dynu.com:6969/live/AndreConrado//81.ts
				PrintWriter printWriter = new PrintWriter( newPlaylistFile );
				printWriter.println( "#EXTM3U" );
				int id = 1;
				for ( M3UChannel m3uChannel : m3uList )
				{
					Optional< PreferencesChannel > findPreferenceChannel = getPreferences().findPreferenceChannel( m3uChannel );
					if ( findPreferenceChannel.isPresent() && !findPreferenceChannel.get().isDisable() )
					{
						StringBuilder sb = new StringBuilder();
						// id
						sb.append( "#EXTINF:" );
						if ( findPreferenceChannel.isPresent() && ( findPreferenceChannel.get().getId() > 0 ) )
						{
							sb.append( findPreferenceChannel.get().getId() );
						}
						else
						{
							sb.append( id );
						}
						id++;
						sb.append( ", " );
						// Name
						if ( findPreferenceChannel.isPresent() && ( findPreferenceChannel.get().getName().length() > 0 ) )
						{
							sb.append( findPreferenceChannel.get().getName() );
						}
						else
						{
							sb.append( m3uChannel.getName() );
						}
						printWriter.println( sb.toString() );
						sb = new StringBuilder();
						// Group
						sb.append( "#EXTGRP:" );
						if ( findPreferenceChannel.isPresent() && ( findPreferenceChannel.get().getGroup().length() > 0 ) )
						{
							sb.append( findPreferenceChannel.get().getGroup() );
						}
						else
						{
							sb.append( m3uChannel.getGroupName() );
						}
						printWriter.println( sb.toString() );
						printWriter.println( m3uChannel.getUrl() );
					}
					id++;
				}
				printWriter.flush();
				printWriter.close();
			}
			/***
			 *
			 */
			// InputStreamResource resource = new InputStreamResource( new FileInputStream(
			// newPlaylistFile ) );
			response.addHeader( HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + output );
			response.setContentType( MediaType.APPLICATION_XML_VALUE );
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream( newPlaylistFile );
			// copy from in to out
			IOUtils.copy( in, out );
			out.close();
			in.close();
			logger.info( "Enviado ficheiro com " + newPlaylistFile.length() + " bytes" );
			newPlaylistFile.delete();
		}
		catch ( Exception e )
		{
			logger.error( e.getMessage() );
			try
			{
				response.sendError( HttpStatus.INTERNAL_SERVER_ERROR.ordinal() );
			}
			catch ( IOException e1 )
			{
				logger.error( e1.getMessage() );
			}
			return;
		}
	}

	/**
	 *
	 * The <b>parser</b> method returns {@link ResponseEntity<InputStreamResource>} <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-12-29
	 *
	 * <br>
	 * url example: /seriesParser?username=AndreConrado&password=teste&output=newPlaylist.m3u Heroku example:
	 * https://iptv-playlist-parser.herokuapp.com/seriesParser?username=name&password=pass&output=playlist.m3u8
	 *
	 * @param username
	 * @param password
	 * @param output
	 * @return
	 */
	@RequestMapping( value = "/seriesParser", method = RequestMethod.GET )
	public void seriesParser(	HttpServletResponse response,
								@RequestParam( value = "username", required = true ) String username,
								@RequestParam( value = "password", required = true ) String password,
								@RequestParam( value = "output", required = false, defaultValue = "playlist.m3u8" ) String output,
								@RequestParam( value = "debug", required = false, defaultValue = "false" ) Boolean isDebug )
	{
		try
		{
			final List< String > lines = readFile( isDebug, username, password );
			// read file into stream, try-with-resources
			List< M3USerie > m3uList = new ArrayList<>();
			int idCount = 1;
			for ( Iterator< String > iterator = lines.iterator(); iterator.hasNext(); )
			{
				String line = iterator.next();
				if ( line.startsWith( "#EXTINF:" ) )
				{
					String strRegex = System.getenv( "seriesRegex" );
					if ( StringUtils.isBlank( strRegex ) )
					{
						strRegex = "(.*?)[.\\s][sS](\\d{2}) [eE](\\d{2}).*";
					}
					Pattern p = Pattern.compile( strRegex );
					line = StringUtils.substringBetween( line, "tvg-name=\"", "\"" ).trim();
					Matcher m = p.matcher( line );
					if ( m.matches() )
					{
						int iSeason = Integer.parseInt( m.group( 2 ) );
						int iEpisodio = Integer.parseInt( m.group( 3 ) );
						M3USerie m3uChannel = new M3USerie();
						m3uChannel.setEpisodio( iEpisodio );
						m3uChannel.setSeason( iSeason );
						m3uChannel.setSerieName( StringUtils.trimToEmpty( m.group( 1 ) ) );
						TvShow_ tvShow = ( TvShow_ ) mapTvShows.get( m3uChannel.getSerieName() );
						if ( tvShow == null && !isDebug )
						{
							// https://www.episodate.com/api/show-details?q=
							String strAuxSerieName = m3uChannel.getSerieName().replaceAll( " ", "-" );
							strAuxSerieName = strAuxSerieName.replace( ".", "" );
							strAuxSerieName = strAuxSerieName.replace( ":", "" );
							String strEpisodate = System.getenv( "episodate" );
							if ( StringUtils.isBlank( strEpisodate ) )
							{
								strEpisodate = "https://www.episodate.com/api/show-details?q=%s";
							}
							URL url = new URL( String.format( strEpisodate, strAuxSerieName.toLowerCase() ) );
							try
							{
								logger.info( "Procurar serie " + m3uChannel.getSerieName() + " :: " + url );
								ObjectMapper objectMapper = new ObjectMapper();
								TvShow tvShowAux = objectMapper.readValue( url, TvShow.class );
								tvShow = tvShowAux.getTvShow();
								firebaseSeries.child( m3uChannel.getSerieName() ).setValue( tvShow, new CompletionListener()
								{
									@Override
									public void onComplete( DatabaseError error, DatabaseReference ref )
									{
										System.out.println( "Complete :: " + ref.getKey() );
									}
								} );
							}
							catch ( Exception e )
							{
								logger.error( "Erro com " + m3uChannel.getSerieName() + " :: " + e.getMessage() );
							}
						}
						if ( tvShow == null )
						{
							mapTvShows.put( m3uChannel.getSerieName(), new TvShow_() );
							m3uChannel.setName( "Season " + m3uChannel.getSeason() + " :: Ep - " + m3uChannel.getEpisodio() );
						}
						else
						{
							mapTvShows.put( m3uChannel.getSerieName(), tvShow );
							Optional< Episode > findFirst =
											tvShow.getEpisodes().stream().filter( p1 -> ( p1.getSeason() == m3uChannel.getSeason() ) &&
												( p1.getEpisode() == m3uChannel.getEpisodio() ) ).findFirst();
							if ( findFirst.isPresent() )
							{
								m3uChannel.setName( "[" + m3uChannel.getSeason() + "x" + m3uChannel.getEpisodio() + "] - " +
									findFirst.get().getName() );
								m3uChannel.setImage( Objects.toString(	findFirst.get().getAdditionalProperties().get( "image_thumbnail_path" ),
																		"" ) );
							}
							else
							{
								m3uChannel.setName( "[Season " + m3uChannel.getSeason() + "] Ep - " + m3uChannel.getEpisodio() );
							}
						}
						m3uChannel.setNomeEpisodio( m3uChannel.getName() );
						String channelUrl = iterator.next();
						m3uChannel.setUrl( channelUrl );
						m3uChannel.setGroupName( m3uChannel.getSerieName() );
						m3uChannel.setId( idCount++ );
						m3uList.add( m3uChannel );
					}
				}
			}
			File newPlaylistFile = new File( output );
			newPlaylistFile.delete();
			newPlaylistFile.createNewFile();
			// #EXTM3U
			// #EXTINF:0,RTP MADEIRA
			// #EXTGRP:groupTest
			// http://megaiptv.dynu.com:6969/live/AndreConrado/F8MYMoq33L/81.ts
			PrintWriter printWriter = new PrintWriter( newPlaylistFile );
			printWriter.println( "#EXTM3U" );
			Collections.sort( m3uList );
			createOuputFile( output, m3uList, printWriter );
			logger.info( "Output :: Series processed - " + m3uList.size() );
			printWriter.flush();
			printWriter.close();
			/***
			 *
			 */
			/***
			 *
			 */
			// InputStreamResource resource = new InputStreamResource( new FileInputStream(
			// newPlaylistFile ) );
			response.addHeader( HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + output );
			response.setContentType( MediaType.APPLICATION_XML_VALUE );
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream( newPlaylistFile );
			// copy from in to out
			IOUtils.copy( in, out );
			out.close();
			in.close();
			System.out.println( "Enviado ficheiro com " + newPlaylistFile.length() + " bytes" );
			newPlaylistFile.delete();
		}
		catch ( Exception e )
		{
			logger.error( e.getMessage() );
			try
			{
				response.sendError( HttpStatus.INTERNAL_SERVER_ERROR.ordinal() );
			}
			catch ( IOException e1 )
			{
				logger.error( e1.getMessage() );
			}
		}
	}

	/**
	 *
	 * The <b>moviesParser</b> method returns {@link ResponseEntity<InputStreamResource>} <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-12-29
	 *
	 * <br>
	 * url example: /moviesParser?username=AndreConrado&password=teste&output=newPlaylist.m3u Heroku example:
	 * https://iptv-playlist-parser.herokuapp.com/moviesParser?username=name&password=pass&output=playlist.m3u8
	 *
	 * @param username
	 * @param password
	 * @param output
	 * @return
	 */
	@RequestMapping( value = "/moviesParser", method = RequestMethod.GET )
	public void moviesParser(	HttpServletResponse response,
								@RequestParam( value = "username", required = true ) String username,
								@RequestParam( value = "password", required = true ) String password,
								@RequestParam( value = "output", required = false, defaultValue = "playlist.m3u8" ) String output,
								@RequestParam( value = "debug", required = false, defaultValue = "false" ) Boolean isDebug )
	{
		try
		{
			final List< String > lines = readFile( isDebug, username, password );
			// read file into stream, try-with-resources
			List< M3USerie > m3uList = new ArrayList<>();
			int idCount = 1;
			String strMoviesTvgName = System.getenv( "movies-tvg-name" );
			if ( StringUtils.isBlank( strMoviesTvgName ) )
			{
				strMoviesTvgName = "►►►►►►   VOD Video Clube   ◄◄◄◄◄◄";
			}
			for ( Iterator< String > iterator = lines.iterator(); iterator.hasNext(); )
			{
				String line = iterator.next();
				if ( line.contains( strMoviesTvgName ) )
				{
					line = iterator.next();
					String strMoviesRegex = System.getenv( "moviesRegex" );
					if ( StringUtils.isBlank( strMoviesRegex ) )
					{
						strMoviesRegex = "tvg-name=\"(.+?)\".*tvg-logo=\"(.+?)\".*group-title=\"(.+?)\"";
					}
					Pattern p = Pattern.compile( strMoviesRegex );
					while ( iterator.hasNext() )
					{
						if ( line.contains( "►►►" ) )
						{
							break;
						}
						Matcher m = p.matcher( line );
						if ( m.find() && m.groupCount() > 1 )
						{
							String movieName = m.group( 1 );
							String movieImage = m.group( 2 );
							String movieGroupName = m.group( 3 );
							M3USerie m3uChannel = new M3USerie();
							m3uChannel.setName( movieName );
							m3uChannel.setImage( movieImage );
							String channelUrl = iterator.next();
							m3uChannel.setUrl( channelUrl );
							m3uChannel.setGroupName( StringUtils.capitalize( movieGroupName.toLowerCase() ) );
							m3uChannel.setId( idCount++ );
							m3uList.add( m3uChannel );
						}
						line = iterator.next();
					}
				}
			}
			File newPlaylistFile = new File( output );
			newPlaylistFile.delete();
			newPlaylistFile.createNewFile();
			// #EXTM3U
			// #EXTINF:0,RTP MADEIRA
			// #EXTGRP:groupTest
			// http://megaiptv.dynu.com:6969/live/AndreConrado/F8MYMoq33L/81.ts
			PrintWriter printWriter = new PrintWriter( newPlaylistFile );
			printWriter.println( "#EXTM3U" );
			Collections.sort( m3uList, new Comparator< M3UChannel >()
			{
				@Override
				public int compare( M3UChannel o1, M3UChannel o2 )
				{
					return ObjectUtils.compare( o1.getName(), o2.getName() );
				}
			} );
			createOuputFile( output, m3uList, printWriter );
			logger.info( "Output :: Movies processed - " + m3uList.size() );
			printWriter.flush();
			printWriter.close();
			/***
			 *
			 */
			/***
			 *
			 */
			// InputStreamResource resource = new InputStreamResource( new FileInputStream(
			// newPlaylistFile ) );
			response.addHeader( HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + output );
			response.setContentType( MediaType.APPLICATION_XML_VALUE );
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream( newPlaylistFile );
			// copy from in to out
			IOUtils.copy( in, out );
			out.close();
			in.close();
			System.out.println( "Enviado ficheiro com " + newPlaylistFile.length() + " bytes" );
			newPlaylistFile.delete();
		}
		catch ( Exception e )
		{
			logger.error( e.getMessage() );
			try
			{
				response.sendError( HttpStatus.INTERNAL_SERVER_ERROR.ordinal() );
			}
			catch ( IOException e1 )
			{
				logger.error( e1.getMessage() );
			}
		}
	}

	/**
	 * The <b>createOuputFile</b> method returns {@link void}
	 * 
	 * @author 62000465 2019-03-18
	 * @param output
	 * @param m3uList
	 * @param printWriter
	 * @param id
	 * @throws IOException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 */
	private void createOuputFile( String output, List< M3USerie > m3uList, PrintWriter printWriter )
		throws IOException, JsonGenerationException, JsonMappingException
	{
		int id = 1;
		if ( output.endsWith( "xml" ) )
		{
			Items items = new Items();
			for ( M3UChannel m3uChannel : m3uList )
			{
				Channel channel = new Channel();
				channel.setTitle( m3uChannel.getName() );
				channel.setDescription( m3uChannel.getImage() );
				channel.setStream_url( m3uChannel.getUrl() );
				channel.setGroup( m3uChannel.getGroupName() );
				items.getChannel().add( channel );
			}
			XmlMapper xmlMapper = new XmlMapper();
			xmlMapper.writeValue( printWriter, items );
			System.out.println( xmlMapper.writeValueAsString( items ) );
		}
		else
		{
			for ( M3UChannel m3uChannel : m3uList )
			{
				StringBuilder sb = new StringBuilder();
				// id
				sb.append( "#EXTINF:" );
				sb.append( id++ );
				sb.append( ", " );
				// Name
				sb.append( m3uChannel.getName() );
				printWriter.println( sb.toString() );
				sb = new StringBuilder();
				// Group
				sb.append( "#EXTGRP:" );
				sb.append( m3uChannel.getGroupName() );
				printWriter.println( sb.toString() );
				printWriter.println( m3uChannel.getUrl() );
			}
		}
	}

	/**
	 * Getter for preferences
	 * 
	 * @author 62000465 2019-03-01
	 * @return the preferences {@link Preferences}
	 */
	private Preferences getPreferences()
	{
		if ( preferences == null )
		{
			preferences = new Preferences();
			ObjectMapper mapper = new ObjectMapper();
			try
			{
				preferences = mapper.readValue( preferencesFile.getInputStream(), Preferences.class );
			}
			catch ( Exception e )
			{
				logger.error( e.getMessage(), e );
			}
		}
		return preferences;
	}

	/**
	 * The <b>readFile</b> method returns {@link List<String>}
	 * 
	 * @author 62000465 2019-03-01
	 * @param isDebug
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private List< String > readFile( Boolean isDebug, String username, String password ) throws Exception
	{
		List< String > lines = new ArrayList<>();
		if ( isDebug )
		{
			try (	InputStream is = new FileInputStream( channelFile.getFile() );
					BufferedReader reader = new BufferedReader( new InputStreamReader( is ) );
					Stream< String > stream = reader.lines() )
			{
				stream.forEach( p -> lines.add( StringUtils.trimToEmpty( p ) ) );
			}
		}
		else
		{
			String url = System.getenv().get( "megaIPTVUrl" );
			if ( StringUtils.isBlank( url ) )
			{
				if ( isDebug )
				{
					System.out.println( "Obter o url do MegaIPTV pelo ficheiro de config: config.properties" );
				}
				Properties properties = new Properties();
				properties.load( configuration.getInputStream() );
				url = properties.getProperty( "megaIPTVUrl" );
			}
			url = url.replace( "{username}", username );
			url = url.replace( "{password}", password );
			try (	InputStream is = new URL( url ).openConnection().getInputStream();
					BufferedReader reader = new BufferedReader( new InputStreamReader( is ) );
					Stream< String > stream = reader.lines() )
			{
				stream.forEach( p -> lines.add( p ) );
				if ( isDebug )
				{
					logger.info( "Carregado o ficheiro do url: " + url );
				}
			}
		}
		logger.info( "Readed " + lines.size() + " lines." );
		return lines;
	}
}
