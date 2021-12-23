package de.tisan.church.untertitelinator.gui.keyer;

import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tools.persistencemanager.JSONPersistence;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUIKeyerPictureRotationPanel extends JPanel {

    private static final long serialVersionUID = 5623162033757903847L;
    private final List<BufferedImage> imageList;
    private JLabel layerImage;

    public GUIKeyerPictureRotationPanel(FlatLayoutManager man, GUIKeyer instance, Dimension preferredSize) {
        setLayout(null);
        setOpaque(false);
        setBackground(FlatColors.BLACK);
        layerImage = new JLabel();
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                layerImage.setBounds(0, 0, getWidth(), getHeight());
            }
        });
        add(layerImage);
        imageList = loadImages();
        repaint();

        startRotationThread();
    }

    private void startRotationThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer waiting = JSONPersistence.get()
                        .getSetting(UTPersistenceConstants.IMAGEROTATIONTIMEOUT, 5000, Integer.class);
                int index = 0;
                while (true) {
                    if (isVisible()) {

                        if (index >= imageList.size()) {
                            index = 0;
                        }

                        BufferedImage img = imageList.get(index);
                        showImage(img);
                        index++;
                    }
                    try {
                        Thread.sleep(waiting);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();

    }

    private List<BufferedImage> loadImages() {
        String path = (String) JSONPersistence.get().getSetting(UTPersistenceConstants.IMAGEROTATIONPATH, "images");
        List<BufferedImage> imageList = new ArrayList<>();

        File directory = new File(path);
        if (directory.exists() && directory.isDirectory()) {
            imageList = Arrays
                    .stream(directory.listFiles(
                            (dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg")))
                    .map(file -> {
                        try {
                            return ImageIO.read(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());
        }

        return imageList;
    }

    private void showImage(BufferedImage image) {
        ImageIcon ico = new ImageIcon(image);
        double factor = getScaleFactorToFit(new Dimension(image.getWidth(), image.getHeight()), getSize());

        int scaledWidth = (int) (image.getWidth() * factor);
        int scaledHeight = (int) (image.getHeight() * factor);


        layerImage.setIcon(new ImageIcon(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH)));
    }


    private double getScaleFactorToFit(Dimension original, Dimension toFit) {
        double dScale = 1d;
        if (original != null && toFit != null) {
            double dScaleWidth = getScaleFactor(original.width, toFit.width);
            double dScaleHeight = getScaleFactor(original.height, toFit.height);
            dScale = Math.min(dScaleHeight, dScaleWidth);
        }
        return dScale;

    }

    private double getScaleFactor(int iMasterSize, int iTargetSize) {
        return (double) iTargetSize / (double) iMasterSize;
    }
}
