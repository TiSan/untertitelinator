package de.tisan.church.untertitelinator.churchtools.instancer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Optional;

import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class CTDiscovery
{
	private DatagramSocket sendSocket;
	private DatagramSocket receiveSocket;
	private String command;

	public CTDiscovery()
	{
		command = "_UNTERTITELINATOR_DISCOVERY_";
	}

	public Optional<String[]> discoverServerIp()
	{
		try
		{
			if (receiveSocket == null)
			{
				receiveSocket = new DatagramSocket(8002, InetAddress.getByName("0.0.0.0")); // 0.0.0.0 for listen to all ips
				receiveSocket.setTrafficClass(0x10); // High prio in unifi network
				receiveSocket.setBroadcast(true);
			}
			int size = 4096;
			byte[] buf = new byte[size];
			DatagramPacket packet = new DatagramPacket(buf, size);
			receiveSocket.receive(packet);
			String data = new String(packet.getData());
			String[] spl = data.split("_");
			String[] returnValue = new String[2];
			for (String s : spl)
			{
				if (s.startsWith("IP="))
				{
					returnValue[0] = s.split("=")[1].trim();
				}
				else
					if (s.startsWith("PORT="))
					{
						returnValue[1] = s.split("=")[1].trim();
					}
			}
			System.out.println(
			        "Discovery package received! -> " + packet.getAddress() + ":" + packet.getPort() + " : " + data);
			return Optional.ofNullable(returnValue);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return Optional.empty();
	}

	public void startDiscoveryServer()
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						sendDiscoveryPacket();
						Thread.sleep(1000);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void sendDiscoveryPacket() throws IOException
	{
		if (sendSocket == null)
		{
			sendSocket = new DatagramSocket(8001, InetAddress.getLocalHost());
			sendSocket.setBroadcast(true);
		}
		String ip = InetAddress.getLocalHost().getHostAddress();
		String port = "" + JSONPersistence.get().getSetting(UTPersistenceConstants.SERVER_PORT, 8080, Integer.class);
		String data = command + "IP=" + ip + "_PORT=" + port;
		byte[] buf = data.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("255.255.255.255"), 8002);
		sendSocket.send(packet);
		//System.out.println("Discovery package sent!" + packet.getAddress() + ":" + packet.getPort());
	}
}
