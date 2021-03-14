package de.tisan.church.untertitelinator.churchtools.instancer;

import java.io.IOException;
import java.util.Arrays;

import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.SongLinePacket;
import de.tisan.church.untertitelinator.data.Song;
import de.tisan.tisanapi.sockets.ObjectServerSocket;

public class CTInstanceServer {
	private static CTInstanceServer instance;

	public static CTInstanceServer get() {
		return instance == null ? instance = new CTInstanceServer() : instance;
	}

	private ObjectServerSocket<Packet> socketServer;

	public void startServer() {
		socketServer = new ObjectServerSocket<Packet>(8080);
		socketServer.addConnectListener(new CTInstanceServerListener<Packet>());
		socketServer.start();
	}

	public void sendNewSongLines(String[] currentLines, String[] nextLines, Song song) {
		SongLinePacket packet = new SongLinePacket(Arrays.asList(currentLines), Arrays.asList(nextLines), song);
		sendPacket(packet);
	}

	private void sendPacket(Packet packet) {
		if (socketServer == null) {
			return;
		}
		socketServer.getSockets().parallelStream().forEach(s -> {
			try {
				s.writeObject(packet);
			} catch (IOException e) {
				System.out.println("Write Packet to " + s.getIP() + "@" + s.getPort() + " failed! " + e.getMessage());
				e.printStackTrace();
			}
		});
	}

	public void publish(Packet packet) {
		sendPacket(packet);
	}

}
