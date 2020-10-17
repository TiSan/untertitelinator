package de.tisan.church.untertitelinator.churchtools.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarDomainAttributes {
	String campusName;
}
