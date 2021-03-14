package de.tisan.church.untertitelinator.churchtools.instancer.client;

import java.io.IOException;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.SongLinePacket;
import de.tisan.tisanapi.sockets.listeners.ObjectSocketReadListener;

public class CTInstanceClientReadListener<T> extends ObjectSocketReadListener<Packet> {

	@Override
	public void onReadObject(Packet object) {
		System.out.println("Received " + object.getClass());
		
		CTEventHub.get().publish(object);
		
		if (object instanceof SongLinePacket) {
			SongLinePacket sPacket = (SongLinePacket) object;

			System.out.println(sPacket.getCurrentLines());
			System.out.println(sPacket.getNextLines());
		}
	}

	@Override
	public void onReadFail(IOException ex) {

	}

}
