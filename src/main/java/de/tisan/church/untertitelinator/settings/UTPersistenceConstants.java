package de.tisan.church.untertitelinator.settings;

import de.tisan.tools.persistencemanager.PersistenceConstants;

public enum UTPersistenceConstants implements PersistenceConstants{
	GUIPRESENTATORDELAY("guiPresentatorDelay"), GUIPRESENTATORWIDTH("guiPresentatorWidth"),
	GUIPRESENTATORHEIGHT("guiPresentatorHeight"), GUIPRESENTATORX("guiPresentatorX"),
	GUIPRESENTATORY("guiPresentatorY"), GUIKEYERWIDTH("guiKeyerWidth"), GUIKEYERHEIGHT("guiKeyerHeight"),
	GUIKEYERX("guiKeyerX"), GUIKEYERY("guiKeyerY"), LINESEPARATOR("lineSeparator"),
	CURRENTLINEFILEPATH("currentLineFilePath"), NEXTLINEFILEPATH("nextLineFilePath"),
	BLACKOUTLINEFILLER("blackoutLineFiller"), SONGSFOLDERPATH("songsFolderPath"), SONGFILESUFFIX("songFileSuffix"),
	CHURCHNAME("churchName"), GUIKEYERBACKGROUND("guiKeyerBackground"),
	GUIKEYERSECONELINEENABLED("guiKeyerSecondLineEnabled"), GUIKEYERMOVEABLE("guiKeyerMoveable"),
	GUIKEYERMINIMIZABLE("guiKeyerMinimizable"), GUIKEYERMAXIMIZABLE("guiKeyerMaximizable"),
	GUIKEYERCLOSEABLE("guiKeyerCloseable"), GUIPRESENTATORCLOSEABLE("guiPresentatorCloseable"),
	GUIPRESENTATORMAXIMIZABLE("guiPresentatorMaximizable"), GUIPRESENTATORMINIMIZABLE("guiPresentatorMinimizable"),
	GUIPRESENTATORMOVEABLE("guiPresentatorMoveable"), GUIPRESENTATORCURRENTTITLETEXT("guiPresentatorCurrentTitleText"),
	CHURCHTOOLSUSER("churchToolsUser"), CHURCHTOOLSPASSWORD("churchToolsPassword"), CHURCHTOOLSBASEURL("churchToolsBaseUrl");

	private String key;

	private UTPersistenceConstants(String settingsKey) {
		this.key = settingsKey;
	}

	public String getSettingsKey() {
		return key;
	}
}
