package de.tisan.church.untertitelinator.instancer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tisanapi.sockets.ObjectServerSocket;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class UTInstanceServer {
	private static UTInstanceServer instance;

	public static UTInstanceServer get() {
		return instance == null ? instance = new UTInstanceServer() : instance;
	}

	private ObjectServerSocket<Packet> socketServer;
	private UTDiscovery discovery;
	private boolean started;

	public void startServer() throws UnknownHostException {
		socketServer = new ObjectServerSocket<Packet>(
				JSONPersistence.get().getSetting(UTPersistenceConstants.SERVER_PORT, 8080, Integer.class),
				InetAddress.getByName("0.0.0.0"));
		socketServer.addConnectListener(new UTInstanceServerListener<Packet>());

		socketServer.start();
		discovery = new UTDiscovery();
		discovery.startDiscoveryServer();
		started = true;
	}

	public boolean isStarted() {
		return started;
	}

	private void sendPacket(Packet packet) {
		if (socketServer == null) {
			return;
		}
		socketServer.getSockets().parallelStream().forEach(s -> {
			try {
				s.writeObject(packet);
			} catch (IOException e) {
				Logger.getInstance().err(
						"Write Packet to " + s.getIP() + "@" + s.getPort() + " failed! " + e.getMessage(), e,
						UTInstanceServer.class);
				s.disconnect();
			}
		});
	}

	public void publish(Packet packet) {
		sendPacket(packet);
	}

}
