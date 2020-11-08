package de.tisan.church.untertitelinator.churchtools.api.objects;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventService {
	long id;
	long personId;
	Domain person;
	String name;
	long serviceId;
	boolean agreed;
	boolean isValid;
	LocalDateTime requestedDate;
	long requesterPersonId;
	Domain requesterPerson;
	String comment;
	long counter;
	boolean allowChat;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPersonId() {
		return personId;
	}
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	public Domain getPerson() {
		return person;
	}
	public void setPerson(Domain person) {
		this.person = person;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getServiceId() {
		return serviceId;
	}
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}
	public boolean isAgreed() {
		return agreed;
	}
	public void setAgreed(boolean agreed) {
		this.agreed = agreed;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public LocalDateTime getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(LocalDateTime requestedDate) {
		this.requestedDate = requestedDate;
	}
	public long getRequesterPersonId() {
		return requesterPersonId;
	}
	public void setRequesterPersonId(long requesterPersonId) {
		this.requesterPersonId = requesterPersonId;
	}
	public Domain getRequesterPerson() {
		return requesterPerson;
	}
	public void setRequesterPerson(Domain requesterPerson) {
		this.requesterPerson = requesterPerson;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public long getCounter() {
		return counter;
	}
	public void setCounter(long counter) {
		this.counter = counter;
	}
	public boolean isAllowChat() {
		return allowChat;
	}
	public void setAllowChat(boolean allowChat) {
		this.allowChat = allowChat;
	}
	
	
}
