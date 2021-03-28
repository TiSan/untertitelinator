package de.tisan.church.untertitelinator.instancer;

import de.tisan.church.untertitelinator.instancer.packets.Packet;

public interface UTEventListener {
	public void onEventReceived(Packet packet);
}
