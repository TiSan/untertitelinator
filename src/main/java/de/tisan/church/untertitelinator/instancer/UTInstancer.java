package de.tisan.church.untertitelinator.instancer;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import de.tisan.church.untertitelinator.gui.keyer.GUIKeyer;
import de.tisan.church.untertitelinator.gui.main.GUIMain;
import de.tisan.church.untertitelinator.gui.presentator.GUIPresentator;
import de.tisan.church.untertitelinator.instancer.client.UTInstanceClient;
import de.tisan.church.untertitelinator.main.Controller;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tisanapi.sockets.ObjectSocket;

public class UTInstancer {

	private List<UTInstance> connectedInstances;
	private UTInstanceType ownInstanceType;

	private static UTInstancer instancer;

	public static UTInstancer get() {
		return (instancer == null ? (instancer = new UTInstancer()) : instancer);
	}

	private UTInstancer() {
		connectedInstances = new ArrayList<UTInstance>();
	}

	public void addInstance(UTInstance instance) {
		connectedInstances.add(instance);
	}

	public void removeInstance(UTInstance instance) {
		connectedInstances.remove(instance);
	}

	public List<UTInstance> getInstances() {
		return connectedInstances;
	}

	public UTInstanceType getOwnInstanceType() {
		return ownInstanceType;
	}

	public void startModule(UTInstanceType instance) {
		ownInstanceType = instance;
		switch (instance) {
		case CONTROLLER:
			startServerConnection();
			startController();
			break;
		case KEYER:
			startKeyer();
			startClientConnection(instance);
			break;
		case PRESENTATOR:
			startPresentator();
			startClientConnection(instance);
			break;
		default:
			break;
		}

	}

	private void startServerConnection() {
		try {
			UTInstanceServer.get().startServer();
		} catch (UnknownHostException e) {
			Logger.getInstance().err("UTInstanceServer couldnt be started! " + e.getMessage(), e, UTInstancer.class);
		}
	}

	private void startClientConnection(UTInstanceType instance) {
		UTInstanceClient.get().connect(instance);
	}

	private void startPresentator() {
		GUIPresentator.get().setVisible(true);
	}

	private void startKeyer() {
		GUIKeyer.get().setVisible(true);
	}

	private void startController() {
		GUIMain main = new GUIMain();
		main.setVisible(true);
		Controller controller = new Controller();
		controller.init();
	}

	public UTInstance getInstanceForSocket(ObjectSocket<?> socket) {
		return connectedInstances.stream().filter(i -> i.getSocket().equals(socket)).findFirst().orElse(null);
	}
}
