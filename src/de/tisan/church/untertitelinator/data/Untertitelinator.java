package de.tisan.church.untertitelinator.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Untertitelinator {

	File currentLineFile;
	List<Song> songs;
	private SongPlayer currentPlayer;

	public Untertitelinator() {
		songs = new ArrayList<Song>();
		loadSongs();
	}

	private void loadSongs() {
		File songDir = new File("songs/");
		if (songDir.exists() == false) {
			songDir.mkdirs();
			return;
		}

		File[] songFiles = songDir.listFiles();
		for (File song : songFiles) {
			if (song.getName().toLowerCase().endsWith(".song")) {
				songs.add(new Song(song));
			}
		}
	}

	public List<Song> getSongs() {
		return songs;
	}

	public SongPlayer createSongPlayerForSong(Song song) {
		return new SongPlayer(song);
	}

	public void switchSong(Song song) {
		this.currentPlayer = createSongPlayerForSong(song);
	}

	public SongPlayer getCurrentPlayer() {
		return currentPlayer;
	}
}
