package de.tisan.church.untertitelinator.instancer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.tisan.church.untertitelinator.instancer.packets.KeepAlivePacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tisanapi.sockets.ObjectServerSocket;
import de.tisan.tisanapi.sockets.ObjectSocket;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class UTInstanceServer {
	private static UTInstanceServer instance;

	public static UTInstanceServer get() {
		return instance == null ? instance = new UTInstanceServer() : instance;
	}

	private ObjectServerSocket<Packet> socketServer;
	private UTDiscovery discovery;
	private boolean started;
	private List<UTInstanceConnection> connections;
	

	public void startServer() throws UnknownHostException {
		this.connections = new ArrayList<>();
		socketServer = new ObjectServerSocket<Packet>(
				JSONPersistence.get().getSetting(UTPersistenceConstants.SERVER_PORT, 8080, Integer.class),
				InetAddress.getByName("0.0.0.0"));
		socketServer.addConnectListener(new UTInstanceServerListener<Packet>());

		socketServer.start();
		discovery = new UTDiscovery();
		discovery.startDiscoveryServer();
		started = true;
		startKeepAlivePacketThread();
	}

	public boolean isStarted() {
		return started;
	}

	private synchronized void sendPacket(Packet packet, UTInstance... toInstances) {
		if (socketServer == null) {
			return;
		}
		List<UTInstance> instances = Arrays.asList(toInstances);
		
		connections = connections.parallelStream()
				.filter(c -> instances.contains(c.getInstanceType()))
				.filter(s -> {
					try {
						s.getSocket().writeObject(packet);
						return true;
					} catch (IOException e) {
						Logger.getInstance().err(
								"Write Packet to " + s.getIp() + "@" + s.getSocket().getPort() + " failed! " + e.getMessage(), e,
								UTInstanceServer.class);
						socketServer.remove(s.getSocket());
						return false;
					}
				})
				.collect(Collectors.toList());
		
	}

	public void publish(Packet packet) {
		sendPacket(packet, UTInstance.CONTROLLER, UTInstance.KEYER, UTInstance.PRESENTATOR);
	}
	public void publish(Packet packet, UTInstance...instances) {
		sendPacket(packet, instances);
	}

	private void startKeepAlivePacketThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(2000);
						sendPacket(new KeepAlivePacket(UTInstance.CONTROLLER));
					} catch (Exception e) {
					}
				}
			}
		}).start();
	}

	public void registerNewClient(ObjectSocket<Packet> socket)
	{
		UTInstanceConnection connection = new UTInstanceConnection(socket);
		connections.add(connection);
	}

	public List<UTInstanceConnection> getConnections()
	{
		return connections;
	}

}
