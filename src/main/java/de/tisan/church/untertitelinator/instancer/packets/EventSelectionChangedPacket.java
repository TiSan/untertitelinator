package de.tisan.church.untertitelinator.instancer.packets;

import java.util.List;

import de.tisan.church.untertitelinator.churchtools.api.objects.Event;
import de.tisan.church.untertitelinator.data.EventService;

public class EventSelectionChangedPacket extends Packet {
	private static final long serialVersionUID = -7327533510839271401L;
	Event event;

	List<EventService> services;

	public EventSelectionChangedPacket() {
	}

	public EventSelectionChangedPacket(Event event, List<EventService> services) {
		super();
		this.event = event;
		this.services = services;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<EventService> getServices() {
		return services;
	}

	public void setServices(List<EventService> services) {
		this.services = services;
	}

	@Override
	public String toString() {
		return "Event: " + event.getName();
	}

}
