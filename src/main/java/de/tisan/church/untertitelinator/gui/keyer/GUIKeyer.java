package de.tisan.church.untertitelinator.gui.keyer;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tisan.church.untertitelinator.churchtools.api.objects.Event;
import de.tisan.church.untertitelinator.gui.common.GUIStandbyPanel;
import de.tisan.church.untertitelinator.instancer.*;
import de.tisan.church.untertitelinator.instancer.client.UTInstanceClient;
import de.tisan.church.untertitelinator.instancer.packets.*;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;
import de.tisan.tisanapi.logger.Logger;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class GUIKeyer extends JFrame {

    private static GUIKeyer instance;
    public static GUIKeyer get() {
        if (instance == null) {
            instance = new GUIKeyer();
        }
        return instance;
    }



    private GUIKeyerUntertitelPanel pnlUntertitel;
    private GUIKeyerStartPagePanel pnlStartPage;
    private GUIKeyerLogoPanel pnlLogo;
    private GUIKeyerEndcardPanel pnlEndcardPage;
    private GUIKeyerUntertitelPanel pnlKollekte;
    private GUIStandbyPanel pnlStandby;
    private GUIKeyerPictureRotationPanel pnlPictureRotation;

    Color bg;
    Color fg;
    protected Event event;

    public GUIKeyer() {
        try {
            String useDisplay = (String) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERDISPLAYID, "\\Display1");
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
            bg = (Color) Color.class.getField(
                            (String) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERBACKGROUND, "GREEN"))
                    .get(new Color(0));
            fg = Color.white;
            setBackground(bg);

            JPanel contentPane = new JPanel();
            contentPane.setLayout(null);
            setContentPane(contentPane);
            contentPane.setBackground(bg);

            FlatLayoutManager man = FlatLayoutManager.get(this);
            man.disableAllEffects();

            pnlStandby = new GUIStandbyPanel(man, getSize());
            pnlStandby.setBounds(0, 0, getWidth(), getHeight());
            contentPane.add(pnlStandby);

            pnlLogo = new GUIKeyerLogoPanel(man, this, getSize());
            pnlLogo.setBounds(0, 0, getWidth(), getHeight());
            contentPane.add(pnlLogo);

            pnlPictureRotation = new GUIKeyerPictureRotationPanel(man, this, getSize());
            pnlPictureRotation.setBounds(0, 0, getWidth(), getHeight());
            contentPane.add(pnlPictureRotation);
            pnlPictureRotation.setVisible(false);

            pnlStartPage = new GUIKeyerStartPagePanel(man, this, getSize());
            pnlStartPage.setBounds(0, 0, getWidth(), getHeight());
            contentPane.add(pnlStartPage);

            pnlEndcardPage = new GUIKeyerEndcardPanel(man, this, getSize());
            pnlEndcardPage.setBounds(0, 0, getWidth(), getHeight());
            contentPane.add(pnlEndcardPage);
            pnlEndcardPage.setVisible(false);

            pnlKollekte = new GUIKeyerUntertitelPanel(man, this, getSize());
            pnlKollekte.setBounds(0, 0, getWidth(), getHeight());
            contentPane.add(pnlKollekte);
            pnlKollekte.setVisible(false);

            pnlUntertitel = new GUIKeyerUntertitelPanel(man, this, getSize());
            pnlUntertitel.setBounds(0, 0, getWidth(), getHeight());
            contentPane.add(pnlUntertitel);
            pnlUntertitel.setVisible(false);

            addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent componentEvent) {
                    pnlUntertitel.setBounds(0, 0, getWidth(), getHeight());
                    pnlStartPage.setBounds(0, 0, getWidth(), getHeight());
                    pnlEndcardPage.setBounds(0, 0, getWidth(), getHeight());
                    pnlKollekte.setBounds(0, 0, getWidth(), getHeight());
                    pnlLogo.setBounds(0, 0, getWidth(), getHeight());
                    pnlStandby.setBounds(0, 0, getWidth(), getHeight());
                    pnlPictureRotation.setBounds(0, 0, getWidth(), getHeight());
                }
            });


            UTEventHub.get().registerListener(new UTEventListener() {

                @Override
                public void onEventReceived(Packet packet) {
                    if (packet instanceof CommandPacket) {
                        switch (((CommandPacket) packet).getCommand()) {
                            case TOGGLE_KOLLEKTE:
                                boolean newState = toggleKollekte();
                                UTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.KOLLEKTE, newState));
                                break;
                            case TOGGLE_LOGO:
                                boolean newState1 = toggleLogo();
                                UTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.LOGO, newState1));
                                break;
                            case TOGGLE_UNTERTITEL:
                                boolean newState2 = toggleUntertitel();
                                UTEventHub.get()
                                        .publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.UNTERTITEL, newState2));
                                break;
                            case TOGGLE_BEGIN_LAYER:
                                boolean newState4 = toggleBeginLayer();
                                UTEventHub.get()
                                        .publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.BEGINLAYER, newState4));
                                break;
                            case TOGGLE_ENDCARD:
                                boolean newState5 = toggleEndcard();
                                UTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.ENDCARD, newState5));
                                break;
                            case TOGGLE_PICTURE_ROTATION:
                                boolean newState6 = togglePictureRotation();
                                UTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.PICTURE_ROTATION, newState6));
                            default:
                                break;
                        }
                    } else if (packet instanceof EventSelectionChangedPacket) {
                        EventSelectionChangedPacket p = (EventSelectionChangedPacket) packet;
                        event = p.getEvent();
                    } else if (packet instanceof ConnectionStatusPacket) {
                        ConnectionStatusPacket p = (ConnectionStatusPacket) packet;
                        if (p.getModule().equals(UTInstance.KEYER)) {
                            pnlStandby.setVisible(!p.isConnected());
                        }
                    }
                }
            });

        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            Logger.getInstance().err("Error while toggling Keyer Layers " + e.getMessage(), e, getClass());
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    repaint();
                    UTEventHub.get()
                            .publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.ENDCARD, pnlEndcardPage.isVisible()));
                    UTEventHub.get()
                            .publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.BEGINLAYER, pnlStartPage.isVisible()));
                    UTEventHub.get()
                            .publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.KOLLEKTE, pnlKollekte.isVisible()));
                    UTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.LOGO, pnlLogo.isVisible()));
                    UTEventHub.get().publish(
                            new GUIKeyerLayerChangePacket(GUIKeyerLayer.UNTERTITEL, pnlUntertitel.isVisible()));
                    UTEventHub.get().publish(
                            new GUIKeyerLayerChangePacket(GUIKeyerLayer.PICTURE_ROTATION, pnlPictureRotation.isVisible()));

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Logger.getInstance().err("Timer for Refreshing Keyer Panes is stopped! " + e.getMessage(), e, getClass());

                    }
                }
            }
        }).start();
    }

    private boolean toggleBeginLayer() {
        if (pnlStartPage.isVisible()) {
            pnlStartPage.setVisible(false);
        } else {
            pnlStartPage.setVisible(true);
        }
        return pnlStartPage.isVisible();
    }

    private boolean toggleUntertitel() {
        if (pnlUntertitel.isVisible()) {
            pnlUntertitel.setVisible(false);
        } else {
            pnlUntertitel.setVisible(true);
        }
        return pnlUntertitel.isVisible();
    }

    private boolean toggleLogo() {
        if (pnlLogo.isVisible()) {
            pnlLogo.setVisible(false);
        } else {
            pnlLogo.setVisible(true);
        }
        return pnlLogo.isVisible();
    }

    private boolean toggleEndcard() {
        if (pnlEndcardPage.isVisible()) {
            pnlEndcardPage.setVisible(false);
        } else {
            pnlEndcardPage.setVisible(true);
        }
        return pnlEndcardPage.isVisible();
    }

    private boolean togglePictureRotation() {
        if (pnlPictureRotation.isVisible()) {
            pnlPictureRotation.setVisible(false);
        } else {
            pnlPictureRotation.setVisible(true);
        }
        return pnlPictureRotation.isVisible();
    }

    private boolean toggleKollekte() {
        if (pnlKollekte.isVisible()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    UTEventHub.get().publish(new SongLinePacket("", ""));
                    pnlKollekte.setVisible(false);
                }
            }).start();
            return !pnlKollekte.isVisible();
        } else {
            pnlKollekte.setVisible(true);
            String kollekteLine1 = JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERKOLLEKTELINE1,
                    "Kollektenkonto: DE76 5006 1741 0000 0096 87", String.class);
            UTEventHub.get().publish(new CommandPacket(Command.SEND_EVENT));
            UTEventHub.get().publish(new SongLinePacket(kollekteLine1, "Verwendungszweck: 'Kollekte "
                    + (event != null ? event.getStartDayString() : "") + "'"));
        }
        return pnlKollekte.isVisible();

    }

    public GUIStandbyPanel getPnlStandby() {
        return pnlStandby;
    }
}
