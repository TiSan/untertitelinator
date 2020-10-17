package de.tisan.church.untertitelinator.churchtools.api.objects;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Agenda {
	long id;
	String name;
	String series;
	boolean isFinal;
	int eventStartPosition;
	long calendarId;
	int total;
	List<AgendaItem> items = new ArrayList<AgendaItem>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public int getEventStartPosition() {
		return eventStartPosition;
	}

	public void setEventStartPosition(int eventStartPosition) {
		this.eventStartPosition = eventStartPosition;
	}

	public long getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(long calendarId) {
		this.calendarId = calendarId;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<AgendaItem> getItems() {
		return items;
	}

	public void setItems(List<AgendaItem> items) {
		this.items = items;
	}

}
