package de.tisan.church.untertitelinator.instancer.packets;

import java.util.List;

import de.tisan.church.untertitelinator.churchtools.api.objects.Event;

public class EventListPacket extends Packet {
	private static final long serialVersionUID = -7327533510839271401L;

	List<Event> eventList;

	public EventListPacket() {
	}

	public EventListPacket(List<Event> eventList) {
		super();
		this.eventList = eventList;
	}

	public List<Event> getEventList() {
		return eventList;
	}

	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}

	@Override
	public String toString() {
		return "EventList: " + eventList.size();
	}

}
