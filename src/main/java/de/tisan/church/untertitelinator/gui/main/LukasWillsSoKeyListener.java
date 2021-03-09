package de.tisan.church.untertitelinator.gui.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LukasWillsSoKeyListener implements KeyListener {
	private GUIMain instance;

	public LukasWillsSoKeyListener(GUIMain instance) {
		this.instance = instance;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			instance.previousLine();
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			instance.nextLine();
			e.consume();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			instance.pause();
			e.consume();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
