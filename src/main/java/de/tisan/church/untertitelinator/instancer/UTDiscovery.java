package de.tisan.church.untertitelinator.instancer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class UTDiscovery {
	private DatagramSocket sendSocket;
	private DatagramSocket receiveSocket;
	private String command;
	private Integer startPort;
	private Integer endPort;

	public UTDiscovery() {
		command = "_UNTERTITELINATOR_DISCOVERY_";
		startPort = JSONPersistence.get().getSetting(UTPersistenceConstants.UDP_START_PORT, Integer.valueOf(8020),
				Integer.class);
		endPort = JSONPersistence.get().getSetting(UTPersistenceConstants.UDP_END_PORT, Integer.valueOf(8050),
				Integer.class);

	}

	public List<UTDiscoveryResult> discoverServerIp() {
		List<UTDiscoveryResult> connections = new ArrayList<UTDiscoveryResult>();
		try {
			if (receiveSocket == null) {
				Optional<DatagramSocket> oSocket = getNewDatagramSocket();
				if (oSocket.isPresent()) {
					receiveSocket = getNewDatagramSocket().get();
				} else {
					return connections;
				}
			}
			int size = 4096;
			byte[] buf = new byte[size];
			DatagramPacket packet = new DatagramPacket(buf, size);
			receiveSocket.receive(packet);
			String data = new String(packet.getData());
			String[] spl = data.split("\\|");
			for (String s : spl) {
				UTDiscoveryResult connection = new UTDiscoveryResult();
				String[] spl2 = s.split("_");
				boolean completeStatement = true;
				if (spl2[0].trim().startsWith("IP=")) {
					connection.setIp(spl2[0].split("=")[1].trim());
				} else {
					completeStatement = false;
				}

				if (spl2[1].trim().startsWith("PORT=")) {
					connection.setPort(spl2[1].split("=")[1].trim());
				} else {
					completeStatement = false;
				}

				if (completeStatement == true) {
					connections.add(connection);
				}
			}
			Logger.getInstance().log(
					"Discovery package received! -> " + packet.getAddress() + ":" + packet.getPort() + " : " + data,
					getClass());
		} catch (Exception ex) {
			Logger.getInstance().err("UDP Listener Error! " + ex.getMessage(), ex, getClass());
		}
		return connections;
	}

	private Optional<DatagramSocket> getNewDatagramSocket() {
		for (int i = startPort.intValue(); i < endPort.intValue(); i++) {
			try {
				DatagramSocket s = new DatagramSocket(i, InetAddress.getByName("0.0.0.0")); // 0.0.0.0 for listen to all
				s.setTrafficClass(0x10); // High prio in unifi network
				s.setBroadcast(true);
				return Optional.ofNullable(s);
			} catch (Exception e) {
				Logger.getInstance().err("Port " + i + " scheint belegt zu sein. Nehme " + (i + 1) + " ...",
						getClass());
			}
		}
		Logger.getInstance().err("Kein freier Port vorhanden!", getClass());
		return Optional.empty();
	}

	public void startDiscoveryServer() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				int attempt = 0;
				while (true) {
					try {
						sendDiscoveryPacket();
						Thread.sleep(1000);
					} catch (Exception e) {
						Logger.getInstance().err("SendDiscoveryPacket failed! " + e.getMessage(), e, getClass());
					}
					attempt++;
					if(attempt == 10) {
						Logger.getInstance().err("10 tries for sending a discovery packet failed. Please view the logs and try again with restarting this app.", UTDiscovery.class);
						return;
					}
				}
			}
		}).start();
	}

	private void sendDiscoveryPacket() throws IOException {
		if (sendSocket == null) {
			sendSocket = new DatagramSocket(8001, InetAddress.getLocalHost());
			sendSocket.setBroadcast(true);
		}
		String data = command;
		String port = "" + JSONPersistence.get().getSetting(UTPersistenceConstants.SERVER_PORT, 8080, Integer.class);

		List<String> ipAddresses = getIpList();
		for (String ip : ipAddresses) {
			data += "|IP=" + ip + "_PORT=" + port;
		}
		byte[] buf = data.getBytes();
		for (int i = startPort.intValue(); i < endPort.intValue(); i++) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("255.255.255.255"), i);
			sendSocket.send(packet);
		}
	}

	List<String> getIpList() {
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> interfaces;
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface interf = interfaces.nextElement();
				Enumeration<InetAddress> addresses = interf.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress adr = addresses.nextElement();

					// Nur(!) IPv4 Adressen ausgeben...
					if (adr instanceof Inet4Address) {
						ipList.add(adr.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			Logger.getInstance().err("IP Addresses from current computer couldnt be fetched! " + e.getMessage(), e,
					getClass());

		}

		return ipList;
	}
}
