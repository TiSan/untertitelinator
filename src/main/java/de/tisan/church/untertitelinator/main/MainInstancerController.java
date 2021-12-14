package de.tisan.church.untertitelinator.main;

import de.tisan.church.untertitelinator.instancer.UTInstance;
import de.tisan.church.untertitelinator.instancer.UTInstancer;

public class MainInstancerController {
	public static void main(String[] args) {
		UTInstancer instancer = new UTInstancer();
		instancer.startModule(UTInstance.CONTROLLER);
	}
}
