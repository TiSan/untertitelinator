package de.tisan.church.untertitelinator.gui.presentator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tisan.church.untertitelinator.gui.common.GUIStandbyPanel;
import de.tisan.church.untertitelinator.gui.player.GUIVideoPanel;
import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.UTInstance;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.ConnectionStatusPacket;
import de.tisan.church.untertitelinator.instancer.packets.Monitor;
import de.tisan.church.untertitelinator.instancer.packets.MonitorListPacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class GUIPresentator extends JFrame {

	private static final long serialVersionUID = 7666681011188876592L;
	private static GUIPresentator instance;
	private final GUIStandbyPanel pnlStandby;
	Color bg = FlatColors.BLACK;
	private GUIPresentatorUntertitelPanel pnlUntertitel;
	private GUIVideoPanel pnlVideo;
	
	
	public static GUIPresentator get() {
		if (instance == null) {
			instance = new GUIPresentator();
		}
		return instance;
	}

	

	public GUIPresentator() {

		String useDisplay = (String) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIPRESENTATORDISPLAYID,
				"\\Display0");
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		GraphicsDevice display = devices[0];
		for (GraphicsDevice device : devices) {
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
		setBackground(bg);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);
		contentPane.setBackground(bg);

		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		getContentPane().setCursor(blankCursor);

		FlatLayoutManager man = FlatLayoutManager.get(this);
		man.setResizable(false);

		pnlStandby = new GUIStandbyPanel(man, getSize());
		pnlStandby.setBounds(0, 0, getWidth(), getHeight());
		contentPane.add(pnlStandby);
		
		pnlVideo = new GUIVideoPanel(man, getSize());
		pnlVideo.setBounds(0, 0, getWidth(), getHeight());
		contentPane.add(pnlVideo);
		pnlVideo.setVisible(false);
		
		pnlUntertitel = new GUIPresentatorUntertitelPanel(man, this, getSize());
		pnlUntertitel.setBounds(0, 0, getWidth(), getHeight());
		contentPane.add(pnlUntertitel);

		UTEventHub.get().registerListener(new UTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				
				if (packet instanceof ConnectionStatusPacket) {
					ConnectionStatusPacket p = (ConnectionStatusPacket) packet;
					if (p.getModule().equals(UTInstance.PRESENTATOR)) {
						pnlStandby.setVisible(!p.isConnected());
					}
				} else if (packet instanceof CommandPacket) {
					CommandPacket sPacket = (CommandPacket) packet;
					switch (sPacket.getCommand()) {
						case TOGGLE_VIDEO:
							toggleVideo();
							break;
						case PRESENTATOR_SEND_DISPLAYS:
							try {
								GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment()
										.getScreenDevices();

								List<Monitor> monitorList = new ArrayList<Monitor>();
								Robot r = new Robot();
								for (GraphicsDevice device : devices) {
									Monitor mon = new Monitor();
									mon.setName(device.getIDstring());

									ByteArrayOutputStream bos = new ByteArrayOutputStream();
									ImageIO.write(r.createScreenCapture(device.getDefaultConfiguration().getBounds()),
											"jpg", bos);
									mon.setImage(bos.toByteArray());
									mon.setBounds(new int[] { device.getDefaultConfiguration().getBounds().width,
											device.getDefaultConfiguration().getBounds().height });
									monitorList.add(mon);
								}
								MonitorListPacket mlPacket = new MonitorListPacket(monitorList, UTInstance.PRESENTATOR);
								UTEventHub.get().publish(mlPacket);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case PRESENTATOR_CHANGE_DISPLAY:
							
							GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
							GraphicsDevice display = devices[0];
							for (GraphicsDevice device : devices) {

								if (sPacket.getArgs().get(0).equals(device.getIDstring())) {
									display = device;
									break;
								}
							}
							Rectangle displayRect = display.getDefaultConfiguration().getBounds();
							setLocation(displayRect.getLocation());
							setSize(displayRect.getSize());
							break;
						default:
							break;
					}
				}
			}
		});
			

		man.disableAllEffects();
	}
	private boolean toggleVideo() {
		if (pnlVideo.isVisible()) {
			pnlVideo.setVisible(false);
		} else {
			pnlVideo.setVisible(true);
		}
		return pnlVideo.isVisible();
	}
	
}
