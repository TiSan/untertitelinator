package de.tisan.church.untertitelinator.gui.presentator;

import java.awt.*;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tisan.church.untertitelinator.data.Untertitelinator;
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
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class GUIPresentator extends JFrame {

    private static final long serialVersionUID = 7666681011188876592L;
    private static GUIPresentator instance;

    public static GUIPresentator get() {
        if (instance == null) {
            instance = new GUIPresentator();
//			instance.setVisible(true);
        }
        return instance;
    }

    private FlatButton currentLine1;
    private FlatButton currentLine2;
    private FlatButton nextLine1;
    private FlatButton nextLine2;
    private FlatButton titleLine;

    public GUIPresentator() {

        String useDisplay = (String) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIPRESENTATORDISPLAYID, "\\Display0");
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        GraphicsDevice display = devices[0];
        for (GraphicsDevice device : devices) {
            System.out.println("'" + device.getIDstring() + "'");
            if (useDisplay.equals(device.getIDstring())) {
                display = device;
                break;
            }
        }
        Rectangle displayRect = display.getDefaultConfiguration().getBounds();
        setUndecorated(true);
        setLocation(displayRect.getLocation());
        setSize(displayRect.getSize());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(FlatColors.BLACK);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        contentPane.setBackground(FlatColors.BLACK);
        FlatLayoutManager man = FlatLayoutManager.get(this);



        Font font = FlatFont.getInstance(195, Font.BOLD);
        int spaceY = 60;
        int spaceX = 30;
        int width = getWidth() - (spaceX * 2);
        int height = 250;

        titleLine = new FlatButton((String) JSONPersistence.get()
                .getSetting(UTPersistenceConstants.GUIPRESENTATORCURRENTTITLETEXT, "Aktueller Titel"), man);
        titleLine.setBounds(spaceX, 50, width, 150);
        titleLine.setFont(FlatFont.getInstance(60, Font.BOLD));
        titleLine.setBackground(FlatColors.BLACK);
        titleLine.setAnchor(Anchor.LEFT, Anchor.RIGHT);
        contentPane.add(titleLine);

        currentLine1 = new FlatButton("Aktuelle Zeile 1", man);
        currentLine1.setBounds(spaceX, titleLine.getY() + titleLine.getHeight() + spaceY, width, height);
        currentLine1.setFont(font);
        currentLine1.setBackground(FlatColors.BLACK);
        currentLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
        contentPane.add(currentLine1);

        currentLine2 = new FlatButton("Aktuelle Zeile 2", man);
        currentLine2.setBounds(spaceX, currentLine1.getY() + currentLine1.getHeight(), width, height);
        currentLine2.setFont(font);
        currentLine2.setBackground(FlatColors.BLACK);
        currentLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
        contentPane.add(currentLine2);

        height = 130;
        spaceY = 10;
        font = FlatFont.getInstance(100, Font.BOLD);
        Color fgColor = FlatColors.GRAY;

        nextLine1 = new FlatButton("N\u00E4chste Zeile 1", man);
        nextLine1.setBounds(spaceX, currentLine2.getY() + currentLine2.getHeight() + spaceY, width, height);
        nextLine1.setFont(font);
        nextLine1.setBackground(FlatColors.BLACK);
        nextLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
        nextLine1.setForeground(fgColor);
        contentPane.add(nextLine1);

        nextLine2 = new FlatButton("N\u00E4chste Zeile 2", man);
        nextLine2.setBounds(spaceX, nextLine1.getY() + nextLine1.getHeight() + spaceY, width, height);
        nextLine2.setFont(font);
        nextLine2.setBackground(FlatColors.BLACK);
        nextLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
        nextLine2.setForeground(fgColor);
        contentPane.add(nextLine2);

        UTEventHub.get().registerListener(new UTEventListener() {

            @Override
            public void onEventReceived(Packet packet) {
                if (packet instanceof SongLinePacket) {
                    SongLinePacket sPacket = (SongLinePacket) packet;
                    if (sPacket.getSongPlayer() != null) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                showNewTextLines(sPacket.getSongPlayer().getSong().getTitle(),
                                        sPacket.getCurrentLines().get(0),
                                        sPacket.getCurrentLines().get(1),
                                        sPacket.getNextLines().get(0),
                                        sPacket.getNextLines().get(1),
                                        0,
                                        sPacket.getCurrentLines()
                                                .stream()
                                                .filter(Objects::nonNull)
                                                .filter(e -> (e.length() > 0))
                                                .findFirst()
                                                .isPresent() == false
                                );
                            }
                        }).start();
                    }
                }
            }
        });

        man.disableAllEffects();
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
