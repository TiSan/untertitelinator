package de.tisan.church.untertitelinator.instancer.client;

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.tisanapi.sockets.listeners.ObjectSocketReadListener;

public class UTInstanceClientReadListener<T> extends ObjectSocketReadListener<Packet> {

	@Override
	public void onReadObject(Packet object) {
		UTEventHub.get().publish(object, false);
	}

	@Override
	public void onReadFail(Exception ex) {
		
	}

}
