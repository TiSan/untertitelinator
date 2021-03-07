package de.tisan.church.untertitelinator.data;

import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class SongPlayer {
	Song song;
	int index;
	boolean pause;

	public SongPlayer(Song song) {
		this.song = song;
		setIndex(0);
	}

	private void setIndex(int index) {
		this.index = index;
	}

	public void nextLine() {
		setIndex(index + 1);
		if (index >= song.getSongLines().size()) {
			setIndex(song.getSongLines().size() - 1);
		}
	}

	public void previousLine() {
		setIndex(index - 1);
		if (index < 0) {
			setIndex(0);
		}
	}

	public void jumpToStart() {
		setIndex(0);
	}

	public void jumpToEnd() {
		setIndex(song.getSongLines().size() - 1);
	}

	public boolean isOnEnd() {
		return index >= song.getSongLines().size() - 1;
	}

	public String getCurrentLine() {
		return pause ? getBlackoutLine() : song.getSongLines().get(index);
	}
	
	public String getNextLine() {
		return pause ? getBlackoutLine() : (isValidIndex(index + 1) ? song.getSongLines().get(index + 1) : getBlackoutLine());
	}

	private boolean isValidIndex(int i) {
		return i >= 0 && i <= song.getSongLines().size() - 1;
	}

	public String getBlackoutLine() {
		return (String) JSONPersistence.get().getSetting(UTPersistenceConstants.BLACKOUTLINEFILLER, "    ");
	}

	public String getTitle() {
		return pause ? getBlackoutLine() : song.getTitle();
	}

	public void pause() {
		pause = !pause;
	}

	public Song getSong() {
		return song;
	}

	public int getCurrentIndex() {
		return index;
	}

	public boolean isPaused() {
		return pause;
	}
}
