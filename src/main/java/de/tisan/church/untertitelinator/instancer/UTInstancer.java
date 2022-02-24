package de.tisan.church.untertitelinator.instancer;

import java.net.UnknownHostException;

import de.tisan.church.untertitelinator.gui.keyer.GUIKeyer;
import de.tisan.church.untertitelinator.gui.main.GUIMain;
import de.tisan.church.untertitelinator.gui.presentator.GUIPresentator;
import de.tisan.church.untertitelinator.instancer.client.UTInstanceClient;
import de.tisan.church.untertitelinator.main.Controller;
import de.tisan.church.untertitelinator.main.Loader;
import de.tisan.tisanapi.logger.Logger;

public class UTInstancer {

	public void startModule(UTInstance instance) {
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
			Logger.getInstance().err("UTInstanceServer couldnt be started! " + e.getMessage(), e, Loader.class);
		}
	}

	private void startClientConnection(UTInstance instance) {
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
}
