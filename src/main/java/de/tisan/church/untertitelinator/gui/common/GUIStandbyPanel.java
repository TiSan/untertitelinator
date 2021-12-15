package de.tisan.church.untertitelinator.gui.common;

import de.tisan.church.untertitelinator.gui.keyer.GUIKeyer;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.tisanapi.logger.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class GUIStandbyPanel extends JPanel {

    private final Dimension size;
    private JLabel layerImage;
    private BufferedImage image;
    private Random rnd;

    public GUIStandbyPanel(FlatLayoutManager man, GUIKeyer instance, Dimension preferredSize) {
        setLayout(null);
        setOpaque(true);
        layerImage = new JLabel();
        size = preferredSize;
        rnd = new Random();
        setBackground(FlatColors.BLACK);
        try {
            image = ImageIO.read(
                    GUIKeyer.class.getResourceAsStream("/de/tisan/church/untertitelinator/resources/ut_logo_v1_white_small.png"));
        } catch (IOException e1) {
            Logger.getInstance().err("Couldnt load Standby logo! " + e1.getMessage(), e1, getClass());
        }
        layerImage.setIcon(new ImageIcon(image));
        layerImage.setBounds(0, 0, image.getWidth(), image.getHeight());
        add(layerImage);

        repaint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    changeLocation();
                }
            }
        }).start();
    }

    private void changeLocation() {
        int x = rnd.nextInt(size.width - image.getWidth());
        int y = rnd.nextInt(size.height - image.getHeight());

        layerImage.setLocation(x, y);
        System.out.println("x: " + x + "; y: " + y);
    }

}
