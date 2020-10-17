package de.tisan.church.untertitelinator.churchtools.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgendaResponsiblePerson {
	String service;
	boolean accepted;
	Domain person;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public Domain getPerson() {
		return person;
	}

	public void setPerson(Domain person) {
		this.person = person;
	}

}
