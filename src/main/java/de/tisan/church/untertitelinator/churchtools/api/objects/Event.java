package de.tisan.church.untertitelinator.churchtools.api.objects;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
	long id;
	String guid;
	String name;
	String description;
	LocalDateTime startDate;
	LocalDateTime endDate;
	String chatStatus;
	EventPermission permissions;
	Domain calendar;
	List<EventService> eventServices;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public String getChatStatus() {
		return chatStatus;
	}

	public void setChatStatus(String chatStatus) {
		this.chatStatus = chatStatus;
	}

	public EventPermission getPermissions() {
		return permissions;
	}

	public void setPermissions(EventPermission permissions) {
		this.permissions = permissions;
	}

	public Domain getCalendar() {
		return calendar;
	}

	public void setCalendar(Domain calendar) {
		this.calendar = calendar;
	}

	public List<EventService> getEventServices() {
		return eventServices;
	}

	public void setEventServices(List<EventService> eventServices) {
		this.eventServices = eventServices;
	}
	

}