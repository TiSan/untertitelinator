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
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;

public class GUIKeyerStartPagePanel extends JPanel {

	private static final long serialVersionUID = 4284425054258046422L;
	private FlatButton layerNextStream;
	private FlatButton layerTitle;
	private FlatButton layerSubTitle;
	private FlatButton layerDate;
	private ArrayList<FlatButton> listCastLayers = new ArrayList<FlatButton>();
	private JLabel layerImage;
	private BufferedImage image;
	private int heightCastBegin;
	private FlatLayoutManager man;
	private int widthElements;

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

		/* Bild */
		layerImage = new JLabel();
		try {
			image = ImageIO.read(GUIKeyerStartPagePanel.class.getResourceAsStream("/de/tisan/church/untertitelinator/resources/bg.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		fitImage(preferredSize.width, preferredSize.height);
		add(layerImage);
		
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				fitImage(getWidth(), getHeight());
			}
		});
		repaint();
	}

	public void fitImage(int width, int height) {
		if (isVisible()) {
			layerImage.setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
		}
		layerImage.setBounds(0, 0, width, height);
	}

	public void showNextStream(String streamTitle, String streamSubtitle, String streamDate, Map<String, String> cast) {
		layerTitle.setText(streamTitle);
		layerSubTitle.setText(streamSubtitle);
		layerDate.setText(streamDate);
		
		listCastLayers.forEach(e -> remove(e));
		listCastLayers.clear();

		Font font = FlatFont.getInstance(30, Font.BOLD);
		int heightElements = 50;
		int x1 = 150;
		int y1 = heightCastBegin;
		
		for (String castItem : cast.keySet()) {
			if(cast.get(castItem) == null) {
				continue;
			}
			System.out.println(castItem);
			FlatButton layerCast = new FlatButton(castItem + ": " + cast.get(castItem), man);
			System.out.println("x1=" + x1 + ";y1=" + y1 + ";width=" + widthElements + ";height=" + heightElements);
			layerCast.setBounds(x1, y1, widthElements, heightElements);
			layerCast.setFont(font);
			layerCast.setBackground(new Color(0, 0, 0, 0));
			layerCast.setAnchor(Anchor.LEFT, Anchor.RIGHT);
			layerCast.setCenterText(false);
			layerCast.disableEffects();
			
			y1 += layerCast.getHeight() + 5;
			
			add(layerCast, 0);
			listCastLayers.add(layerCast);
		}
	}
}
