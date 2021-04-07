package de.tisan.church.untertitelinator.instancer.packets;

import java.util.List;

import de.tisan.church.untertitelinator.data.Song;

public class SongListChangedPacket extends Packet {

	private static final long serialVersionUID = -7119568264318686397L;

	List<Song> songList;

	public SongListChangedPacket() {
	}

	public SongListChangedPacket(List<Song> songList) {
		super();
		this.songList = songList;
	}

	public List<Song> getSongList() {
		return songList;
	}

	public void setSongList(List<Song> songList) {
		this.songList = songList;
	}

	@Override
	public String toString() {
		return "SongList: " + songList.size();
	}

}
