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

	public synchronized void registerListener(CTEventListener listener) {
		this.listeners.add(listener);
	}

	public synchronized void publish(Packet object) {
		CTInstanceServer.get().publish(object);
		new ArrayList<CTEventListener>(listeners).stream().forEach(l -> l.onEventReceived(object));
	}
}
