package de.tisan.church.untertitelinator.gui.keyer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.tisan.church.untertitelinator.churchtools.api.objects.Event;
import de.tisan.church.untertitelinator.data.EventService;
import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.EventSelectionChangedPacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.tisanapi.logger.Logger;

public class GUIKeyerStartPagePanel extends JPanel {

	private static final long serialVersionUID = 4284425054258046422L;
	private GUIKeyerMarqueePanel marqueePanel;
	private FlatButton layerNextStream;
	private FlatButton layerTitle;
	private FlatButton layerSubTitle;
	private FlatButton layerDate;
	private JLabel layerImage;
	private BufferedImage image;
	private int heightCastBegin;
	private FlatLayoutManager man;
	private int widthElements;
	private boolean eventReceived;

	public GUIKeyerStartPagePanel(FlatLayoutManager man, GUIKeyer instance, Dimension preferredSize) {
		setLayout(null);

		this.man = man;
		Font font = FlatFont.getInstance(70, Font.BOLD);
		int spaceX = 30;
		widthElements = preferredSize.width - (spaceX * 2);
		int height3 = 70;
		int x1 = 150;
		int y1 = 100;
		font = FlatFont.getInstance(50, Font.BOLD);

		layerNextStream = new FlatButton("Nächster Stream: ", man);
		layerNextStream.setBounds(x1, y1, widthElements - x1, height3);
		layerNextStream.setFont(font);
		layerNextStream.setBackground(new Color(0, 0, 0, 0));
		layerNextStream.setCenterText(false);
		layerNextStream.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		layerNextStream.disableEffects();
		add(layerNextStream);

		y1 += layerNextStream.getHeight() + 5;
		font = FlatFont.getInstance(90, Font.BOLD);
		height3 = 130;

		layerTitle = new FlatButton("Titel der Veranstaltung", man);
		layerTitle.setBounds(x1, y1, widthElements - x1, height3);
		layerTitle.setFont(font);
		layerTitle.setBackground(new Color(0, 0, 0, 0));
		layerTitle.setCenterText(false);
		layerTitle.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		layerTitle.disableEffects();
		add(layerTitle);

		y1 += layerTitle.getHeight() + 5;
		font = FlatFont.getInstance(70, Font.BOLD);
		height3 = 100;

		layerSubTitle = new FlatButton("Untertitel der Veranstaltung", man);
		layerSubTitle.setBounds(x1, y1, widthElements - x1, height3);
		layerSubTitle.setFont(font);
		layerSubTitle.setBackground(new Color(0, 0, 0, 0));
		layerSubTitle.setCenterText(false);
		layerSubTitle.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		layerSubTitle.disableEffects();
		add(layerSubTitle);

		y1 += layerTitle.getHeight() + 5;
		font = FlatFont.getInstance(50, Font.BOLD);
		height3 = 70;

		layerDate = new FlatButton("Datum", man);
		layerDate.setBounds(x1, y1, widthElements - x1, height3);
		layerDate.setFont(font);
		layerDate.setBackground(new Color(0, 0, 0, 0));
		layerDate.setCenterText(false);
		layerDate.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		layerDate.disableEffects();
		add(layerDate);


		y1 += layerSubTitle.getHeight() + 100;
		heightCastBegin = y1;

		marqueePanel = new GUIKeyerMarqueePanel("+++ Von WLAN enttäuscht: Laschet kann Frust über Ergebnis nicht twittern +++ Auf den ersten Blick: Augenarzt verliebt sich in Iris +++ Thunfisch in Öl: Gemälde ruft Tierschützer auf den Plan +++" +
				"", instance);
		marqueePanel.setBounds(0, preferredSize.height - 230, preferredSize.width, height3);
		marqueePanel.setForeground(FlatColors.BLACK);
		marqueePanel.setFont(font);
		add(marqueePanel);
		/* Bild */
		layerImage = new JLabel();
		try {
			image = ImageIO.read(GUIKeyerStartPagePanel.class
					.getResourceAsStream("/de/tisan/church/untertitelinator/resources/bg_v2.jpg"));
		} catch (IOException e1) {
			Logger.getInstance().err("Couldnt load BeginCard Image! " + e1.getMessage(), e1, getClass());
			
		}
		fitImage(preferredSize.width, preferredSize.height);
		add(layerImage);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				fitImage(getWidth(), getHeight());
			}
		});

		UTEventHub.get().registerListener(new UTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				if (packet instanceof EventSelectionChangedPacket) {
					EventSelectionChangedPacket sPacket = (EventSelectionChangedPacket) packet;
					Event currentEvent = sPacket.getEvent();
					String themaString = currentEvent.getName();

					// Gottesdienst-Titel aus "Info-Feld" lesen
					String titleString = currentEvent.getDescription();
					int indexEnter = titleString.indexOf("\n");
					if (indexEnter == -1) {
						indexEnter = titleString.length();
					}

					titleString = titleString.substring(0, indexEnter);

					// Fallback, wenn nicht gesetzt
					if (titleString.startsWith("Weitere Infos...")) {
						titleString = "Live-Gottesdienst";
					}
					GUIKeyerStartPagePanel.this.eventReceived = true;
					showNextStream(titleString, "Thema: \"" + themaString + "\"",
							currentEvent.getStartDateString() + " Uhr", sPacket.getServices());
				}
			}

		});

		repaint();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while(GUIKeyerStartPagePanel.this.eventReceived == false){
					UTEventHub.get().publish(new CommandPacket(Command.SEND_EVENT));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	private void fitImage(int width, int height) {
		if (isVisible()) {
			layerImage.setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
		}
		layerImage.setBounds(0, 0, width, height);
	}

	private void showNextStream(String streamTitle, String streamSubtitle, String streamDate, List<EventService> cast) {
		layerTitle.setText(streamTitle);
		layerSubTitle.setText(streamSubtitle);
		layerDate.setText(streamDate);

		Font font = FlatFont.getInstance(30, Font.BOLD);
		int heightElements = 50;
		int x1 = 150;
		int y1 = heightCastBegin;
		String castTicker =	cast
				.stream()
				.map(service -> service.getName() + ": " + service.getCastListString())
				.collect(Collectors.joining(" +++ "));
		marqueePanel.setText("Mitwirkende +++ " + castTicker);
	}
}
