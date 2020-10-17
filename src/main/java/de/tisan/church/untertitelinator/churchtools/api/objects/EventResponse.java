package de.tisan.church.untertitelinator.churchtools.api.objects;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventResponse {
	String NOTICE;
	List<Event> data;
	Map<String, String> metas;

	public String getNOTICE() {
		return NOTICE;
	}

	public void setNOTICE(String nOTICE) {
		NOTICE = nOTICE;
	}

	public List<Event> getData() {
		return data;
	}

	public void setData(List<Event> data) {
		this.data = data;
	}

	public Map<String, String> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}

}
