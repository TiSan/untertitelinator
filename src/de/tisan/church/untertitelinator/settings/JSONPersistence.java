package de.tisan.church.untertitelinator.settings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONPersistence extends Persistence
{

	private ObjectMapper mapper;
	private File settingsFile;
	private Map<String, Object> settings;

	private static JSONPersistence instance;

	public static JSONPersistence get()
	{
		return (instance == null ? (instance = new JSONPersistence()) : instance);
	}

	private JSONPersistence()
	{
		this.mapper = new ObjectMapper();
		this.mapper.enable(SerializationFeature.INDENT_OUTPUT);

		this.settingsFile = new File("settings.json");
		if (createFileIfNotExists(settingsFile))
		{
			this.settings = new HashMap<String, Object>();
			saveSettingsFile();
		}
		this.settings = loadSettingsFile();
	}

	private boolean createFileIfNotExists(File file)
	{
		try
		{
			if (!file.exists())
			{
				file.createNewFile();
				return true;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	private Map<String, Object> loadSettingsFile()
	{
		try
		{
			return mapper.readValue(settingsFile, new TypeReference<Map<String, Object>>()
			{
			});
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private void saveSettingsFile()
	{
		try
		{
			mapper.writeValue(settingsFile, settings);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Object getSetting(PersistenceConstants key)
	{
		return settings.get(key.getSettingsKey());
	}

	@Override
	public Object getSetting(PersistenceConstants key, Object defaultValue)
	{
		Object settingValue = getSetting(key);
		if (settingValue == null)
		{
			settingValue = defaultValue;
			setSetting(key, settingValue);
		}
		return settingValue;
	}

	@Override
	public void setSetting(PersistenceConstants key, Object value)
	{
		settings.put(key.getSettingsKey(), value);
		saveSettingsFile();
	}

}
