package de.tisan.church.untertitelinator.churchtools.instancer.client;

import java.io.IOException;
import java.util.Optional;

import de.tisan.church.untertitelinator.churchtools.instancer.CTDiscovery;
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
		Optional<String[]> oServer = discovery.discoverServerIp();
		String[] server = oServer.get();
		this.socket = new ObjectSocket<Packet>(server[0], Integer.valueOf(server[1]));
		socket.addConnectListener(new CTInstanceClientConnectListener<Packet>());
		socket.addReadListener(new CTInstanceClientReadListener<Packet>());
		socket.connect();
	}

	public void publish(Packet packet)
	{
		if(socket == null) {
			return;
		}
		try
		{
			socket.writeObject(packet);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
