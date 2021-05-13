package de.tisan.church.untertitelinator.instancer.packets;

import de.tisan.church.untertitelinator.gui.keyer.GUIKeyerLayer;

public class GUIKeyerLayerChangePacket extends Packet{
	private static final long serialVersionUID = 1268973976649151700L;
	GUIKeyerLayer layerName;
	CommandState state;
	
	public GUIKeyerLayerChangePacket() {
	}

	public GUIKeyerLayerChangePacket(GUIKeyerLayer layerName, CommandState state) {
		super();
		this.layerName = layerName;
		this.state = state;
	}

	public GUIKeyerLayer getLayerName() {
		return layerName;
	}

	public void setLayerName(GUIKeyerLayer layerName) {
		this.layerName = layerName;
	}

	public CommandState getState() {
		return state;
	}

	@Override
	public String toString() {
		return "Keyer Layer " + layerName + " is now " + (getState().equals(CommandState.ON) ? "visible" : "not visible");
	}
	
	
}
