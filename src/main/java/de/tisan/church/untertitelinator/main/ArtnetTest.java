package de.tisan.church.untertitelinator.main;

import ch.bildspur.artnet.ArtNetClient;

public class ArtnetTest {
	public static void main(String[] args) {
		ArtNetClient artnet = new ArtNetClient();
		artnet.start();
		byte[] dmxData = new byte[512];
		// set data
		dmxData[0+17] = (byte) 0; //rot
		dmxData[1+17] = (byte) 0; //green
		dmxData[2+17] = (byte) 0; //blue
		
		dmxData[9+17] = (byte) 255;
		dmxData[10+17] = (byte) 255;
		
//		for(int i = 0; i < dmxData.length; i++) {
//			dmxData[i] = (byte) 0;
//		}

		// send data to localhost
		while(true) {
			artnet.unicastDmx("10.128.20.40", 0, 0, dmxData);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				artnet.stop();
			}
		}
//		artnet.broadcastDmx(0, 0, dmxData);
		
//		ArtDmxPacket packet = new ArtDmxPacket();
//		packet.setNumChannels(512);
//		packet.set
		
		//artnet.stop();
	}
}
