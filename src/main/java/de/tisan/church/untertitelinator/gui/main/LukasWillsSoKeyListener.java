package de.tisan.church.untertitelinator.gui.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.main.Loader;

public class LukasWillsSoKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			Untertitelinator.get().getCurrentPlayer().previousLine();
			Loader.getMainUi().updateUIComponents();
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Untertitelinator.get().getCurrentPlayer().nextLine();
			Loader.getMainUi().updateUIComponents();
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			Untertitelinator.get().getCurrentPlayer().pause();
			Loader.getMainUi().updateUIComponents();
			e.consume();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
