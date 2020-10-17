package de.tisan.church.untertitelinator.churchtools.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgendaSong {
	long songId;
	int arrangementId;
	String title;
	String arrangement;
	String category;
	String key;
	String bpm;
	boolean isDefault;

	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}

	public int getArrangementId() {
		return arrangementId;
	}

	public void setArrangementId(int arrangementId) {
		this.arrangementId = arrangementId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArrangement() {
		return arrangement;
	}

	public void setArrangement(String arrangement) {
		this.arrangement = arrangement;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBpm() {
		return bpm;
	}

	public void setBpm(String bpm) {
		this.bpm = bpm;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

}
