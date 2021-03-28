package de.tisan.church.untertitelinator.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EventService {
	String name;
	List<ServiceCast> castList;

	public EventService() {
	}

	public EventService(String name) {
		this(name, new ArrayList<ServiceCast>());
	}

	public EventService(String name, List<ServiceCast> castList) {
		super();
		this.name = name;
		this.castList = castList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ServiceCast> getCastList() {
		return castList;
	}

	public void setCastList(List<ServiceCast> castList) {
		this.castList = castList;
	}
	
	public void addCast(String cast) {
		castList.add(new ServiceCast(cast));
		castList = castList.stream().sorted(Comparator.comparing(ServiceCast::getName)).collect(Collectors.toList());
	}

	@JsonIgnore
	public String getCastListString() {
		return String.join(", ", castList.stream().map(ServiceCast::getName).collect(Collectors.toList()));
	}
}
