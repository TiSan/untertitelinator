package de.tisan.church.untertitelinator.gui.presentator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.instancer.packets.SongLinePacket;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class GUIPresentatorUntertitelPanel extends JPanel {

	private static final long serialVersionUID = 4135343673389911574L;
	
	private FlatButton currentLine1;
	private FlatButton currentLine2;
	private FlatButton nextLine1;
	private FlatButton nextLine2;
	private FlatButton titleLine;
	
	private GUIPresentator presentator;

	public GUIPresentatorUntertitelPanel(FlatLayoutManager man, GUIPresentator instance, Dimension preferredSize) {
		this.presentator = instance;
		setLayout(null);
		setBackground(instance.bg);

		int borderY = 10;
		int spaceX = 30;
		int width = preferredSize.width - (spaceX * 2);
		int height = 100;
		Color fgColor = FlatColors.GRAY;

		titleLine = new FlatButton("", man);
		titleLine.setBounds(spaceX, 30, width, height);
		titleLine.setBackground(FlatColors.BLACK);
		titleLine.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		titleLine.setForeground(fgColor);
		add(titleLine);

		currentLine1 = new FlatButton("", man);
		currentLine1.setBounds(spaceX, ((preferredSize.height / 2) - height / 2) - (height / 2), width, height);
		currentLine1.setBackground(FlatColors.BLACK);
		currentLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		add(currentLine1);

		currentLine2 = new FlatButton("", man);
		currentLine2.setBounds(spaceX, ((preferredSize.height / 2) - height / 2) + (height / 2), width, height);
		currentLine2.setBackground(FlatColors.BLACK);
		currentLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		add(currentLine2);

		nextLine2 = new FlatButton("", man);
		nextLine2.setBounds(spaceX, preferredSize.height - borderY - height, width, height);
		nextLine2.setBackground(FlatColors.BLACK);
		nextLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		nextLine2.setForeground(fgColor);
		add(nextLine2);

		nextLine1 = new FlatButton("", man);
		nextLine1.setBounds(spaceX, nextLine2.getY() - height, width, height);
		nextLine1.setBackground(FlatColors.BLACK);
		nextLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		nextLine1.setForeground(fgColor);
		add(nextLine1);

		UTEventHub.get().registerListener(new UTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				if (packet instanceof SongLinePacket) {
					SongLinePacket sPacket = (SongLinePacket) packet;
					if (sPacket.getSongPlayer() != null) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								calculateFontSize(sPacket.getSongPlayer().getSong().getSongLines());
								showNewTextLines(sPacket.getSongPlayer().getSong().getTitle(),
										sPacket.getCurrentLines().get(0), sPacket.getCurrentLines().get(1),
										sPacket.getNextLines().get(0), sPacket.getNextLines().get(1), 0,
										sPacket.getCurrentLines().stream().filter(Objects::nonNull)
												.filter(e -> (e.length() > 0)).findFirst().isPresent() == false);
							}
						}).start();
					}
				} 
			}
		});

	}

	private void calculateFontSize(List<String> linesToShow) {
		Font newFont = FlatFont.getInstance(80, Font.BOLD);
		for (String line : linesToShow) {
			if (line.contains("\n")) {
				String[] s = line.split("\n", 2);
				newFont = getFontSizeFor(s[0], newFont);
				newFont = getFontSizeFor(s[1], newFont);

			} else {
				newFont = getFontSizeFor(line, newFont);
			}
		}
		updateFont(newFont);
	}

	private Font getFontSizeFor(String line, Font newFont) {
		int x1 = 0;
		int y1 = 0;
		while (true) {
			FontMetrics fm = getGraphics().getFontMetrics(newFont);
			java.awt.geom.Rectangle2D rect = fm.getStringBounds(line, getGraphics());
			int textHeight = (int) (rect.getHeight());
			int textWidth = (int) (rect.getWidth());
			int panelHeight = getHeight();
			int panelWidth = getWidth();
			x1 = (panelWidth - textWidth) / 2;
			y1 = (panelHeight - textHeight) / 2 + fm.getAscent();
			if (x1 < 2 || y1 < 2) {
				newFont = FlatFont.getInstance(newFont.getSize() - 1, newFont.getStyle());
			} else {
				break;
			}
		}

		return newFont;
	}

	private void updateFont(Font newFont) {
		titleLine.setFont(newFont);
		currentLine1.setFont(newFont);
		currentLine2.setFont(newFont);
		nextLine1.setFont(newFont);
		nextLine2.setFont(newFont);
	}

	private void showNewTextLines(String title, String line1, String line2, String line3, String line4, int delay,
			boolean paused) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
				titleLine.setText(paused
						? (String) JSONPersistence.get().getSetting(UTPersistenceConstants.BLACKOUTLINEFILLER, "")
						: title);
				currentLine1.setText(line1);
				currentLine2.setText(line2);
				nextLine1.setText(line3);
				nextLine2.setText(line4);
			}
		}).start();
	}

}
