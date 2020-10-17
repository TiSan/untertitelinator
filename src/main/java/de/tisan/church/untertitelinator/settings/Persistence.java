package de.tisan.church.untertitelinator.settings;

public abstract class Persistence
{

	public abstract Object getSetting(PersistenceConstants key);

	public abstract Object getSetting(PersistenceConstants key, Object defaultValue);

	public abstract void setSetting(PersistenceConstants key, Object value);
	
}
