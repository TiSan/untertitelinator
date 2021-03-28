package de.tisan.church.untertitelinator.instancer.packets;

import de.tisan.church.untertitelinator.churchtools.api.objects.Event;

public class EventSelectionChangedPacket extends Packet{
	private static final long serialVersionUID = -7327533510839271401L;
	Event event;

	public EventSelectionChangedPacket() {
	}

	public EventSelectionChangedPacket(Event event) {
		super();
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

}
