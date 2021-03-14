package de.tisan.church.untertitelinator.churchtools.instancer.packets;

import java.util.List;

public class SongLinePacket extends Packet {

	private static final long serialVersionUID = -19463254103119350L;

	List<String> currentLines;
	List<String> nextLines;

	public SongLinePacket() {
	}

	public SongLinePacket(List<String> currentLines, List<String> nextLines) {
		this.currentLines = currentLines;
		this.nextLines = nextLines;
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

}
