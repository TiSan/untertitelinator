package de.tisan.church.untertitelinator.instancer;

import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.tisanapi.sockets.ObjectSocket;

public class UTInstance {
	private ObjectSocket<Packet> socket;

	private UTInstanceType instanceType;

	public UTInstance(ObjectSocket<Packet> socket, UTInstanceType instanceType) {
		super();
		this.socket = socket;
		this.instanceType = instanceType;
	}

	public ObjectSocket<Packet> getSocket() {
		return socket;
	}

	public UTInstanceType getInstanceType() {
		return instanceType;
	}

}
