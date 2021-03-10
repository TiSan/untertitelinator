package de.tisan.church.untertitelinator.gui.keyer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tisan.church.untertitelinator.churchtools.api.objects.Event;
import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.main.Loader;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
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
	Color bg;
	Color fg;
	private GUIKeyerLogoPanel pnlLogo;
	private GUIKeyerEndcardPanel pnlEndcardPage;
	private GUIKeyerUntertitelPanel pnlKollekte;

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

		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void showNewTextLines(String title, String line1, String line2, int delay, boolean paused) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
				pnlUntertitel.showNewTextLines(line1, line2);
			}
		}).start();
	}

	public void loadUi() {
		Event currentEvent = Untertitelinator.get().getCurrentEvent();
		String themaString = currentEvent.getName();

		// Gottesdienst-Titel aus "Info-Feld" lesen
		String titleString = currentEvent.getDescription();
		int indexEnter = titleString.indexOf("\n");
		if (indexEnter == -1) {
			indexEnter = titleString.length();
		}

		titleString = titleString.substring(0, indexEnter);

		// Fallback, wenn nicht gesetzt
		if (titleString.startsWith("Weitere Infos...")) {
			titleString = "Live-Gottesdienst";
		}

		pnlStartPage.showNextStream(titleString, "Thema: \"" + themaString + "\"",
				currentEvent.getStartDate().plusHours(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm"))
						+ " Uhr",
				Untertitelinator.get().getServiceList());

	}

	public boolean toggleBeginLayer() {
		if (pnlStartPage.isVisible()) {
			pnlStartPage.setVisible(false);
		} else {
			pnlStartPage.setVisible(true);
		}
		return pnlStartPage.isVisible();
	}
	
	public boolean isBeginLayerVisible() {
		return pnlStartPage.isVisible();
	}

	public boolean toggleUntertitel() {
		if (pnlUntertitel.isVisible()) {
			pnlUntertitel.setVisible(false);
			// bar.setBackground(new Color(0, 0, 0, 0));
		} else {
			pnlUntertitel.setVisible(true);
			// bar.setBackground(bg);
		}
		return pnlUntertitel.isVisible();
	}
	
	public boolean isUntertitelVisible() {
		return pnlUntertitel.isVisible();
	}

	public boolean toggleLogo() {
		if (pnlLogo.isVisible()) {
			pnlLogo.setVisible(false);
		} else {
			pnlLogo.setVisible(true);
		}
		return pnlLogo.isVisible();
	}

	public boolean isLogoVisible() {
		return pnlLogo.isVisible();
	}
	
	public boolean toggleEndcard() {
		if (pnlEndcardPage.isVisible()) {
			pnlEndcardPage.setVisible(false);
		} else {
			pnlEndcardPage.setVisible(true);
		}
		return pnlEndcardPage.isVisible();
	}
	
	public boolean isEndcardVisible() {
		return pnlEndcardPage.isVisible();
	}

	public boolean toggleWindowBar() {
		if (bar.isVisible()) {
			bar.setVisible(false);
		} else {
			bar.setVisible(true);
		}
		return bar.isVisible();

	}
	
	public boolean isWindowBarVisible() {
		return bar.isVisible();
	}

	public boolean toggleKollekte() {
		if (pnlKollekte.isVisible()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					pnlKollekte.showNewTextLines("", "", true);
					pnlKollekte.setVisible(false);
					Loader.getMainUi().updateUIComponents();
				}
			}).start();
		} else {
			pnlKollekte.setVisible(true);
			String kollekteLine1 = JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERKOLLEKTELINE1, "Kollektenkonto: DE76 5006 1741 0000 0096 87", String.class);
			pnlKollekte.showNewTextLines(kollekteLine1, "Verwendungszweck: 'Kollekte "
					+ Untertitelinator.get().getCurrentEvent().getStartDayString() + "'");
		}
		return pnlKollekte.isVisible();

	}
	
	public boolean isKollekteVisible() {
		return pnlKollekte.isVisible();
	}
}
