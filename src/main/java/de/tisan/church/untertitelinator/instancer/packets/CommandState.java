package de.tisan.church.untertitelinator.instancer.packets;

import java.util.List;

public enum CommandState {
	ON, OFF;

	public CommandState toggle() {
		return this == ON ? OFF : ON;
	}

	public static CommandState convert(List<String> args) {
		if(args != null && args.size() > 0) {
			return CommandState.valueOf(args.get(0));
		}
		return null;
	}
}
