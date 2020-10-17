package de.tisan.church.untertitelinator.churchtools.api.objects;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgendaResponse {
	String NOTICE;
	Agenda data;
	Map<String, String> metas;

	public String getNOTICE() {
		return NOTICE;
	}

	public void setNOTICE(String nOTICE) {
		NOTICE = nOTICE;
	}

	public Agenda getData() {
		return data;
	}

	public void setData(Agenda data) {
		this.data = data;
	}

	public Map<String, String> getMetas() {
		return metas;
	}

	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
	}

}
