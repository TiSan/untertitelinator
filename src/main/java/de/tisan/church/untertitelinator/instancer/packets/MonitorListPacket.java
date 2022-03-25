package de.tisan.church.untertitelinator.instancer.packets;

import java.util.List;

import de.tisan.church.untertitelinator.instancer.UTInstance;

public class MonitorListPacket extends Packet {

	private static final long serialVersionUID = 7413953528781897492L;
	
	List<Monitor> monitorList;
	UTInstance instanceType;

	public MonitorListPacket() {
	}

	

	public MonitorListPacket(List<Monitor> monitorList, UTInstance instanceType)
	{
		super();
		this.monitorList = monitorList;
		this.instanceType = instanceType;
	}



	public UTInstance getInstanceType()
	{
		return instanceType;
	}



	public void setInstanceType(UTInstance instanceType)
	{
		this.instanceType = instanceType;
	}



	public List<Monitor> getMonitorList() {
		return monitorList;
	}

	public void setMonitorList(List<Monitor> monitorList) {
		this.monitorList = monitorList;
	}

	@Override
	public String toString() {
		return instanceType + " Monitorlist with " + monitorList.size() + " monitors";
	}
}
