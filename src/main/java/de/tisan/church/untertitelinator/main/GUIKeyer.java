package de.tisan.church.untertitelinator.main;

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
import de.tisan.church.untertitelinator.settings.JSONPersistence;
import de.tisan.church.untertitelinator.settings.PersistenceConstants;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;

public class GUIKeyer extends JFrame {

	private static final long serialVersionUID = 7666681011188876592L;
	private static GUIKeyer instance;

	public static GUIKeyer get() {
		if (instance == null) {
			instance = new GUIKeyer();
//			instance.setVisible(true);
		}
		return instance;
	}

	private GUIKeyerUntertitelPanel pnlUntertitel;
	private GUIKeyerStartPagePanel pnlStartPage;
	private FlatTitleBarWin10 bar;
	Color bg;
	private GUIKeyerLogoPanel pnlLogo;
	private GUIKeyerEndcardPanel pnlEndcardPage;
	private GUIKeyerKollektePanel pnlKollekte;

	public GUIKeyer() {
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			screenSize = new Dimension(
					(Integer) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERWIDTH, 1024),
					(Integer) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERHEIGHT, 768));
			setUndecorated(true);
			setLocation((Integer) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERX, 0),
					(Integer) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERY, 0));
			setSize(screenSize);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			bg = (Color) Color.class
					.getField(
							(String) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERBACKGROUND, "GREEN"))
					.get(new Color(0));

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
			bar.setCloseable((boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERCLOSEABLE, false));
			bar.setMaximizable(
					(boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERMAXIMIZABLE, true));
			bar.setMinimizable(
					(boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERMINIMIZABLE, false));
			bar.setMoveable((boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERMOVEABLE, false));
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

			pnlKollekte = new GUIKeyerKollektePanel(man, this, screenSize);
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

	public void showNewTextLines(String title, String line1, String line2, String line3, String line4, int delay,
			boolean paused) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
				pnlUntertitel.showNewTextLines(line1, line2, line3, line4);
			}
		}).start();
	}

	public void loadUí() {
		Event currentEvent = Untertitelinator.get().getCurrentEvent();
		String titleName = currentEvent.getName();
		System.out.println(titleName);

		int indexEnter = currentEvent.getDescription().indexOf("\n");
		if (indexEnter == -1) {
			indexEnter = currentEvent.getDescription().length();
		}
		pnlStartPage
				.showNextStream(currentEvent.getDescription().substring(0, indexEnter), "Thema: \"" + titleName + "\"",
						currentEvent.getStartDate().plusHours(1)
								.format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm")) + " Uhr",
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

	public boolean toggleLogo() {
		if (pnlLogo.isVisible()) {
			pnlLogo.setVisible(false);
		} else {
			pnlLogo.setVisible(true);
		}
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

	public boolean toggleWindowBar() {
		if (bar.isVisible()) {
			bar.setVisible(false);
		} else {
			bar.setVisible(true);
		}
		return bar.isVisible();

	}

	public boolean toggleKollekte() {
		if (pnlKollekte.isVisible()) {
			pnlKollekte.setVisible(false);
		} else {
			pnlKollekte.setVisible(true);
		}
		return pnlKollekte.isVisible();

	}
}
