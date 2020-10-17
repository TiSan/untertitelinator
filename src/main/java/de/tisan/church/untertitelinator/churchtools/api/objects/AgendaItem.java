package de.tisan.church.untertitelinator.churchtools.api.objects;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgendaItem {
	long id;
	int position;
	String title;
	String type;
	String note;
	int duration;
	LocalDateTime start;
	boolean isBeforeEvent;
	AgendaResponsible responsible;
	List<ServiceGroupNote> serviceGroupNotes;
	AgendaSong song;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public boolean isBeforeEvent() {
		return isBeforeEvent;
	}

	public void setBeforeEvent(boolean isBeforeEvent) {
		this.isBeforeEvent = isBeforeEvent;
	}

	public AgendaResponsible getResponsible() {
		return responsible;
	}

	public void setResponsible(AgendaResponsible responsible) {
		this.responsible = responsible;
	}

	public List<ServiceGroupNote> getServiceGroupNotes() {
		return serviceGroupNotes;
	}

	public void setServiceGroupNotes(List<ServiceGroupNote> serviceGroupNotes) {
		this.serviceGroupNotes = serviceGroupNotes;
	}

	public AgendaSong getSong() {
		return song;
	}

	public void setSong(AgendaSong song) {
		this.song = song;
	}

}
