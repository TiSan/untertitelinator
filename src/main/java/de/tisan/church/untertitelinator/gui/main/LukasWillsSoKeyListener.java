package de.tisan.church.untertitelinator.gui.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.UIRefreshPacket;

public class LukasWillsSoKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			UTEventHub.get().publish(new CommandPacket(Command.PREVIOUS_LINE));
			UTEventHub.get().publish(new UIRefreshPacket());
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			UTEventHub.get().publish(new CommandPacket(Command.NEXT_LINE));
			UTEventHub.get().publish(new UIRefreshPacket());
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			UTEventHub.get().publish(new CommandPacket(Command.PAUSE));
			UTEventHub.get().publish(new UIRefreshPacket());
			e.consume();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
