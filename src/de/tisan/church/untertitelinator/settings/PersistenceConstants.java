package de.tisan.church.untertitelinator.settings;

public enum PersistenceConstants
{
	GUIPRESENTATORDELAY("guiPresentatorDelay"), GUIPRESENTATORWIDTH("guiPresentatorWidth"), GUIPRESENTATORHEIGHT(
	        "guiPresentatorHeight"), GUIPRESENTATORX("guiPresentatorX"), GUIPRESENTATORY(
	                "guiPresentatorY"), LINESEPARATOR(
	                        "lineSeparator"), CURRENTLINEFILEPATH("currentLineFilePath"), NEXTLINEFILEPATH(
	                                "nextLineFilePath"), BLACKOUTLINEFILLER("blackoutLineFiller"), SONGSFOLDERPATH(
	                                        "songsFolderPath"), SONGFILESUFFIX("songFileSuffix"), CHURCHNAME("churchName"), SERVERPORT("8080");

	private String key;

	private PersistenceConstants(String key)
	{
		this.key = key;
	}

	public String getSettingsKey()
	{
		return key;
	}
}
