package de.tisan.church.untertitelinator.instancer.packets;

public class KeepAlivePacket extends Packet {

	private static final long serialVersionUID = 5091431982503004621L;
	private long ts;

	public KeepAlivePacket() {
		ts = System.currentTimeMillis();
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "KeepAlivePacket";
	}

}
