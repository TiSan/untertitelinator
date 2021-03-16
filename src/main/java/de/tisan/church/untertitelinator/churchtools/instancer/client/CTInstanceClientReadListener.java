package de.tisan.church.untertitelinator.churchtools.instancer.client;

import java.io.IOException;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tisanapi.sockets.listeners.ObjectSocketReadListener;

public class CTInstanceClientReadListener<T> extends ObjectSocketReadListener<Packet> {

	@Override
	public void onReadObject(Packet object) {
		Logger.getInstance().log("Received " + object.getClass(), CTInstanceClientReadListener.class);
		
		CTEventHub.get().publish(object, false);
	}

	@Override
	public void onReadFail(IOException ex) {
		
	}

}
