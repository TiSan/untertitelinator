package de.tisan.church.untertitelinator.ccli;

import java.io.IOException;

import de.tisan.church.untertitelinator.server.LambdatraServer;
import de.tisan.church.untertitelinator.server.impl.CCLISongParserTest;

public class Main {
	public static void main(String[] args) throws IOException {
		LambdatraServer.get().addListener(new CCLISongParserTest());
	}
}
