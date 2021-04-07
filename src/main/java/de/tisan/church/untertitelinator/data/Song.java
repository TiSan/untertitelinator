package de.tisan.church.untertitelinator.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class Song
{
	File dataFile;
	List<String> lines;

	public Song(File file)
	{
		this.dataFile = file;
		readLines();
	}

	public Song()
	{
	}

	public List<String> getLines()
	{
		return lines;
	}

	public File getDataFile()
	{
		return dataFile;
	}

	public void setDataFile(File dataFile)
	{
		this.dataFile = dataFile;
	}

	private void readLines()
	{
		List<String> lines = new ArrayList<>();

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll((String) JSONPersistence.get().getSetting(UTPersistenceConstants.LINESEPARATOR, "<BR>"), "\n").trim();
				if(line.startsWith("#") || line.startsWith("//")) {
					continue;
				}
				lines.add(line);
			}
			reader.close();
		}
		catch(Exception e)
		{
			Logger.getInstance().err("Error while reading Song file " + e.getMessage(), e, getClass());
		}

		this.lines = lines;
	}
	
	@JsonIgnore
	public String getTitle()
	{
		return this.lines.stream().filter(line -> line.toLowerCase().startsWith("title:")).findFirst()
		        .map(line -> line.split(":", 2)[1]).orElse("Titel in Datei nicht definiert.");
	}
	
	@JsonIgnore
	public List<String> getSongLines()
	{
		return this.lines.stream().filter(line -> line.toLowerCase().startsWith("title:") == false)
		        .collect(Collectors.toList());
	}

}
