package de.tisan.church.untertitelinator.main;

import de.tisan.church.untertitelinator.data.Untertitelinator;

public class Main {
	
	
	public static void main(String[] args) {
		System.out.println(" ____ ___       __                 __  .__  __         .__  .__               __                \r\n" + 
				"|    |   \\_____/  |_  ____________/  |_|__|/  |_  ____ |  | |__| ____ _____ _/  |_  ___________ \r\n" + 
				"|    |   /    \\   __\\/ __ \\_  __ \\   __\\  \\   __\\/ __ \\|  | |  |/    \\\\__  \\\\   __\\/  _ \\_  __ \\\r\n" + 
				"|    |  /   |  \\  | \\  ___/|  | \\/|  | |  ||  | \\  ___/|  |_|  |   |  \\/ __ \\|  | (  <_> )  | \\/\r\n" + 
				"|______/|___|  /__|  \\___  >__|   |__| |__||__|  \\___  >____/__|___|  (____  /__|  \\____/|__|   \r\n" + 
				"             \\/          \\/                          \\/             \\/     \\/                   ");
		System.out.println("Loading Untertitelinator v" + Untertitelinator.VERSION);
		Untertitelinator un = new Untertitelinator();
		
		GUIMain main = new GUIMain(un);
		main.setVisible(true);
		
	}
}
