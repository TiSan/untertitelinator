package de.tisan.church.untertitelinator.data;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.tisan.church.untertitelinator.churchtools.api.ChurchToolsApi;
import de.tisan.church.untertitelinator.churchtools.api.objects.Event;
import de.tisan.church.untertitelinator.churchtools.api.objects.Service;
import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.EventSelectionChangedPacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.instancer.packets.SongListChangedPacket;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class Untertitelinator
{
	public static final String VERSION = Untertitelinator.class.getPackage().getImplementationVersion();

	File currentLineFile;
	List<Song> songs;
	private SongPlayer currentPlayer;

	private List<Event> events;

	private Event currentEvent;

	private List<Service> services;

	private static Untertitelinator instance;

//	public static Untertitelinator get()
//	{
//		if (instance == null)
//		{
//			instance = new Untertitelinator();
//		}
//		return instance;
//	}

	public Untertitelinator() {
		songs = new ArrayList<Song>();
		// loadSongs();

		UTEventHub.get().registerListener(new UTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				if (packet instanceof CommandPacket) {
					CommandPacket sPacket = (CommandPacket) packet;
					switch (sPacket.getCommand()) {
					case CHANGE_SONG:
						switchSong(getSongs().stream().filter(s -> s.getTitle().equals(sPacket.getArgs().get(0)))
								.findFirst().get());
						break;
					case SEND_EVENT:
						UTEventHub.get().publish(new EventSelectionChangedPacket(getCurrentEvent(), getServiceList()));
						break;
					case LOAD_SONGS:
						loadSongs();
						break;
					case SELECT_EVENT:
						selectEvent(Integer.valueOf(sPacket.getArgs().get(0)));
						break;
					case LOAD_EVENTS:
						loadEvents();
						selectEvent(events.get(0));
						break;
					default:
						break;
					}
				}
			}
		});
	}

	public synchronized void loadSongs() {
		songs.clear();
		File songDir = new File(
				(String) JSONPersistence.get().getSetting(UTPersistenceConstants.SONGSFOLDERPATH, "songs/"));
		if (songDir.exists() == false) {
			songDir.mkdirs();
			return;
		}
		FilenameFilter filter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(
						(String) JSONPersistence.get().getSetting(UTPersistenceConstants.SONGFILESUFFIX, ".song"));
			}

		};

		for (File song : songDir.listFiles(filter)) {
			songs.add(new Song(song));
		}

		if(songs.isEmpty() == false) {
			UTEventHub.get().publish(new SongListChangedPacket(songs), new CommandPacket(Command.CHANGE_SONG, songs.get(0).getTitle()));
		}
	}

	public List<Song> getSongs() {
		return songs;
	}

	private SongPlayer createSongPlayerForSong(Song song) {
		return new SongPlayer(song);
	}

	private void switchSong(Song song) {
		SongPlayer newSongPlayer = createSongPlayerForSong(song);
		newSongPlayer.enable();
		if (this.currentPlayer != null && this.currentPlayer.isPaused()) {
			newSongPlayer.pause();
		}
		if (this.currentPlayer != null) {
			this.currentPlayer.disable();
		}
		this.currentPlayer = newSongPlayer;
		this.currentPlayer.updateEvent();
	}

	public SongPlayer getCurrentPlayer() {
		return currentPlayer;
	}

	public void loadEvents() {
		events = ChurchToolsApi.get().getEvents().orElse(Collections.emptyList());
		services = ChurchToolsApi.get().getServices().orElse(Collections.emptyList());
	}

	public List<EventService> getServiceList() {
		Set<EventService> eventServices = new HashSet<EventService>();
		for (de.tisan.church.untertitelinator.churchtools.api.objects.EventService es : currentEvent
				.getEventServices()) {
			if (es.getName() == null || es.getName().trim().isEmpty()) {
				continue;
			}

			Service s = services.parallelStream().filter(ss -> ss.getId() == es.getServiceId()).findFirst().get();
			if (s.getComment().equals("<NOT_VISIBLE>")) {
				continue;
			}
			String serviceName = s.getComment().isEmpty() == false ? s.getComment() : s.getName();

			EventService service = eventServices.stream().filter(e -> e.getName().equalsIgnoreCase(serviceName))
					.findFirst().orElse(new EventService(serviceName));
			service.addCast(es.getName());
			eventServices.add(service);
		}
		return eventServices.stream().sorted(Comparator.comparing(EventService::getName)).collect(Collectors.toList());
	}

	public void selectEvent(int no) {
		if (no >= 0 && no < events.size()) {
			selectEvent(events.get(no));
		}
	}

	public void selectEvent(Event event) {
		if (event != null) {
			currentEvent = event;
			UTEventHub.get().publish(new EventSelectionChangedPacket(event, getServiceList()));
		}
	}

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public List<Event> getAllEvents() {
		return events;
	}
}
