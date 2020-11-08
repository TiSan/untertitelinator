package de.tisan.church.untertitelinator.data;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import de.tisan.church.untertitelinator.settings.JSONPersistence;
import de.tisan.church.untertitelinator.settings.PersistenceConstants;

public class Untertitelinator
{
	public static final String VERSION = "0.6.3";
	
	File currentLineFile;
	List<Song> songs;
	private SongPlayer currentPlayer;

	public Untertitelinator()
	{
		songs = new ArrayList<Song>();
		loadSongs();
	}

	private void loadSongs()
	{
		File songDir = new File(
		        (String) JSONPersistence.get().getSetting(PersistenceConstants.SONGSFOLDERPATH, "songs/"));
		if (songDir.exists() == false)
		{
			songDir.mkdirs();
			return;
		}
		FilenameFilter filter = new FilenameFilter()
		{

			@Override
			public boolean accept(File dir, String name)
			{
				return name.toLowerCase().endsWith(
				        (String) JSONPersistence.get().getSetting(PersistenceConstants.SONGFILESUFFIX, ".song"));
			}

		};

		for (File song : songDir.listFiles(filter))
		{
			songs.add(new Song(song));
		}
	}

	public List<Song> getSongs()
	{
		return songs;
	}

	private SongPlayer createSongPlayerForSong(Song song)
	{
		return new SongPlayer(song);
	}

	public void switchSong(Song song)
	{
		SongPlayer newSongPlayer = createSongPlayerForSong(song);
		if (this.currentPlayer != null && this.currentPlayer.isPaused())
		{
			newSongPlayer.pause();
		}
		this.currentPlayer = newSongPlayer;
	}

	public SongPlayer getCurrentPlayer()
	{
		return currentPlayer;
	}
}
