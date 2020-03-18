package de.tisan.church.untertitelinator.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SongPlayer {
	Song song;
	int index;
	boolean pause;
	File fileCurrent;

	public SongPlayer(Song song) {
		this.song = song;
		this.fileCurrent = new File("currentLineForObs.utline");
		if (fileCurrent.exists() == false) {
			try {
				fileCurrent.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		setIndex(0);
	}

	private void setIndex(int index) {
		this.index = index;
		if (isValidIndex()) {
			writeCurrentLineToFile();
		}
	}

	private boolean isValidIndex() {
		return index >= 0 && index <= song.getSongLines().size() - 1;
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

	public String getBlackoutLine() {
		return "    ";
	}

	public String getTitle() {
		return song.getTitle();
	}

	public void pause() {
		pause = !pause;
	}

	private void writeCurrentLineToFile() {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(fileCurrent, false));
			w.write(getCurrentLine());
			w.flush();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
