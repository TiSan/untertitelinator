package de.tisan.church.untertitelinator.main;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.gui.keyer.GUIKeyer;
import de.tisan.church.untertitelinator.gui.main.GUIMain;
import de.tisan.church.untertitelinator.gui.main.GUISelectGodi;
import de.tisan.church.untertitelinator.gui.presentator.GUIPresentator;

public class Loader
{
	private static GUIMain mainUi;
	private static GUIPresentator beamerUi;
	private static GUIKeyer keyerUi;
	private static GUISelectGodi selectionUi;
	private static boolean selectionTaken;

	public static void loadUi()
	{
		mainUi = new GUIMain();
		beamerUi = GUIPresentator.get();
		keyerUi = GUIKeyer.get();
		selectionUi = new GUISelectGodi();
	}

	public static void loadUntertitelinatorSongs()
	{
		Untertitelinator.get().loadSongs();
	}

	public static void loadSongsIntoMainGui()
	{
	}

	public static void showUi()
	{
		mainUi.setVisible(true);
		beamerUi.setVisible(true);
		keyerUi.setVisible(true);
	}

	public static void loadChurchtoolsEvents()
	{
		Untertitelinator.get().loadEvents();
	}

	public static void loadKeyerUi()
	{
		keyerUi.loadUi();
	}

	public static void loadSelectionMenu()
	{
		selectionUi.loadUi();
	}

	public static void showSelectionMenu()
	{

		selectionUi.setVisible(true);
	}

	public static boolean isReadyToContinue()
	{
		return selectionTaken;
	}

	public static void selectionTaken()
	{
		selectionTaken = true;
	}

	public static GUIMain getMainUi()
	{
		return mainUi;
	}

	public static GUIPresentator getBeamerUi()
	{
		return beamerUi;
	}

	public static GUIKeyer getKeyerUi()
	{
		return keyerUi;
	}

	public static GUISelectGodi getSelectionUi()
	{
		return selectionUi;
	}

}
