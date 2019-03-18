package com.playlist;

import java.io.ByteArrayInputStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Configuration
public class FirebaseConfig {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	public DatabaseReference firebaseDatabse() {
		DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
		return firebase;
	}

	@PostConstruct
	public void init() {

		/**
		 * https://firebase.google.com/docs/server/setup
		 * 
		 * Create service account , download json
		 */
		String strFirebase = System.getenv("firebase");
		FirebaseOptions options;
		try {
			options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(strFirebase.getBytes())))
					.setDatabaseUrl("https://iptv-parser.firebaseio.com").build();

			FirebaseApp.initializeApp(options);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
}
