package de.tisan.church.untertitelinator.gui.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Command;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.UIRefreshPacket;

public class LukasWillsSoKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			CTEventHub.get().publish(new CommandPacket(Command.PREVIOUS_LINE));
			CTEventHub.get().publish(new UIRefreshPacket());
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			CTEventHub.get().publish(new CommandPacket(Command.NEXT_LINE));
			CTEventHub.get().publish(new UIRefreshPacket());
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			CTEventHub.get().publish(new CommandPacket(Command.PAUSE));
			CTEventHub.get().publish(new UIRefreshPacket());
			e.consume();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
