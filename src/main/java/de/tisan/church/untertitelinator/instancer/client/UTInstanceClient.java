package de.tisan.church.untertitelinator.instancer.client;

import java.io.IOException;
import java.util.List;

import de.tisan.church.untertitelinator.instancer.UTDiscovery;
import de.tisan.church.untertitelinator.instancer.UTInstanceConnection;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tisanapi.sockets.ObjectSocket;

public class UTInstanceClient
{

	private static UTInstanceClient instance;
	private UTDiscovery discovery;
	private ObjectSocket<Packet> socket;

	public static UTInstanceClient get()
	{
		return instance == null ? instance = new UTInstanceClient() : instance;
	}

	private UTInstanceClient()
	{
		discovery = new UTDiscovery();
	}

	public void connect()
	{
		List<UTInstanceConnection> oServer = discovery.discoverServerIp();
		for (UTInstanceConnection connection : oServer)
		{
			this.socket = new ObjectSocket<Packet>(connection.getIp(), Integer.valueOf(connection.getPort()));
			boolean result = socket.connect();
			if (result == true)
			{
				socket.addConnectListener(new UTInstanceClientConnectListener<Packet>());
				socket.addReadListener(new UTInstanceClientReadListener<Packet>());
				break;
			}
		}
	}

	public void publish(Packet packet)
	{
		if (socket == null)
		{
			return;
		}
		try
		{
			socket.writeObject(packet);
		}
		catch (IOException e)
		{
			Logger.getInstance().err("Publishing Packet (" + packet.toString() + ") to socket is failed! " + e.getMessage(), e, getClass());
			socket.disconnect();
		}
	}
}
