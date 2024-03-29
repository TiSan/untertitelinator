package de.tisan.church.untertitelinator.gui.main;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.UTInstance;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.ConnectionStatusPacket;
import de.tisan.church.untertitelinator.instancer.packets.KeepAlivePacket;
import de.tisan.church.untertitelinator.instancer.packets.MonitorListPacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.flisteners.MouseClickedHandler;
import de.tisan.flatui.components.flisteners.MouseDragHandler;
import de.tisan.flatui.components.flisteners.MouseEnterHandler;
import de.tisan.flatui.components.flisteners.MouseLeaveHandler;
import de.tisan.flatui.components.flisteners.MouseListener;
import de.tisan.flatui.components.flisteners.MouseMoveHandler;
import de.tisan.flatui.components.flisteners.MousePressHandler;
import de.tisan.flatui.components.flisteners.MouseReleaseHandler;
import de.tisan.flatui.components.flisteners.Priority;
import de.tisan.tisanapi.logger.Logger;

public class GUIMainConnectionStatePanel extends AGUIMainPanel {
    private static final long serialVersionUID = 8492314016575322257L;
    private FlatButton boxKeyer;
    private FlatButton boxPresentator;

    private long lastTsKeyer;
    private long lastTsPresentator;
    private boolean keyerOnline;
    private boolean presentatorOnline;


    public GUIMainConnectionStatePanel(FlatLayoutManager man, GUIMain instance, Dimension preferredSize) {
        super(man, instance, preferredSize);

        JLabel lblCurrentLine = new JLabel("Verbindungsstatus");
        lblCurrentLine.setFont(FlatFont.getInstance(14, Font.PLAIN));
        lblCurrentLine.setForeground(FlatColors.WHITE);
        lblCurrentLine.setBounds(0, 0, preferredSize.width, 20);
        add(lblCurrentLine);

        boxKeyer = new FlatButton("Keyer", man);
        boxKeyer.setBounds(0, 25, 100, 25);
        boxKeyer.setAnchor(Anchor.LEFT, Anchor.RIGHT);
        //boxKeyer.disableEffects();
        boxKeyer.setBackground(FlatColors.HIGHLIGHTBACKGROUND);
        boxKeyer.addMouseListener(Priority.NORMAL, new MouseListener() {
			
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				UTEventHub.get().publish(new CommandPacket(Command.KEYER_SEND_DISPLAYS));
			}
			
			@Override
			public void onMousePress(MousePressHandler handler) {
			}
			
			@Override
			public void onMouseMove(MouseMoveHandler handler) {
			}
			
			@Override
			public void onMouseLeave(MouseLeaveHandler handler) {
			}
			
			@Override
			public void onMouseEnter(MouseEnterHandler handler) {
			}
			
			@Override
			public void onMouseDrag(MouseDragHandler handler) {
			}
			
			@Override
			public void onMouseClicked(MouseClickedHandler handler) {
			}
		});
        add(boxKeyer);

        boxPresentator = new FlatButton("Presentator", man);
        boxPresentator.setBounds(105, 25, 100, 25);
        boxPresentator.setAnchor(Anchor.LEFT, Anchor.RIGHT);
        boxPresentator.setBackground(FlatColors.HIGHLIGHTBACKGROUND);
        //boxPresentator.disableEffects();
        boxPresentator.addMouseListener(Priority.NORMAL, new MouseListener() {
			
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				UTEventHub.get().publish(new CommandPacket(Command.PRESENTATOR_SEND_DISPLAYS));
			}
			
			@Override
			public void onMousePress(MousePressHandler handler) {
			}
			
			@Override
			public void onMouseMove(MouseMoveHandler handler) {
			}
			
			@Override
			public void onMouseLeave(MouseLeaveHandler handler) {
			}
			
			@Override
			public void onMouseEnter(MouseEnterHandler handler) {
			}
			
			@Override
			public void onMouseDrag(MouseDragHandler handler) {
			}
			
			@Override
			public void onMouseClicked(MouseClickedHandler handler) {
			}
		});
        add(boxPresentator);

        UTEventHub.get().registerListener(new UTEventListener() {

            @Override
            public void onEventReceived(Packet packet) {
                if (packet instanceof KeepAlivePacket) {
                    KeepAlivePacket p = (KeepAlivePacket) packet;
                    switch (p.getInstance()) {
                        case CONTROLLER:
                            break;
                        case KEYER:
                            lastTsKeyer = p.getTs();
                            keyerOnline = true;
                            boxKeyer.setBackground(FlatColors.RED);
                            break;
                        case PRESENTATOR:
                            lastTsPresentator = p.getTs();
                            presentatorOnline = true;
                            boxPresentator.setBackground(FlatColors.RED);
                            break;
                    }
                } else if(packet instanceof MonitorListPacket) {
                	MonitorListPacket monitorList = (MonitorListPacket) packet;
                	
                	new GUISelectMonitor(monitorList).setVisible(true);
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    	Logger.getInstance().err("Fehler beim Ticker für die Aufrechterhaltung der Verbindung", e, getClass());
                    }

                    long current = System.currentTimeMillis();

                    if(keyerOnline && current - lastTsKeyer > 4000){
                        keyerOnline = false;
                        boxKeyer.setBackground(FlatColors.HIGHLIGHTBACKGROUND);
                        UTEventHub.get().publish(new ConnectionStatusPacket(ConnectionStatusPacket.ConnectionType.SERVER, UTInstance.KEYER, false), false);
                    }

                    if(presentatorOnline && current - lastTsPresentator > 4000){
                        presentatorOnline = false;
                        boxPresentator.setBackground(FlatColors.HIGHLIGHTBACKGROUND);
                        UTEventHub.get().publish(new ConnectionStatusPacket(ConnectionStatusPacket.ConnectionType.SERVER, UTInstance.PRESENTATOR, false), false);
                    }

                }
            }
        }).start();
    }
}
