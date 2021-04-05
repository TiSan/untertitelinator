package de.tisan.church.untertitelinator.gui.keyer;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.tisanapi.logger.Logger;

public class GUIKeyerLogoPanel extends JPanel {

	private static final long serialVersionUID = 8624323979144171302L;
	private JLabel layerImage;
	private BufferedImage image;

	public GUIKeyerLogoPanel(FlatLayoutManager man, GUIKeyer instance, Dimension preferredSize) {
		setLayout(null);
		setOpaque(false);

		layerImage = new JLabel();

		try {
			image = ImageIO.read(GUIKeyerLogoPanel.class.getResourceAsStream(
					"/de/tisan/church/untertitelinator/resources/evko_broadcast_watermark_v1_small.png"));
			layerImage.setIcon(new ImageIcon(image));
			setBounds(0, 0, image.getWidth(), image.getHeight());
		} catch (IOException e1) {
			Logger.getInstance().err("Couldnt load Watermark image! " + e1.getMessage(), e1, getClass());
			
		}

		add(layerImage);

		fitImage(preferredSize.width, preferredSize.height);

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				fitImage(getWidth(), getHeight());
			}
		});

		repaint();
	}

	private void fitImage(int width, int height) {
		int safeBounds = 80;
		if (isVisible()) {
			int x = width - image.getWidth() - safeBounds;
			int y = safeBounds;
			layerImage.setBounds(x, y, image.getWidth(), image.getHeight());
		}
	}
}
