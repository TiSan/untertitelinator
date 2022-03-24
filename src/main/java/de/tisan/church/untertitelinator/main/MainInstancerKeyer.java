package de.tisan.church.untertitelinator.main;

import de.tisan.church.untertitelinator.instancer.UTInstanceType;
import de.tisan.church.untertitelinator.instancer.UTInstancer;

public class MainInstancerKeyer {
	public static void main(String[] args) {
		UTInstancer.get().startModule(UTInstanceType.KEYER);
	}
}
