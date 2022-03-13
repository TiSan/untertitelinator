package de.tisan.church.untertitelinator.instancer.packets;

import java.util.List;

public class KeyerMonitorListPacket extends Packet {

	private static final long serialVersionUID = 7413953528781897492L;
	
	List<Monitor> monitorList;

	public KeyerMonitorListPacket() {
	}

	public KeyerMonitorListPacket(List<Monitor> monitorList) {
		this.monitorList = monitorList;
	}

	public List<Monitor> getMonitorList() {
		return monitorList;
	}

	public void setMonitorList(List<Monitor> monitorList) {
		this.monitorList = monitorList;
	}

	@Override
	public String toString() {
		return "Keyer Monitorlist with " + monitorList.size() + " monitors";
	}
}
