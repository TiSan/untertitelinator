package de.tisan.church.untertitelinator.godi;

import java.util.List;

import de.tisan.church.untertitelinator.ccli.CCLISong;

public class CCLISongPlayment
{
	private CCLISong song;

	private List<String> verseOrder;

	public CCLISongPlayment()
	{
	}

	public CCLISong getSong()
	{
		return song;
	}

	public void setSong(CCLISong song)
	{
		this.song = song;
	}

	public List<String> getVerseOrder()
	{
		return verseOrder;
	}

	public void setVerseOrder(List<String> verseOrder)
	{
		this.verseOrder = verseOrder;
	}

}
