package de.tisan.church.untertitelinator.gui.keyer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.instancer.packets.SongLinePacket;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.tisanapi.logger.Logger;

public class GUIKeyerUntertitelPanel extends JPanel {

	private static final long serialVersionUID = 4135343673389911574L;
	private FlatButton currentLine1;
	private FlatButton currentLine2;
	private GUIKeyer keyer;
	private Color backgroundDarker;

	public GUIKeyerUntertitelPanel(FlatLayoutManager man, GUIKeyer instance, Dimension preferredSize) {
		this.keyer = instance;
		backgroundDarker = instance.bg.darker().darker().darker();
		setLayout(null);
		setBackground(instance.bg);

		Font font = FlatFont.getInstance(70, Font.BOLD);
		final int height = 100;
		int spaceY = (int) (preferredSize.height - (height * 3));
		int spaceX = 30;
		int width = getWidth() - (spaceX * 2);

		currentLine1 = new FlatButton("", man);
		currentLine1.setBounds(spaceX, spaceY, width, height);
		currentLine1.setFont(font);
		currentLine1.setBackground(instance.bg);
		currentLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		add(currentLine1);

		currentLine2 = new FlatButton("", man);
		currentLine2.setBounds(spaceX, currentLine1.getY() + currentLine1.getHeight(), width, height);
		currentLine2.setFont(font);
		currentLine2.setBackground(instance.bg);
		currentLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		add(currentLine2);

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				currentLine1.setBounds(spaceX, (getHeight() - (height * 3)), getWidth() - (spaceX * 2), height);
				currentLine2.setBounds(spaceX, currentLine1.getY() + currentLine1.getHeight(),
						getWidth() - (spaceX * 2), height);

			}
		});
		UTEventHub.get().registerListener(new UTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				if (packet instanceof SongLinePacket) {
					SongLinePacket sPacket = (SongLinePacket) packet;
					new Thread(new Runnable() {

						@Override
						public void run() {
							showLines(sPacket.getCurrentLines().get(0), sPacket.getCurrentLines().get(1));
						}
					}).start();
				}
			}
		});

	}

	private void showLines(String line1, String line2) {
		if (line1.equals(currentLine1.getText()) && line2.equals(currentLine2.getText())) {
			return;
		}

		if (line1.trim().isEmpty() == false || line2.trim().isEmpty() == false) {
			currentLine1.setBackground(backgroundDarker, true);
			currentLine2.setBackground(backgroundDarker, true);
		}

		currentLine1.setForeground(backgroundDarker, true);
		currentLine2.setForeground(backgroundDarker, true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Logger.getInstance().err("Timer1 for animation stopped! " + e.getMessage(), e, getClass());
			
		}

		currentLine1.setText(line1);
		currentLine2.setText(line2);

		currentLine1.setForeground(keyer.fg, true);
		currentLine2.setForeground(keyer.fg, true);
		if (line1.trim().isEmpty() && line2.trim().isEmpty()) {
			currentLine1.setBackground(keyer.bg, true);
			currentLine2.setBackground(keyer.bg, true);
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Logger.getInstance().err("Timer2 for animation stopped! " + e.getMessage(), e, getClass());
			
		}

	}

}
