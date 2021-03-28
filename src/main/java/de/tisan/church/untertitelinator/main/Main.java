package de.tisan.church.untertitelinator.main;

import javax.swing.JOptionPane;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.gui.main.GUILoad;
import de.tisan.church.untertitelinator.instancer.client.UTInstanceClient;
import de.tisan.tisanapi.logger.Logger;

public class Main {

	public static void main(String[] args) {
		System.out.println(
				" ____ ___       __                 __  .__  __         .__  .__               __                \r\n"
						+ "|    |   \\_____/  |_  ____________/  |_|__|/  |_  ____ |  | |__| ____ _____ _/  |_  ___________ \r\n"
						+ "|    |   /    \\   __\\/ __ \\_  __ \\   __\\  \\   __\\/ __ \\|  | |  |/    \\\\__  \\\\   __\\/  _ \\_  __ \\\r\n"
						+ "|    |  /   |  \\  | \\  ___/|  | \\/|  | |  ||  | \\  ___/|  |_|  |   |  \\/ __ \\|  | (  <_> )  | \\/\r\n"
						+ "|______/|___|  /__|  \\___  >__|   |__| |__||__|  \\___  >____/__|___|  (____  /__|  \\____/|__|   \r\n"
						+ "             \\/          \\/                          \\/             \\/     \\/                   ");
		Logger.getInstance().log("Loading Untertitelinator v" + Untertitelinator.VERSION, Main.class);
		for(int i = 0; i < args.length; i++) {
			if(args[i].contains("debug")) {
				debugMode();
				return;
			}
		}
		GUILoad load = new GUILoad();
		load.setVisible(true);
	}
	
	private static void debugMode() {
		
		int result = JOptionPane.showOptionDialog(null,
				"Möchten Sie den Standalone-Modus starten oder betreiben Sie ein verteiltes UT-Netzwerk?",
				"Abfrage zur Laufart", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				new String[] { "Standalone", "Verteiles Netzwerk" }, "Standalone");
		if (result == 0) {
			GUILoad load = new GUILoad();
			load.setVisible(true);
			
		} else if (result == 1) {
			UTInstanceClient.get().connect();
		} else {
			System.exit(1);
		}
	}
}
