package de.tisan.church.untertitelinator.instancer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.tisan.church.untertitelinator.instancer.UTDiscovery;

public class UTDiscoveryTest
{
	UTDiscovery objectToTest;

	@BeforeEach
	public void init()
	{
		objectToTest = new UTDiscovery();
	}

	@Test
	public void testIp()
	{
		System.out.println(objectToTest.getIpList());
	}
}
