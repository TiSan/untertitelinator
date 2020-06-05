package de.tisan.church.untertitelinator.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tisan.church.untertitelinator.ccli.CCLIParser;
import de.tisan.church.untertitelinator.ccli.CCLISong;
import de.tisan.church.untertitelinator.settings.JSONPersistence;
import de.tisan.church.untertitelinator.settings.PersistenceConstants;

public class UntertitelinatorServer {
	public static UntertitelinatorServer get() {
		return (instance == null ? (instance = new UntertitelinatorServer()) : instance);
	}

	private static UntertitelinatorServer instance;

	private List<CCLISong> songs;

	private UntertitelinatorServer() {
		this.songs = new ArrayList<CCLISong>();
		loadSongs();
	}

	public void loadSongs() {
		File directory = new File(
				(String) JSONPersistence.get().getSetting(PersistenceConstants.CCLISONGSPATH, "ccli/"));
		if (directory.exists() == false) {
			directory.mkdir();
		}
		for (File file : directory.listFiles()) {
			if (file.getName().toLowerCase().endsWith(".sng")) {
				try {
					songs.add(CCLIParser.parseSong(file));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<CCLISong> getSongs() {
		return songs;
	}

}
