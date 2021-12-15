package de.tisan.church.untertitelinator.instancer.client;

import java.io.IOException;

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTInstance;
import de.tisan.church.untertitelinator.instancer.packets.ConnectionStatusPacket;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tisanapi.sockets.listeners.ObjectSocketConnectListener;

public class UTInstanceClientConnectListener<Packet> extends ObjectSocketConnectListener<Packet>
{
	UTInstance instance;

	public UTInstanceClientConnectListener(UTInstance instance){
		this.instance = instance;

	}

	@Override
	public void onSocketConnect()
	{
		UTEventHub.get().publish(new ConnectionStatusPacket(ConnectionStatusPacket.ConnectionType.CLIENT, instance, true));
	}

	@Override
	public void onSocketConnectFailed(Exception ex)
	{
		Logger.getInstance().err("Connection failed! " + ex.getMessage(), UTInstanceClientConnectListener.class);
//		UTInstanceClient.get().connect();
	}

	@Override
	public void onSocketDisconnect()
	{
		Logger.getInstance().err("Socket disconnected, bereite neue Verbindung vor...",
		        UTInstanceClientConnectListener.class);
		UTEventHub.get().publish(new ConnectionStatusPacket(ConnectionStatusPacket.ConnectionType.CLIENT, instance, false), false);
	}

}
