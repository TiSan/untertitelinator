package de.tisan.church.untertitelinator.instancer.client;

import java.util.ArrayList;
import java.util.List;

import de.tisan.church.untertitelinator.instancer.UTDiscovery;
import de.tisan.church.untertitelinator.instancer.UTInstanceConnection;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tisanapi.sockets.ObjectSocket;

public class UTInstanceClient {

	private static UTInstanceClient instance;
	private UTDiscovery discovery;
	private ObjectSocket<Packet> socket;
	private List<Packet> queue;
	private List<UTInstanceConnection> oServer;
	private int indexServerList;

	public static UTInstanceClient get() {
		return instance == null ? instance = new UTInstanceClient() : instance;
	}

	private UTInstanceClient() {
		discovery = new UTDiscovery();
		queue = new ArrayList<Packet>();
	}

	public void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (oServer == null) {
					oServer = discovery.discoverServerIp();
				}
				for (int i = indexServerList; i < oServer.size(); i++) {
					UTInstanceConnection connection = oServer.get(i);
					socket = new ObjectSocket<Packet>(connection.getIp(), Integer.valueOf(connection.getPort()));
					boolean result = socket.connect();
					if (result == true) {
						socket.addConnectListener(new UTInstanceClientConnectListener<Packet>());
						socket.addReadListener(new UTInstanceClientReadListener<Packet>());
						break;
					}
					if (i == oServer.size() - 1) {
						indexServerList = 0;
					} else {
						indexServerList = i;
					}
				}
			}

		}).start();
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}

	public void publish(Packet packet) {
		if (socket == null) {
			queue.add(packet);
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (queue.size() > 0) {
						for (Packet queuePacket : queue) {
							socket.writeObject(queuePacket);
						}
						queue.clear();
					}
					socket.writeObject(packet);
				} catch (Exception e) {
					Logger.getInstance().err(
							"Publishing Packet (" + packet.toString() + ") to socket is failed! " + e.getMessage(), e,
							getClass());
					socket.disconnect();
				}

			}
		}).start();
	}
}
