package de.tisan.church.untertitelinator.churchtools.instancer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tisan.church.untertitelinator.churchtools.instancer.client.CTInstanceClient;
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

	public synchronized void publish(Packet... objects) {
		Arrays.asList(objects).stream().forEach(this::publish);
	}
	
	public synchronized void publish(Packet object) {
		publish(object, true);
	}
	
	public synchronized void publish(Packet object, boolean sendOverSocket) {
		if(sendOverSocket) {
			CTInstanceServer.get().publish(object);
			CTInstanceClient.get().publish(object);
		}
		new ArrayList<CTEventListener>(listeners).stream().forEach(l -> l.onEventReceived(object));
	}
}
