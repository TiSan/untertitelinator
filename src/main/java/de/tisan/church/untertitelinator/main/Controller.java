package de.tisan.church.untertitelinator.main;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;

public class Controller {

	private Untertitelinator untertitelinator;

	public Controller() {
		this.untertitelinator = new Untertitelinator();
	}

	public void init() {
		UTEventHub.get().publish(new CommandPacket(Command.LOAD_EVENTS), new CommandPacket(Command.LOAD_SONGS));
	}
}
