package de.tisan.church.untertitelinator.churchtools.api.objects;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgendaResponsible {
	String text;
	List<AgendaResponsiblePerson> persons;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<AgendaResponsiblePerson> getPersons() {
		return persons;
	}

	public void setPersons(List<AgendaResponsiblePerson> persons) {
		this.persons = persons;
	}

}
