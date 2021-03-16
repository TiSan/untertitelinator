package de.tisan.church.untertitelinator.churchtools.instancer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tisanapi.sockets.ObjectServerSocket;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class CTInstanceServer {
	private static CTInstanceServer instance;

	public static CTInstanceServer get() {
		return instance == null ? instance = new CTInstanceServer() : instance;
	}

	private ObjectServerSocket<Packet> socketServer;
	private CTDiscovery discovery;

	public void startServer() throws UnknownHostException {
		socketServer = new ObjectServerSocket<Packet>(JSONPersistence.get().getSetting(UTPersistenceConstants.SERVER_PORT, 8080, Integer.class), InetAddress.getByName("0.0.0.0"));
		socketServer.addConnectListener(new CTInstanceServerListener<Packet>());
		socketServer.start();
		discovery = new CTDiscovery();
		discovery.startDiscoveryServer();
	}

	private void sendPacket(Packet packet) {
		if (socketServer == null) {
			return;
		}
		socketServer.getSockets().parallelStream().forEach(s -> {
			try {
				s.writeObject(packet);
			} catch (IOException e) {
				Logger.getInstance().log("Write Packet to " + s.getIP() + "@" + s.getPort() + " failed! " + e.getMessage(), CTInstanceServer.class);
				e.printStackTrace();
			}
		});
	}

	public void publish(Packet packet) {
		sendPacket(packet);
	}

}
