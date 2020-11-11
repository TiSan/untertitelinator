package de.tisan.church.untertitelinator.main;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.tisan.flatui.components.fcommons.FlatLayoutManager;

public class GUIKeyerLogoPanel extends JPanel {
	private JLabel layerImage;
	private BufferedImage image;

	public GUIKeyerLogoPanel(FlatLayoutManager man, GUIKeyer instance, Dimension preferredSize) {
		setLayout(null);
		setOpaque(false);
		layerImage = new JLabel();
		try {
			image = ImageIO.read(
					GUIKeyer.class.getResourceAsStream("/de/tisan/church/untertitelinator/resources/bg_logo.png"));
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

}
