package de.tisan.church.untertitelinator.gui.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.UIRefreshPacket;
import de.tisan.church.untertitelinator.data.Untertitelinator;

public class LukasWillsSoKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			Untertitelinator.get().getCurrentPlayer().previousLine();
			CTEventHub.get().publish(new UIRefreshPacket());
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Untertitelinator.get().getCurrentPlayer().nextLine();
			CTEventHub.get().publish(new UIRefreshPacket());
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			Untertitelinator.get().getCurrentPlayer().pause();
			CTEventHub.get().publish(new UIRefreshPacket());
			e.consume();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
