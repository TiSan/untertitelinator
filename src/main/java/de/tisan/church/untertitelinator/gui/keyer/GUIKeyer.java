package de.tisan.church.untertitelinator.gui.keyer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.CTEventListener;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.GUIKeyerLayerChangePacket;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.SongLinePacket;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.UIRefreshPacket;
import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class GUIKeyer extends JFrame {

	private static final long serialVersionUID = 7666681011188876592L;
	private static GUIKeyer instance;

	public static GUIKeyer get() {
		if (instance == null) {
			instance = new GUIKeyer();
		}
		return instance;
	}

	private GUIKeyerUntertitelPanel pnlUntertitel;
	private GUIKeyerStartPagePanel pnlStartPage;
	private FlatTitleBarWin10 bar;
	private GUIKeyerLogoPanel pnlLogo;
	private GUIKeyerEndcardPanel pnlEndcardPage;
	private GUIKeyerUntertitelPanel pnlKollekte;

	Color bg;
	Color fg;

	public GUIKeyer() {
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			screenSize = new Dimension(
					(Integer) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERWIDTH, 1024),
					(Integer) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERHEIGHT, 768));
			setUndecorated(true);
			setLocation((Integer) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERX, 0),
					(Integer) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERY, 0));
			setSize(screenSize);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

			bar = new FlatTitleBarWin10(man, "");
			bar.addFlatTitleBarListener(new FlatTitleBarListener() {

				@Override
				public void onWindowDragged() {
				}

				@Override
				public void onMinimizeButtonReleased() {
				}

				@Override
				public void onMinimizeButtonPressed() {
				}

				@Override
				public void onMinimizeButtonMouseMove() {
				}

				@Override
				public void onMaximizeButtonReleased() {
					if ((GUIKeyer.this.getExtendedState() == Frame.MAXIMIZED_BOTH) == false) {
						bar.setVisible(false);
					}
					CTEventHub.get().publish(new UIRefreshPacket());
				}

				@Override
				public void onMaximizeButtonPressed() {
				}

				@Override
				public void onMaximizeButtonMouseMove() {
				}

				@Override
				public void onImageClicked() {
				}

				@Override
				public void onCloseButtonReleased() {
				}

				@Override
				public void onCloseButtonPressed() {
				}

				@Override
				public void onCloseButtonMouseMove() {
				}
			});
			bar.setOptionMenuToggleEnabled(false);
			bar.setBounds(0, 0, getWidth(), 30);
			bar.setBackground(new Color(0, 0, 0, 0));
			bar.disableEffects();
			contentPane.add(bar);
			bar.setOpaque(true);
			bar.addFlatTitleBarListener(new DefaultFlatTitleBarListener(this));
			bar.setCloseable(
					(boolean) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERCLOSEABLE, false));
			bar.setMaximizable(
					(boolean) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERMAXIMIZABLE, true));
			bar.setMinimizable(
					(boolean) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERMINIMIZABLE, false));
			bar.setMoveable((boolean) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERMOVEABLE, false));
			bar.setAnchor(Anchor.LEFT, Anchor.RIGHT);

			pnlLogo = new GUIKeyerLogoPanel(man, this, screenSize);
			pnlLogo.setBounds(0, 0, getWidth(), getHeight());
			contentPane.add(pnlLogo);

			pnlStartPage = new GUIKeyerStartPagePanel(man, this, screenSize);
			pnlStartPage.setBounds(0, 0, getWidth(), getHeight());
			contentPane.add(pnlStartPage);

			pnlEndcardPage = new GUIKeyerEndcardPanel(man, this, screenSize);
			pnlEndcardPage.setBounds(0, 0, getWidth(), getHeight());
			contentPane.add(pnlEndcardPage);
			pnlEndcardPage.setVisible(false);

			pnlKollekte = new GUIKeyerUntertitelPanel(man, this, screenSize);
			pnlKollekte.setBounds(0, 0, getWidth(), getHeight());
			contentPane.add(pnlKollekte);
			pnlKollekte.setVisible(false);

			pnlUntertitel = new GUIKeyerUntertitelPanel(man, this, screenSize);
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
				}
			});

			CTEventHub.get().registerListener(new CTEventListener() {

				@Override
				public void onEventReceived(Packet packet) {
					if (packet instanceof CommandPacket) {
						switch (((CommandPacket) packet).getCommand()) {
						case TOGGLE_KOLLEKTE:
							boolean newState = toggleKollekte();
							CTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.KOLLEKTE, newState));
							CTEventHub.get().publish(new UIRefreshPacket());
							break;
						case TOGGLE_LOGO:
							boolean newState1 = toggleLogo();
							CTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.LOGO, newState1));
							CTEventHub.get().publish(new UIRefreshPacket());
							break;
						case TOGGLE_UNTERTITEL:
							boolean newState2 = toggleUntertitel();
							CTEventHub.get()
									.publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.UNTERTITEL, newState2));
							CTEventHub.get().publish(new UIRefreshPacket());
							break;
						case TOGGLE_WINDOW_BAR:
							boolean newState3 = toggleWindowBar();
							CTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.MAXBUTTON, newState3));
							CTEventHub.get().publish(new UIRefreshPacket());
							break;
						case TOGGLE_BEGIN_LAYER:
							boolean newState4 = toggleBeginLayer();
							CTEventHub.get()
									.publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.BEGINLAYER, newState4));
							CTEventHub.get().publish(new UIRefreshPacket());
							break;
						case TOGGLE_ENDCARD:
							boolean newState5 = toggleEndcard();
							CTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.ENDCARD, newState5));
							CTEventHub.get().publish(new UIRefreshPacket());
							break;
						default:
							break;
						}
					}
				}
			});

		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					repaint();
					CTEventHub.get()
							.publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.ENDCARD, pnlEndcardPage.isVisible()));
					CTEventHub.get()
							.publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.BEGINLAYER, pnlStartPage.isVisible()));
					CTEventHub.get()
							.publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.KOLLEKTE, pnlKollekte.isVisible()));
					CTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.LOGO, pnlLogo.isVisible()));
					CTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.MAXBUTTON, bar.isVisible()));
					CTEventHub.get().publish(
							new GUIKeyerLayerChangePacket(GUIKeyerLayer.UNTERTITEL, pnlUntertitel.isVisible()));

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
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
			// bar.setBackground(new Color(0, 0, 0, 0));
		} else {
			pnlUntertitel.setVisible(true);
			// bar.setBackground(bg);
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

	private boolean toggleWindowBar() {
		if (bar.isVisible()) {
			bar.setVisible(false);
		} else {
			bar.setVisible(true);
		}
		return bar.isVisible();

	}

	private boolean toggleKollekte() {
		if (pnlKollekte.isVisible()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					CTEventHub.get().publish(new SongLinePacket("", ""));
					pnlKollekte.setVisible(false);
					CTEventHub.get().publish(new UIRefreshPacket());
				}
			}).start();
			return !pnlKollekte.isVisible();
		} else {
			pnlKollekte.setVisible(true);
			String kollekteLine1 = JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERKOLLEKTELINE1,
					"Kollektenkonto: DE76 5006 1741 0000 0096 87", String.class);
			CTEventHub.get().publish(new SongLinePacket(kollekteLine1, "Verwendungszweck: 'Kollekte "
					+ Untertitelinator.get().getCurrentEvent().getStartDayString() + "'"));
			CTEventHub.get().publish(new UIRefreshPacket());
		}
		return pnlKollekte.isVisible();

	}

}
