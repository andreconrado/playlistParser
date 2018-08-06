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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.domain.M3UChannel;
import com.playlist.domain.Preferences;
import com.playlist.domain.PreferencesChannel;

@RestController
public class ParserController {
	@Value("classpath:config.properties")
	private Resource configuration;
	@Value("classpath:preferences.json")
	private Resource preferencesFile;

	@Value("classpath:application.properties")
	private Resource propertiesFile;

	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public String version() {
		String version = "?";
		try {
			Properties properties = new Properties();
			properties.load(propertiesFile.getInputStream());
			version = properties.getProperty("application.version");
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return version;
	}

	@RequestMapping(value = "/conrado", method = RequestMethod.GET)
	public void conrado(HttpServletResponse response) {
		parser(response, "AndreConrado", "F8MYMoq33L", "playlist.m3u8");
	}

	/**
	 *
	 * The <b>parser</b> method returns {@link ResponseEntity<InputStreamResource>}
	 * <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-12-29
	 *
	 * <br>
	 * url example:
	 * /parser?username=AndreConrado&password=teste&output=newPlaylist.m3u Heroku
	 * example:
	 * https://iptv-playlist-parser.herokuapp.com/parser?username=name&password=pass&output=playlist.m3u8
	 *
	 * @param username
	 * @param password
	 * @param output
	 * @return
	 */
	@RequestMapping(value = "/parser", method = RequestMethod.GET)
	public void parser(HttpServletResponse response, @RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "output", required = false, defaultValue = "playlist.m3u8") String output) {
		try {
			Properties properties = new Properties();
			properties.load(configuration.getInputStream());
			String url = properties.getProperty("megaIPTVUrl");
			url = url.replace("{username}", username);
			url = url.replace("{password}", password);
			ObjectMapper mapper = new ObjectMapper();
			Preferences preferences = new Preferences();
			preferences = mapper.readValue(preferencesFile.getInputStream(), Preferences.class);
			final List<String> lines = new LinkedList<>();
			try (InputStream is = new URL(url).openConnection().getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					Stream<String> stream = reader.lines()) {
				stream.forEach(p -> lines.add(p));
				System.out.println("Carregado o ficheiro do url: " + url);
			} catch (IOException e) {
				System.err.println(e.getMessage());
				response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
				return;
			}
			// read file into stream, try-with-resources
			List<M3UChannel> m3uList = new ArrayList<>();
			String groupName = "Sem grupo";
			int idCount = 1;
			for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
				String line = iterator.next();
				if (line.startsWith("#EXTINF:")) {
					if (line.contains("►")) {
						groupName = line.split("►►►")[1].replaceAll("◄", "");
					} else {
						M3UChannel m3uChannel = new M3UChannel();
						line = line.split(",")[1];
						String channelUrl = iterator.next();
						m3uChannel.setName(line);
						m3uChannel.setUrl(channelUrl);
						m3uChannel.setGroupName(groupName);
						m3uChannel.setId(idCount++);
						m3uList.add(m3uChannel);
					}
				}
			}
			File newPlaylistFile = new File(output);
			newPlaylistFile.delete();
			newPlaylistFile.createNewFile();
			if (output.endsWith(".m3u")) {
				PrintWriter printWriter = new PrintWriter(newPlaylistFile);
				printWriter.println("#EXTM3U");
				int id = 1;
				for (M3UChannel m3uChannel : m3uList) {
					Optional<PreferencesChannel> findPreferenceChannel = preferences.findPreferenceChannel(m3uChannel);
					if (findPreferenceChannel.isPresent() && !findPreferenceChannel.get().isDisable()) {
						StringBuilder sb = new StringBuilder();
						// id
						sb.append("#EXTINF:");
						if (findPreferenceChannel.isPresent() && (findPreferenceChannel.get().getId() > 0)) {
							sb.append(findPreferenceChannel.get().getId());
						} else {
							sb.append(id);
						}
						id++;
						sb.append(" ");
						// Name
						sb.append("tvg-name=\"");
						if (findPreferenceChannel.isPresent() && (findPreferenceChannel.get().getName().length() > 0)) {
							sb.append(findPreferenceChannel.get().getName());
						} else {
							sb.append(m3uChannel.getName());
						}
						sb.append("\" ");
						// Logo
						if (findPreferenceChannel.isPresent() && (findPreferenceChannel.get().getLogo().length() > 0)) {
							sb.append("tvg-logo=\"");
							sb.append(findPreferenceChannel.get().getLogo());
							sb.append("\" ");
						}
						// Group
						sb.append("group-title=\"");
						if (findPreferenceChannel.isPresent()
								&& (findPreferenceChannel.get().getGroup().length() > 0)) {
							sb.append(findPreferenceChannel.get().getGroup());
						} else {
							sb.append(m3uChannel.getGroupName());
						}
						sb.append("\" ");
						sb.append(", ");
						sb.append(m3uChannel.getName());
						printWriter.println(sb.toString());
						printWriter.println(m3uChannel.getUrl());
					}
					id++;
				}
				printWriter.flush();
				printWriter.close();
			} else {
				// #EXTM3U
				// #EXTINF:0,RTP MADEIRA
				// #EXTGRP:groupTest
				// http://megaiptv.dynu.com:6969/live/AndreConrado//81.ts
				PrintWriter printWriter = new PrintWriter(newPlaylistFile);
				printWriter.println("#EXTM3U");
				int id = 1;
				for (M3UChannel m3uChannel : m3uList) {
					Optional<PreferencesChannel> findPreferenceChannel = preferences.findPreferenceChannel(m3uChannel);
					if (findPreferenceChannel.isPresent() && !findPreferenceChannel.get().isDisable()) {
						StringBuilder sb = new StringBuilder();
						// id
						sb.append("#EXTINF:");
						if (findPreferenceChannel.isPresent() && (findPreferenceChannel.get().getId() > 0)) {
							sb.append(findPreferenceChannel.get().getId());
						} else {
							sb.append(id);
						}
						id++;
						sb.append(", ");
						// Name
						if (findPreferenceChannel.isPresent() && (findPreferenceChannel.get().getName().length() > 0)) {
							sb.append(findPreferenceChannel.get().getName());
						} else {
							sb.append(m3uChannel.getName());
						}
						printWriter.println(sb.toString());
						sb = new StringBuilder();
						// Group
						sb.append("#EXTGRP:");
						if (findPreferenceChannel.isPresent()
								&& (findPreferenceChannel.get().getGroup().length() > 0)) {
							sb.append(findPreferenceChannel.get().getGroup());
						} else {
							sb.append(m3uChannel.getGroupName());
						}
						printWriter.println(sb.toString());
						printWriter.println(m3uChannel.getUrl());
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

			response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + output);
			response.setContentType(MediaType.APPLICATION_XML_VALUE);

			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(newPlaylistFile);

			// copy from in to out
			IOUtils.copy(in, out);

			out.close();
			in.close();
			System.out.println("Enviado ficheiro com " + newPlaylistFile.length() + " bytes");
			newPlaylistFile.delete();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
