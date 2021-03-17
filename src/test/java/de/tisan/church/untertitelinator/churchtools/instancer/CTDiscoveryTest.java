package de.tisan.church.untertitelinator.churchtools.instancer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CTDiscoveryTest
{
	CTDiscovery objectToTest;

	@BeforeEach
	public void init()
	{
		objectToTest = new CTDiscovery();
	}

	@Test
	public void testIp()
	{
		System.out.println(objectToTest.getIpList());
	}
}
