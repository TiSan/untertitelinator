package de.tisan.church.untertitelinator.churchtools.instancer.client;

import java.io.IOException;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.CTEventListener;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.tisanapi.sockets.ObjectSocket;

public class CTInstanceClient {

	public void connect() {
		ObjectSocket<Packet> socket = new ObjectSocket<Packet>("localhost", 8080);
		socket.addConnectListener(new CTInstanceClientConnectListener<Packet>());
		socket.addReadListener(new CTInstanceClientReadListener<Packet>());
		socket.connect();
		
		CTEventHub.get().registerListener(new CTEventListener() {
			
			@Override
			public void onEventReceived(Packet packet) {
				try {
					socket.writeObject(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
