package de.tisan.church.untertitelinator.gui.keyer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tisan.church.untertitelinator.churchtools.api.objects.Event;
import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.CommandState;
import de.tisan.church.untertitelinator.instancer.packets.EventSelectionChangedPacket;
import de.tisan.church.untertitelinator.instancer.packets.GUIKeyerLayerChangePacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.instancer.packets.SongLinePacket;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;
import de.tisan.tisanapi.logger.Logger;
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
	protected Event event;

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

			UTEventHub.get().registerListener(new UTEventListener() {

				@Override
				public void onEventReceived(Packet packet) {
					if (packet instanceof CommandPacket) {
						CommandPacket sPacket = (CommandPacket) packet;
						CommandState state = null;
						switch (sPacket.getCommand()) {
						case STATE_KOLLEKTE:
							state = CommandState.convert(sPacket.getArgs());
							boolean newState = toggleKollekte(state);
							UTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.KOLLEKTE, newState ? CommandState.ON : CommandState.OFF));
							break;
						case STATE_LOGO:
							state = CommandState.convert(sPacket.getArgs());
							boolean newState1 = toggleLogo(state);
							UTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.LOGO, newState1 ? CommandState.ON : CommandState.OFF));
							break;
						case STATE_UNTERTITEL:
							state = CommandState.convert(sPacket.getArgs());
							boolean newState2 = toggleUntertitel(state);
							UTEventHub.get()
									.publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.UNTERTITEL, newState2 ? CommandState.ON : CommandState.OFF));
							break;
						case STATE_WINDOW_BAR:
							state = CommandState.convert(sPacket.getArgs());
							boolean newState3 = toggleWindowBar(state);
							UTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.MAXBUTTON, newState3 ? CommandState.ON : CommandState.OFF));
							break;
						case STATE_BEGIN_CARD:
							state = CommandState.convert(sPacket.getArgs());
							boolean newState4 = toggleBeginLayer(state);
							UTEventHub.get()
									.publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.BEGINLAYER, newState4 ? CommandState.ON : CommandState.OFF));
							break;
						case STATE_END_CARD:
							state = CommandState.convert(sPacket.getArgs());
							boolean newState5 = toggleEndcard(state);
							UTEventHub.get().publish(new GUIKeyerLayerChangePacket(GUIKeyerLayer.ENDCARD, newState5 ? CommandState.ON : CommandState.OFF));
							break;
						default:
							break;
						}
					} else if(packet instanceof EventSelectionChangedPacket) {
						EventSelectionChangedPacket p = (EventSelectionChangedPacket) packet;
						event = p.getEvent();
					}
				}
			});

		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			Logger.getInstance().err("Error while toggling Keyer Layers " + e.getMessage(), e, getClass());
		}
		
		UTEventHub.get().publish(new CommandPacket(Command.SEND_EVENT));
	}

	private boolean toggleBeginLayer(CommandState state) {
		pnlStartPage.setVisible(state == CommandState.ON);
		return pnlStartPage.isVisible();
	}

	private boolean toggleUntertitel(CommandState state) {
		pnlUntertitel.setVisible(state == CommandState.ON);
		return pnlUntertitel.isVisible();
	}

	private boolean toggleLogo(CommandState state) {
		pnlLogo.setVisible(state == CommandState.ON);
		return pnlLogo.isVisible();
	}

	private boolean toggleEndcard(CommandState state) {
		pnlEndcardPage.setVisible(state == CommandState.ON);
		return pnlEndcardPage.isVisible();
	}

	private boolean toggleWindowBar(CommandState state) {
		bar.setVisible(state == CommandState.ON);
		return bar.isVisible();

	}

	private boolean toggleKollekte(CommandState state) {
		if (state == CommandState.ON) {
			pnlKollekte.setVisible(true);
			String kollekteLine1 = JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERKOLLEKTELINE1,
					"Kollektenkonto: DE76 5006 1741 0000 0096 87", String.class);
			UTEventHub.get().publish(new CommandPacket(Command.SEND_EVENT));
			UTEventHub.get().publish(new SongLinePacket(kollekteLine1, "Verwendungszweck: 'Kollekte "
					+ (event != null ? event.getStartDayString() : "") + "'"));
			return true;
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					UTEventHub.get().publish(new SongLinePacket("", ""));
					pnlKollekte.setVisible(false);
				}
			}).start();
			return false;
		} 

	}

}
