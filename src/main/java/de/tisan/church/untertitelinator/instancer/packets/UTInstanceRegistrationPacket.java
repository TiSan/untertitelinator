package de.tisan.church.untertitelinator.instancer.packets;

import de.tisan.church.untertitelinator.instancer.UTInstanceType;

public class UTInstanceRegistrationPacket {

	UTInstanceType type;

	public UTInstanceRegistrationPacket() {
	}

	public UTInstanceRegistrationPacket(UTInstanceType type) {
		super();
		this.type = type;
	}

	public UTInstanceType getType() {
		return type;
	}

	public void setType(UTInstanceType type) {
		this.type = type;
	}

}
