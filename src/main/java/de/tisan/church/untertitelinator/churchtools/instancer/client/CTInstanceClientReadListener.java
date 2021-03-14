package de.tisan.church.untertitelinator.churchtools.instancer.client;

import java.io.IOException;

import de.tisan.tisanapi.sockets.listeners.ObjectSocketReadListener;

public class CTInstanceClientReadListener<Packet> extends ObjectSocketReadListener<Packet> {

	@Override
	public void onReadObject(Packet object) {

	}

	@Override
	public void onReadFail(IOException ex) {

	}

}
