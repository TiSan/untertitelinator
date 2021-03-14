package de.tisan.church.untertitelinator.churchtools.instancer;

import java.util.ArrayList;
import java.util.List;

import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;

public class CTEventHub {
	private static CTEventHub instance;

	List<CTEventListener> listeners;

	public static CTEventHub get() {
		return instance == null ? instance = new CTEventHub() : instance;
	}

	private CTEventHub() {
		this.listeners = new ArrayList<CTEventListener>();
	}

	public void registerListener(CTEventListener listener) {
		this.listeners.add(listener);
	}

	public void publish(Packet object) {
		CTInstanceServer.get().publish(object);
		listeners.parallelStream().forEach(l -> l.onEventReceived(object));
	}
}
