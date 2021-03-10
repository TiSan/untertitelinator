package de.tisan.church.untertitelinator.churchtools.api;

import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CacheEntry {
	String data;
	long created;

	public CacheEntry(String data) {
		this.created = System.currentTimeMillis();
		this.data = data;
	}

	public CacheEntry() {
	}

	public String getData() {
		return data;
	}

	@JsonIgnore
	public String getDataDecoded() {
		return new String(Base64.getDecoder().decode(data));
	}

	public long getCreated() {
		return created;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setCreated(long created) {
		this.created = created;
	}

}
