package de.tisan.church.untertitelinator.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Song {
	File dataFile;
	List<String> lines;

	public Song(File file) {
		this.dataFile = file;
		readLines();
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	private void readLines() {
		List<String> lines = new ArrayList<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.lines = lines;
	}

	public String getTitle() {
		return this.lines.stream().filter(line -> line.toLowerCase().startsWith("title:")).findFirst()
				.map(line -> line.split(":", 2)[1]).orElse("Titel in Datei nicht definiert.");
	}

	public List<String> getSongLines() {
		return this.lines.stream().filter(line -> line.toLowerCase().startsWith("title:") == false)
				.collect(Collectors.toList());
	}

}
