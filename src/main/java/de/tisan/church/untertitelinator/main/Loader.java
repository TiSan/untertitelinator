package de.tisan.church.untertitelinator.main;

import de.tisan.church.untertitelinator.data.Untertitelinator;

public class Loader {
	private static GUIMain main;
	private static GUIPresentator beamerUi;
	private static GUIKeyer keyerUi;
	private static GUISelectGodi selectionUi;
	private static boolean selectionTaken;

	public static void loadUi() {
		main = new GUIMain();
		beamerUi = GUIPresentator.get();
		keyerUi = GUIKeyer.get();
		selectionUi = new GUISelectGodi();
	}

	public static void loadUntertitelinatorSongs() {
		Untertitelinator.get().loadSongs();
	}

	public static void loadSongsIntoMainGui() {
		main.loadSongs();
	}

	public static void showUi() {
		main.setVisible(true);
		beamerUi.setVisible(true);
		keyerUi.setVisible(true);
	}

	public static void loadChurchtoolsEvents() {
		Untertitelinator.get().loadEvents();
	}

	public static void loadKeyerUi() {
		keyerUi.loadUí();
	}

	public static void loadSelectionMenu() {
		selectionUi.loadUi();
	}

	public static void showSelectionMenu() {
		
		selectionUi.setVisible(true);
	}

	public static boolean isReadyToContinue() {
		return selectionTaken;
	}

	public static void selectionTaken() {
		selectionTaken = true;
	}
}
