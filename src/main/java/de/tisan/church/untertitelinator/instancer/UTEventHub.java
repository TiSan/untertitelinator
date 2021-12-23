package de.tisan.church.untertitelinator.instancer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import de.tisan.church.untertitelinator.instancer.client.UTInstanceClient;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.tisanapi.logger.Logger;

public class UTEventHub {
	private static UTEventHub instance = new UTEventHub();

	List<UTEventListener> listeners;

	public static UTEventHub get() {
		return instance;
	}

	private UTEventHub() {
		this.listeners = new ArrayList<UTEventListener>();
	}

	public void registerListener(UTEventListener listener) {
		this.listeners.add(listener);
	}

	public void unregisterListener(UTEventListener listener) {
		this.listeners.remove(listener);
	}

	public void publish(Packet... objects) {
		Arrays.asList(objects).stream().forEach(this::publish);
	}

	public void publish(Packet object) {
		publish(object, true);
	}

	public void publish(Packet object, boolean sendOverSocket) {
		//Logger.getInstance().log(
		//		"Publish Packet '" + object.toString() + "' (" + listeners.size() + " local listeners)", getClass());
		if (sendOverSocket) {
			if (UTInstanceServer.get().isStarted()) {
				UTInstanceServer.get().publish(object);
			}
			if (UTInstanceClient.get().isConnected()) {
				UTInstanceClient.get().publish(object);
			}
		}

		List<UTEventListener> clone = new ArrayList<UTEventListener>(listeners);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (UTEventListener listener : clone) {
					listener.onEventReceived(object);
				}
			}
		});
	}

}
