package de.tisan.church.untertitelinator.gui.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class GUIMain extends JFrame {

	private static final long serialVersionUID = 6255477384834005517L;

	public GUIMain() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
		}
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setUndecorated(true);
		setSize(930, 650);
		setResizable(false);

		setLocation((int) (dim.getWidth() / 2 - (getWidth() / 2)), (int) (dim.getHeight() / 2 - (getHeight() / 2)));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel contentPane = new JPanel();
		setContentPane(contentPane);

		contentPane.setLayout(null);
		contentPane.setBackground(FlatColors.BACKGROUND);

		FlatLayoutManager man = FlatLayoutManager.get(this);

		man.setResizable(false);
		FlatTitleBarWin10 bar = new FlatTitleBarWin10(man,
				(String) JSONPersistence.get().getSetting(UTPersistenceConstants.CHURCHNAME,
						"Evangelische Kirchengemeinde Oberstedten") + " - Untertitelinator v"
						+ Untertitelinator.VERSION);
		bar.setBounds(0, 0, getWidth(), 30);
		bar.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		bar.setMaximizable(false);
		bar.addFlatTitleBarListener(new DefaultFlatTitleBarListener(this));

		contentPane.add(bar);

		GUIMainControllerPanel pnlController = new GUIMainControllerPanel(man, this, new Dimension(625, 50));
		pnlController.setLocation(290, 50);
		contentPane.add(pnlController);

		GUIMainSongTextPanel pnlSongtext = new GUIMainSongTextPanel(man, this, new Dimension(625, 400));
		pnlSongtext.setLocation(290, 110);
		contentPane.add(pnlSongtext);

		GUIMainSongListPanel pnlSongList = new GUIMainSongListPanel(man, this, new Dimension(265, 500));
		pnlSongList.setLocation(10, 110);
		contentPane.add(pnlSongList);

		GUIMainKeyerPanel pnlKeyer = new GUIMainKeyerPanel(man, this, new Dimension(625, 110));
		pnlKeyer.setLocation(pnlSongList.getX() + pnlSongList.getWidth() + 10,
				pnlSongtext.getY() + pnlSongtext.getHeight() + 15);
		contentPane.add(pnlKeyer);

		GUIStartEndCardsPanel pnlStartEnd = new GUIStartEndCardsPanel(man, this, new Dimension(200, 585));
		pnlStartEnd.setLocation(10, 50);
		contentPane.add(pnlStartEnd);

		getAllComponents(this).forEach(c -> c.addKeyListener(new LukasWillsSoKeyListener()));
	}

	private static List<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}


}
