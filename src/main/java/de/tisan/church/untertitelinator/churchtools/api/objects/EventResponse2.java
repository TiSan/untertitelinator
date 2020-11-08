package de.tisan.church.untertitelinator.churchtools.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventResponse2 {
	String NOTICE;
	Event data;

	public String getNOTICE() {
		return NOTICE;
	}

	public void setNOTICE(String nOTICE) {
		NOTICE = nOTICE;
	}

	public Event getData() {
		return data;
	}

	public void setData(Event data) {
		this.data = data;
	}

}
