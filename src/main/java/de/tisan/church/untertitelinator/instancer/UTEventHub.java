package de.tisan.church.untertitelinator.instancer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tisan.church.untertitelinator.instancer.client.UTInstanceClient;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.tisanapi.logger.Logger;

public class UTEventHub {
	private static UTEventHub instance;

	List<UTEventListener> listeners;

	public static UTEventHub get() {
		return instance == null ? instance = new UTEventHub() : instance;
	}

	private UTEventHub() {
		this.listeners = new ArrayList<UTEventListener>();
	}

	public synchronized void registerListener(UTEventListener listener) {
		this.listeners.add(listener);
	}
	
	public synchronized void unregisterListener(UTEventListener listener) {
		this.listeners.remove(listener);
	}

	public synchronized void publish(Packet... objects) {
		Arrays.asList(objects).stream().forEach(this::publish);
	}
	
	public synchronized void publish(Packet object) {
		publish(object, true);
	}
	
	public synchronized void publish(Packet object, boolean sendOverSocket) {
		Logger.getInstance().log("Publish Packet '" + object.toString() + "' (" + listeners.size() + " local listeners)", getClass());
		if(sendOverSocket) {
			UTInstanceServer.get().publish(object);
			UTInstanceClient.get().publish(object);
		}
		new ArrayList<UTEventListener>(listeners).stream().forEach(l -> l.onEventReceived(object));
	}

}
