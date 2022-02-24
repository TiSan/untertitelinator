package de.tisan.church.untertitelinator.gui.keyer;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.tisan.flatui.components.fcommons.FlatUI;
import de.tisan.tisanapi.logger.Logger;

public class GUIKeyerMarqueePanel extends JPanel {

    String text;
    int offsetX;
    Integer textLength = null;
    int textY;

    public GUIKeyerMarqueePanel(String text, GUIKeyer instance) {
        this.text = text;
        
        setOpaque(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    while (true) {
                        if(instance.getPnlStandby().isVisible() == true || instance.getPnlStartPage().isVisible() == false){
                        } else {
                        	SwingUtilities.updateComponentTreeUI(instance);
          
                        }
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                	Logger.getInstance().err("Fehler beim Ticker für das MarqueePanel", e, getClass());
                }
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g2) {
        super.paintComponent(g2);
        Graphics2D g = (Graphics2D)g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, !FlatUI.isFastRendering() ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);


        if(textLength == null){
            FontMetrics fm = g.getFontMetrics(getFont());
            Rectangle2D rect = fm.getStringBounds(this.text, g);
            int textHeight = (int) rect.getHeight();
            int textWidth = (int) rect.getWidth();
            int height = this.getHeight();
            textY = (height - textHeight) / 2 + fm.getAscent();
            textLength = textWidth;
            offsetX = getWidth();
        }

        g.setColor(getForeground());
        g.drawString(text, offsetX, textY);
        offsetX-= 5;
        if(offsetX <= 0 - textLength){
            offsetX = getWidth();
        }
    }

    public void setText(String castTicker) {
        text = castTicker;
        textLength = null;
    }
}
