package de.tisan.church.untertitelinator.instancer.client;

import java.util.ArrayList;
import java.util.List;

import de.tisan.church.untertitelinator.instancer.UTDiscovery;
import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTInstance;
import de.tisan.church.untertitelinator.instancer.UTInstanceConnection;
import de.tisan.church.untertitelinator.instancer.packets.ConnectionStatusPacket;
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
	private UTInstance instanceType;

	public static UTInstanceClient get() {
		return instance == null ? instance = new UTInstanceClient() : instance;
	}

	private UTInstanceClient() {
		discovery = new UTDiscovery();
		queue = new ArrayList<Packet>();
	}

	public void connect(UTInstance instance) {
		this.instanceType = instance;
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (oServer == null) {
					oServer = discovery.discoverServerIp();
				}
				for (int i = indexServerList; i < oServer.size(); i++) {
					UTInstanceConnection connection = oServer.get(i);
					socket = new ObjectSocket<Packet>(connection.getIp(), Integer.valueOf(connection.getPort()), true);
					boolean result = socket.connect();
					if (result == true) {
						UTEventHub.get().publish(new ConnectionStatusPacket(ConnectionStatusPacket.ConnectionType.CLIENT, instanceType, true));
						socket.addConnectListener(new UTInstanceClientConnectListener<Packet>(instanceType));
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
		if (socket == null || socket.isConnected() == false) {
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
				}

			}
		}).start();
	}
}
