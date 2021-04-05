package de.tisan.church.untertitelinator.instancer.packets;

import de.tisan.church.untertitelinator.gui.keyer.GUIKeyerLayer;

public class GUIKeyerLayerChangePacket extends Packet{
	private static final long serialVersionUID = 1268973976649151700L;
	GUIKeyerLayer layerName;
	boolean visible;
	
	public GUIKeyerLayerChangePacket() {
	}

	public GUIKeyerLayerChangePacket(GUIKeyerLayer layerName, boolean visible) {
		super();
		this.layerName = layerName;
		this.visible = visible;
	}

	public GUIKeyerLayer getLayerName() {
		return layerName;
	}

	public void setLayerName(GUIKeyerLayer layerName) {
		this.layerName = layerName;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		return "Keyer Layer " + layerName + " is now " + (isVisible() ? "visible" : "not visible");
	}
	
	
}
