package com.playlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author anco62000465 2017-09-27
 *
 */
@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}

// /**
// * The <b>main</b> method returns {@link void} <br>
// * <br>
// * <b>author</b> anco62000465 2017-09-27
// *
// * @param args
// */
// public static void main2(String[] args) {
// Properties properties = new Properties();
// try (FileInputStream input = new FileInputStream("config.properties")) {
// properties.load(input);
// } catch (IOException e2) {
// e2.printStackTrace();
// return;
// }
// ObjectMapper mapper = new ObjectMapper();
// Preferences preferences = new Preferences();
// try {
// preferences = mapper.readValue(new
// File(properties.getProperty("preferencesFile")), Preferences.class);
// } catch (IOException e1) {
// e1.printStackTrace();
// return;
// }
// final List<String> lines = new ArrayList<>();
// String originalm3uFile = properties.getProperty("originalm3uFile");
// try (InputStream is = new
// URL(originalm3uFile).openConnection().getInputStream();
// BufferedReader reader = new BufferedReader(new InputStreamReader(is));
// Stream<String> stream = reader.lines()) {
// stream.forEach(p -> lines.add(p));
// System.out.println("Carregado o ficheiro do url: " + originalm3uFile);
// } catch (IOException e) {
// System.err.println(e.getMessage());
// }
//
// if ((lines == null) || lines.isEmpty()) {
// System.out.println("A tentar carregar ficheiro local: " + originalm3uFile);
// try {
// lines.addAll(Files.readAllLines(Paths.get(originalm3uFile)));
// } catch (IOException e) {
// System.err.println(e.getMessage());
// return;
// }
// }
// // read file into stream, try-with-resources
// List<M3UChannel> m3uList = new ArrayList<>();
//
// String groupName = "Sem grupo";
// int idCount = 1;
// for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
// String line = iterator.next();
// if (line.startsWith("#EXTINF:")) {
// if (line.contains("►")) {
// groupName = line.split("►►►")[1].replaceAll("◄", "");
// } else {
// M3UChannel m3uChannel = new M3UChannel();
// line = line.split(",")[1];
// String url = iterator.next();
// m3uChannel.setName(line);
// m3uChannel.setUrl(url);
// m3uChannel.setGroupName(groupName);
// m3uChannel.setId(idCount++);
// m3uList.add(m3uChannel);
// }
// }
// }
//
// try {
// File newPlaylistFile = new File(properties.getProperty("outputm3uFile"));
// newPlaylistFile.delete();
// newPlaylistFile.createNewFile();
// PrintWriter printWriter = new PrintWriter(newPlaylistFile);
// printWriter.println("#EXTM3U");
// int id = 1;
// for (M3UChannel m3uChannel : m3uList) {
// Optional<PreferencesChannel> findPreferenceChannel =
// preferences.findPreferenceChannel(m3uChannel);
// if (findPreferenceChannel.isPresent() &&
// !findPreferenceChannel.get().isDisable()) {
// StringBuilder sb = new StringBuilder();
// // id
// sb.append("#EXTINF:");
// if (findPreferenceChannel.isPresent() && (findPreferenceChannel.get().getId()
// > 0)) {
// sb.append(findPreferenceChannel.get().getId());
// } else {
// sb.append(id);
// }
// id++;
// sb.append(" ");
// // Name
// sb.append("tvg-name=\"");
// if (findPreferenceChannel.isPresent() &&
// (findPreferenceChannel.get().getName().length() > 0)) {
// sb.append(findPreferenceChannel.get().getName());
// } else {
// sb.append(m3uChannel.getName());
// }
// sb.append("\" ");
// // Logo
// if (findPreferenceChannel.isPresent() &&
// (findPreferenceChannel.get().getLogo().length() > 0)) {
// sb.append("tvg-logo=\"");
// sb.append(findPreferenceChannel.get().getLogo());
// sb.append("\" ");
// }
// // Group
// sb.append("group-title=\"");
// if (findPreferenceChannel.isPresent() &&
// (findPreferenceChannel.get().getGroup().length() > 0)) {
// sb.append(findPreferenceChannel.get().getGroup());
// } else {
// sb.append(m3uChannel.getGroupName());
// }
// sb.append("\" ");
// sb.append(", ");
// sb.append(m3uChannel.getName());
// printWriter.println(sb.toString());
// printWriter.println(m3uChannel.getUrl());
// }
// id++;
// }
// printWriter.flush();
// printWriter.close();
// } catch (IOException e) {
// e.printStackTrace();
// }
// try {
// // #EXTM3U
// // #EXTINF:0,RTP MADEIRA
// // #EXTGRP:groupTest
// // http://megaiptv.dynu.com:6969/live/AndreConrado/F8MYMoq33L/81.ts
// File newPlaylistFile = new File(properties.getProperty("outputm3u8File"));
// newPlaylistFile.delete();
// newPlaylistFile.createNewFile();
// PrintWriter printWriter = new PrintWriter(newPlaylistFile);
// printWriter.println("#EXTM3U");
// int id = 1;
// for (M3UChannel m3uChannel : m3uList) {
// Optional<PreferencesChannel> findPreferenceChannel =
// preferences.findPreferenceChannel(m3uChannel);
// if (findPreferenceChannel.isPresent() &&
// !findPreferenceChannel.get().isDisable()) {
// StringBuilder sb = new StringBuilder();
// // id
// sb.append("#EXTINF:");
// if (findPreferenceChannel.isPresent() && (findPreferenceChannel.get().getId()
// > 0)) {
// sb.append(findPreferenceChannel.get().getId());
// } else {
// sb.append(id);
// }
// id++;
// sb.append(", ");
// // Name
// if (findPreferenceChannel.isPresent() &&
// (findPreferenceChannel.get().getName().length() > 0)) {
// sb.append(findPreferenceChannel.get().getName());
// } else {
// sb.append(m3uChannel.getName());
// }
// printWriter.println(sb.toString());
// System.out.println(sb.toString());
// sb = new StringBuilder();
// // Group
// sb.append("#EXTGRP:");
// if (findPreferenceChannel.isPresent() &&
// (findPreferenceChannel.get().getGroup().length() > 0)) {
// sb.append(findPreferenceChannel.get().getGroup());
// } else {
// sb.append(m3uChannel.getGroupName());
// }
// printWriter.println(sb.toString());
// printWriter.println(m3uChannel.getUrl());
// }
// id++;
// }
// printWriter.flush();
// printWriter.close();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// }
