package com.playlist;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Configuration
public class FirebaseConfig {

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
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}
}
