package de.tisan.church.untertitelinator.churchtools.instancer.client;

import java.io.IOException;

import de.tisan.tisanapi.logger.Logger;
import de.tisan.tisanapi.sockets.listeners.ObjectSocketConnectListener;

public class CTInstanceClientConnectListener<Packet> extends ObjectSocketConnectListener<Packet>
{

	@Override
	public void onSocketConnect()
	{

	}

	@Override
	public void onSocketConnectFailed(IOException ex)
	{
		Logger.getInstance().err("Connection failed! " + ex.getMessage(), CTInstanceClientConnectListener.class);
		CTInstanceClient.get().connect();
	}

	@Override
	public void onSocketDisconnect()
	{
		Logger.getInstance().err("Socket disconnected, bereite neue Verbindung vor...",
		        CTInstanceClientConnectListener.class);
		CTInstanceClient.get().connect();
	}

}
