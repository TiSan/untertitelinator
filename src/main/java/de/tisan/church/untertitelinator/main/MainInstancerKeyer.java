package de.tisan.church.untertitelinator.main;

import de.tisan.church.untertitelinator.instancer.UTInstance;
import de.tisan.church.untertitelinator.instancer.UTInstancer;

public class MainInstancerKeyer {
	public static void main(String[] args) {
		System.out.println("hi");
		UTInstancer instancer = new UTInstancer();
		instancer.startModule(UTInstance.KEYER);
	}
}
