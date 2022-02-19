package de.tisan.church.untertitelinator.gui.keyer;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import de.tisan.flatui.components.fcommons.FlatUI;
import de.tisan.tisanapi.logger.Logger;

public class GUIKeyerMarqueePanel extends JPanel {

    String text;
    Integer offsetX = null;
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
                        if(instance.getPnlStandby().isVisible() == false){
                            repaint();
                        }
                        Thread.sleep(7);
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
            int width = this.getWidth();
            int x1 = (width - textWidth) / 2;
            textY = (height - textHeight) / 2 + fm.getAscent();
            textLength = textWidth;
            offsetX = getWidth();
        }

        g.setColor(getForeground());
        g.drawString(text, (int) offsetX, textY);
        offsetX-= 2;
        if(Math.abs(offsetX) - textLength >= 0){
            offsetX = getWidth();
        }
    }

    public void setText(String castTicker) {
        text = castTicker;
        textLength = null;
    }
}
