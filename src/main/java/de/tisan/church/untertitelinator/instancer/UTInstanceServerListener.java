package de.tisan.church.untertitelinator.instancer;

import java.io.IOException;

import de.tisan.tisanapi.sockets.ObjectSocket;
import de.tisan.tisanapi.sockets.listeners.ObjectServerSocketConnectListener;

public class UTInstanceServerListener<Packet> extends ObjectServerSocketConnectListener<Packet> {

	@Override
	public void onConnect(ObjectSocket<Packet> socket) {

	}

	@Override
	public void onDisconnect(ObjectSocket<Packet> socket) {

	}

	@Override
	public void onServerSocketClosed() {

	}

	@Override
	public void onBindFailed(IOException ex) {

	}

}