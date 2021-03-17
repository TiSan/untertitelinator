package de.tisan.church.untertitelinator.churchtools.instancer.client;

import java.io.IOException;
import java.util.List;

import de.tisan.church.untertitelinator.churchtools.instancer.CTDiscovery;
import de.tisan.church.untertitelinator.churchtools.instancer.CTInstanceConnection;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.tisanapi.sockets.ObjectSocket;

public class CTInstanceClient
{

	private static CTInstanceClient instance;
	private CTDiscovery discovery;
	private ObjectSocket<Packet> socket;

	public static CTInstanceClient get()
	{
		return instance == null ? instance = new CTInstanceClient() : instance;
	}

	private CTInstanceClient()
	{
		discovery = new CTDiscovery();
	}

	public void connect()
	{
		List<CTInstanceConnection> oServer = discovery.discoverServerIp();
		for (CTInstanceConnection connection : oServer)
		{
			this.socket = new ObjectSocket<Packet>(connection.getIp(), Integer.valueOf(connection.getPort()));
			boolean result = socket.connect();
			if (result == true)
			{
				socket.addConnectListener(new CTInstanceClientConnectListener<Packet>());
				socket.addReadListener(new CTInstanceClientReadListener<Packet>());
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
			e.printStackTrace();
			socket.disconnect();
		}
	}
}
