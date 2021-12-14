package de.tisan.church.untertitelinator.instancer.client;

import java.io.IOException;

import de.tisan.tisanapi.logger.Logger;
import de.tisan.tisanapi.sockets.listeners.ObjectSocketConnectListener;

public class UTInstanceClientConnectListener<Packet> extends ObjectSocketConnectListener<Packet>
{

	@Override
	public void onSocketConnect()
	{

	}

	@Override
	public void onSocketConnectFailed(IOException ex)
	{
		Logger.getInstance().err("Connection failed! " + ex.getMessage(), UTInstanceClientConnectListener.class);
//		UTInstanceClient.get().connect();
	}

	@Override
	public void onSocketDisconnect()
	{
		Logger.getInstance().err("Socket disconnected, bereite neue Verbindung vor...",
		        UTInstanceClientConnectListener.class);
		UTInstanceClient.get().connect();
	}

}
