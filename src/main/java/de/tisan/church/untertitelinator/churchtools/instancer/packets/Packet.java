package de.tisan.church.untertitelinator.churchtools.instancer.packets;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(property = "@class", use = JsonTypeInfo.Id.CLASS, visible = true, include = As.PROPERTY)
public abstract class Packet implements Serializable {
	private static final long serialVersionUID = 1L;

}
