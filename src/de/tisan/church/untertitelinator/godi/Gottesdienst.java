package de.tisan.church.untertitelinator.godi;

import java.util.Date;
import java.util.List;

public class Gottesdienst
{
	private List<CCLISongPlayment> playments;
	private Date date;

	public Gottesdienst()
	{

	}

	public List<CCLISongPlayment> getPlayments()
	{
		return playments;
	}

	public void setPlayments(List<CCLISongPlayment> playments)
	{
		this.playments = playments;
	}

}
