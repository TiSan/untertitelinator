package de.tisan.church.untertitelinator.churchtools.instancer.client;

import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.tisanapi.sockets.ObjectSocket;

public class CTInstanceClient {
	public void connect() {
		ObjectSocket<Packet> socket = new ObjectSocket<Packet>("localhost", 8080);
		socket.addConnectListener(new CTInstanceClientConnectListener<Packet>());
		
	}
}
