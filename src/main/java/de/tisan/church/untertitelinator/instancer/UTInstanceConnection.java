package de.tisan.church.untertitelinator.instancer;

import de.tisan.church.untertitelinator.instancer.client.UTInstanceClientReadListener;
import de.tisan.church.untertitelinator.instancer.packets.ConnectionStatusPacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.tisanapi.sockets.ObjectSocket;
import de.tisan.tisanapi.sockets.listeners.ObjectSocketReadListener;

public class UTInstanceConnection
{
	

	private ObjectSocket<Packet> socket;
	private UTInstance instanceType;
	private String ip;

	public UTInstanceConnection(ObjectSocket<Packet> socket)
	{
		this.socket = socket;
		this.instanceType = UTInstance.NOT_DEFINED;
		
		socket.addReadListener(new UTInstanceClientReadListener<Packet>());
		socket.addReadListener(new ObjectSocketReadListener<Packet>()
		{
			
			@Override
			public void onReadObject(Packet object)
			{
				if(object instanceof ConnectionStatusPacket) {
					ConnectionStatusPacket packet = (ConnectionStatusPacket) object;
					instanceType = packet.getModule();
				}
			}
			
			@Override
			public void onReadFail(Exception ex)
			{
				
			}
		});
		this.ip = socket.getIP();
		
	}

	public ObjectSocket<Packet> getSocket()
	{
		return socket;
	}

	public UTInstance getInstanceType()
	{
		return instanceType;
	}

	public String getIp()
	{
		return ip;
	}

}
