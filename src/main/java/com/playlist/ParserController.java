package com.playlist;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParserController {

	@Value("classpath:config.properties")
	private Resource res;

	@Value("classpath:newPlaylist.m3u8")
	private Resource playlistTEST;

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public String ping() {
		return "pong";
	}

	@RequestMapping(value = "/parser", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> hello(@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "output", required = false, defaultValue = "playlist.m3u") String output) {

		try {
			Properties properties = new Properties();
			properties.load(res.getInputStream());

			String url = properties.getProperty("megaIPTVUrl");
			url = url.replace("{username}", username);
			url = url.replace("{password}", password);

			InputStreamResource resource = new InputStreamResource(playlistTEST.getInputStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + output)
					.contentType(MediaType.ALL).body(resource);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return ResponseEntity.notFound().build();
	}
}
