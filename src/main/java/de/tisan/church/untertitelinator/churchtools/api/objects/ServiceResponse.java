package de.tisan.church.untertitelinator.churchtools.api.objects;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceResponse {
	String NOTICE;
	List<Service> data;

	public String getNOTICE() {
		return NOTICE;
	}

	public void setNOTICE(String nOTICE) {
		NOTICE = nOTICE;
	}

	public List<Service> getData() {
		return data;
	}

	public void setData(List<Service> data) {
		this.data = data;
	}

}
