package de.tisan.church.untertitelinator.churchtools.instancer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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

	public List<CTInstanceConnection> discoverServerIp()
	{
		List<CTInstanceConnection> connections = new ArrayList<CTInstanceConnection>();
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
			String[] spl = data.split("\\|");
			for (String s : spl)
			{
				CTInstanceConnection connection = new CTInstanceConnection();
				String[] spl2 = s.split("_");
				boolean completeStatement = true;
				if (spl2[0].trim().startsWith("IP="))
				{
					connection.setIp(spl2[0].split("=")[1].trim());
				}
				else
				{
					completeStatement = false;
				}

				if (spl2[1].trim().startsWith("PORT="))
				{
					connection.setPort(spl2[1].split("=")[1].trim());
				}
				else
				{
					completeStatement = false;
				}
				
				if (completeStatement == true)
				{
					connections.add(connection);
				}
			}
			System.out.println(
			        "Discovery package received! -> " + packet.getAddress() + ":" + packet.getPort() + " : " + data);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return connections;
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
		String data = command;
		String port = "" + JSONPersistence.get().getSetting(UTPersistenceConstants.SERVER_PORT, 8080, Integer.class);

		List<String> ipAddresses = getIpList();
		for (String ip : ipAddresses)
		{
			data += "|IP=" + ip + "_PORT=" + port;
		}
		byte[] buf = data.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("255.255.255.255"), 8002);
		sendSocket.send(packet);
		//System.out.println("Discovery package sent!" + packet.getAddress() + ":" + packet.getPort());
	}

	List<String> getIpList()
	{
		List<String> ipList = new ArrayList<String>();
		try
		{
			Enumeration<NetworkInterface> interfaces;
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements())
			{
				NetworkInterface interf = interfaces.nextElement();
				Enumeration<InetAddress> addresses = interf.getInetAddresses();
				while (addresses.hasMoreElements())
				{
					InetAddress adr = addresses.nextElement();

					// Nur(!) IPv4 Adressen ausgeben...
					if (adr instanceof Inet4Address)
					{
						ipList.add(adr.getHostAddress());
					}
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}

		return ipList;
	}
}
