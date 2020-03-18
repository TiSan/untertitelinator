package de.tisan.church.untertitelinator.data;

public class SongPlayer {
	Song song;
	int index;

	public SongPlayer(Song song) {
		this.song = song;
		index = 0;
	}

	public void nextLine() {
		index++;
		if (index >= song.getSongLines().size()) {
			index = song.getSongLines().size() - 1;
		}
	}

	public void previousLine() {
		index--;
		if (index < 0) {
			index = 0;
		}
	}

	public void jumpToStart() {
		index = 0;
	}

	public void jumpToEnd() {
		index = song.getSongLines().size() - 1;
	}
	
	public boolean isOnEnd() {
		return index >= song.getSongLines().size() - 1;
	}
	
	public String getCurrentLine() {
		return song.getSongLines().get(index);
	}

	public String getBlackoutLine() {
		return "    ";
	}

	public String getTitle() {
		return song.getTitle();
	}
}
