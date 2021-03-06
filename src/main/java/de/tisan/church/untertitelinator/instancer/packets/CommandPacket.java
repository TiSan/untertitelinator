package de.tisan.church.untertitelinator.instancer.packets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandPacket extends Packet {
	private static final long serialVersionUID = -225369293860442376L;
	Command command;
	List<String> args;

	public CommandPacket() {
	}

	public CommandPacket(Command command, List<String> args) {
		super();
		this.command = command;
		this.args = args;
	}
	
	public CommandPacket(Command command, String... args) {
		this(command, Arrays.asList(args));
	}

	public CommandPacket(Command command) {
		this(command, new ArrayList<String>());
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}

	@Override
	public String toString() {
		return "Command: " + command.name() + ", Args: " + (args != null ? Arrays.toString(args.toArray()) : args);
	}

}
