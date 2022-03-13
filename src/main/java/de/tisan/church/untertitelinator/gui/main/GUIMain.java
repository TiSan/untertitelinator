package de.tisan.church.untertitelinator.gui.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
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
import de.tisan.flatui.components.ficon.FlatIcon;
import de.tisan.flatui.components.fmenu.FlatMenu;
import de.tisan.flatui.components.fmenu.FlatMenuActionListener;
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
		setSize(930, 700);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/de/tisan/church/untertitelinator/resources/ut_icon4.png")));

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
						+ (Untertitelinator.VERSION == null ? " DEV (only for internal purposes)" : Untertitelinator.VERSION));
		bar.setBounds(0, 0, getWidth(), 30);
		bar.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		bar.setMaximizable(false);
		bar.setBackground(FlatColors.BLACK);
		bar.addFlatTitleBarListener(new DefaultFlatTitleBarListener(this));

		contentPane.add(bar);

		FlatMenu menu = new FlatMenu(man);
		menu.setBounds(0, bar.getHeight(), getWidth(), 30);
		menu.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		menu.addMenuPoint("Einstellungen", FlatIcon.COGS, new FlatMenuActionListener() {
			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
				bar.hideMenu();
			}
		});
		bar.setOptionMenu(menu);
		contentPane.add(menu);


		GUIMainOverview pnlOverview = new GUIMainOverview(man, this, new Dimension(getWidth(), getHeight() - 30));
		pnlOverview.setBounds(0, 50, getWidth() - 10, getHeight() - 60);
		contentPane.add(pnlOverview);

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				pnlOverview.setBounds(0, 50, getWidth(), getHeight() - 50);
			}});

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
