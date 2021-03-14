package de.tisan.church.untertitelinator.churchtools.instancer.packets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tisan.church.untertitelinator.data.Song;

public class SongLinePacket extends Packet {

	private static final long serialVersionUID = -19463254103119350L;
	Song song;
	List<String> currentLines;
	List<String> nextLines;

	public SongLinePacket() {
	}

	public SongLinePacket(List<String> currentLines, List<String> nextLines, Song song) {
		this.currentLines = currentLines;
		this.nextLines = nextLines;
		this.song = song;
	}
	
	public SongLinePacket(String... currentLines) {
		this.currentLines = Arrays.asList(currentLines);
		this.nextLines = new ArrayList<String>();
		this.song = null;
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

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

}
