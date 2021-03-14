package de.tisan.church.untertitelinator.churchtools.instancer.packets;

import java.util.List;

public class SongLinePacket extends Packet {

	private static final long serialVersionUID = -19463254103119350L;

	List<String> lines;

	public SongLinePacket() {
	}

	public SongLinePacket(List<String> lines) {
		this.lines = lines;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

}
