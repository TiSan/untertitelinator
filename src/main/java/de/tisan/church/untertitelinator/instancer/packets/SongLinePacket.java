package de.tisan.church.untertitelinator.instancer.packets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tisan.church.untertitelinator.data.SongPlayer;

public class SongLinePacket extends Packet {

	private static final long serialVersionUID = -19463254103119350L;
	SongPlayer songPlayer;
	List<String> currentLines;
	List<String> nextLines;

	public SongLinePacket() {
	}

	public SongLinePacket(List<String> currentLines, List<String> nextLines, SongPlayer songPlayer) {
		this.currentLines = currentLines;
		this.nextLines = nextLines;
		this.songPlayer = songPlayer;
	}

	public SongLinePacket(String... currentLines) {
		this.currentLines = Arrays.asList(currentLines);
		this.nextLines = new ArrayList<String>();
		this.songPlayer = null;
	}

	public List<String> getCurrentLines() {
		return currentLines;
	}

	public void setCurrentLines(List<String> currentLines) {
		this.currentLines = currentLines;
	}

	public List<String> getNextLines() {
		return nextLines;
	}

	public void setNextLines(List<String> nextLines) {
		this.nextLines = nextLines;
	}

	public SongPlayer getSongPlayer() {
		return songPlayer;
	}

	public void setSong(SongPlayer songPlayer) {
		this.songPlayer = songPlayer;
	}

	@Override
	public String toString() {
		String songTitle = "";
		if (songPlayer != null && songPlayer.getSong() != null) {
			songTitle = songPlayer.getTitle();
		}
		return "New Song Lines for " + songTitle + ": CurrentLines size = "
				+ (currentLines != null ? currentLines.size() : "-1") + ", NextLines size = "
				+ (nextLines != null ? nextLines.size() : "-1");
	}

}
