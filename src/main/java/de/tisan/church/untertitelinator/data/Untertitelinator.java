package de.tisan.church.untertitelinator.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import de.tisan.church.untertitelinator.churchtools.api.ChurchToolsApi;
import de.tisan.church.untertitelinator.churchtools.api.objects.Event;
import de.tisan.church.untertitelinator.churchtools.api.objects.EventService;
import de.tisan.church.untertitelinator.churchtools.api.objects.Service;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class Untertitelinator {
	public static final String VERSION = Untertitelinator.class.getPackage().getImplementationVersion();

	File currentLineFile;
	List<Song> songs;
	private SongPlayer currentPlayer;

	private List<Event> events;

	private Event currentEvent;

	private List<Service> services;

	private static Untertitelinator instance;

	public static Untertitelinator get() {
		if (instance == null) {
			instance = new Untertitelinator();
		}
		return instance;
	}

	private Untertitelinator() {
		songs = new ArrayList<Song>();
	}

	public void loadSongs() {
		File songDir = new File(
				(String) JSONPersistence.get().getSetting(UTPersistenceConstants.SONGSFOLDERPATH, "songs/"));
		if (songDir.exists() == false) {
			songDir.mkdirs();
			return;
		}
		songs = Arrays
				.asList(songDir.listFiles((dir, name) -> name.toLowerCase().endsWith(
						(String) JSONPersistence.get().getSetting(UTPersistenceConstants.SONGFILESUFFIX, ".song"))))
				.parallelStream().map(Song::new).collect(Collectors.toList());
	}

	public List<Song> getSongs() {
		return songs;
	}

	private SongPlayer createSongPlayerForSong(Song song) {
		return new SongPlayer(song);
	}

	public void switchSong(Song song) {
		SongPlayer newSongPlayer = createSongPlayerForSong(song);
		if (this.currentPlayer != null && this.currentPlayer.isPaused()) {
			newSongPlayer.pause();
		}
		this.currentPlayer = newSongPlayer;
	}

	public SongPlayer getCurrentPlayer() {
		return currentPlayer;
	}

	public void loadEvents() {
		events = ChurchToolsApi.get().getEvents().orElse(Collections.emptyList());
		services = ChurchToolsApi.get().getServices().orElse(Collections.emptyList());
	}

	public Map<String, String> getServiceList() {
		Map<String, String> serviceListStr = new TreeMap<String, String>();
		
		for (EventService es : currentEvent.getEventServices()) {
			if (es.getName() == null || es.getName().trim().isEmpty()) {
				continue;
			}

			Service s = services.parallelStream().filter(ss -> ss.getId() == es.getServiceId()).findFirst().get();
			if (s.getComment().equals("<NOT_VISIBLE>")) {
				continue;
			}
			String key = s.getComment().isEmpty() == false ? s.getComment() : s.getName();
			if (serviceListStr.containsKey(key)) {
				serviceListStr.put(key, serviceListStr.get(key) + ", " + es.getName());
			} else {
				serviceListStr.put(key, es.getName());

			}
		}

		return serviceListStr;
	}

	public void selectEvent(int no) {
		if (no >= 0 && no < events.size()) {
			selectEvent(events.get(no));
		}
	}

	public void selectEvent(Event event) {
		if (event != null) {
			currentEvent = event;
		}
	}

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public List<Event> getAllEvents() {
		return events;
	}
}
