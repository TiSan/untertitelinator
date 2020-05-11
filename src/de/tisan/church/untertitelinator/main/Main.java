package de.tisan.church.untertitelinator.main;

import de.tisan.church.untertitelinator.data.Untertitelinator;

public class Main {
	
	
	public static void main(String[] args) {
		System.out.println("hi");
		Untertitelinator un = new Untertitelinator();
		
		GUIMain main = new GUIMain(un);
		main.setVisible(true);
	}
}
