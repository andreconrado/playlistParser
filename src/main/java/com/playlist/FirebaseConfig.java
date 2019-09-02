package com.playlist;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig
{
	private final Logger logger = LoggerFactory.getLogger( FirebaseConfig.class );

	@PostConstruct
	public void init()
	{
		/**
		 * https://firebase.google.com/docs/server/setup
		 * 
		 * Create service account , download json
		 */
		String strFirebase = System.getenv( "firebase" );
		FirebaseOptions options;
		try
		{
			for ( FirebaseApp firebaseApp : FirebaseApp.getApps() )
			{
				logger.info( firebaseApp.getName() );
				logger.info( firebaseApp.getOptions().getDatabaseUrl() );
				if ( firebaseApp.getName().equals( FirebaseApp.DEFAULT_APP_NAME ) )
				{
					firebaseApp.delete();
				}
			}
			Proxy proxy = getProxy();
			HttpTransport httpTransport = new NetHttpTransport.Builder().build();
			if ( proxy != null )
			{
				httpTransport = new NetHttpTransport.Builder().setProxy( proxy ).build();
			}
			options = new FirebaseOptions.Builder()
							.setCredentials( GoogleCredentials.fromStream( new ByteArrayInputStream( strFirebase.getBytes() ) ) )
							.setDatabaseUrl( "https://iptv-parser.firebaseio.com" ).setHttpTransport( httpTransport ).build();
			logger.info( options.getProjectId() );
			logger.info( "Start Firebase App!" );
			FirebaseApp.initializeApp( options );
		}
		catch ( Exception e )
		{
			logger.error( e.getMessage() );
		}
	}

	/**
	 * The <b>printProxyInfo</b> method returns {@link void}
	 * 
	 * @author 62000465 2019-03-20
	 */
	private Proxy getProxy()
	{
		List< String > lstEnv =
						Arrays.asList( "http.proxyHost", "http.proxyPort", "https.proxyHost", "https.proxyPort", "socksProxyHost", "socksProxyPort" );
		String strHost = "";
		Integer iPort = -1;
		for ( Iterator< String > iterator = lstEnv.iterator(); iterator.hasNext(); )
		{
			String strEnvPropertie = iterator.next();
			String strEnvPropertieValue = System.getenv( strEnvPropertie );
			if ( StringUtils.isNotBlank( strEnvPropertieValue ) && StringUtils.isBlank( strHost ) )
			{
				strHost = strEnvPropertieValue;
			}
			if ( StringUtils.isNotBlank( strEnvPropertieValue ) && iPort < 0 && StringUtils.isNumeric( strEnvPropertieValue ) )
			{
				iPort = Integer.parseInt( strEnvPropertieValue );
			}
		}
		if ( StringUtils.isNotBlank( strHost ) && iPort > 0 )
		{
			logger.info( "Proxy host :: " + strHost );
			logger.info( "Proxy port :: " + iPort );
			return new Proxy( Proxy.Type.HTTP, new InetSocketAddress( strHost, iPort ) );
		}
		else
		{
			return null;
		}
	}
}
