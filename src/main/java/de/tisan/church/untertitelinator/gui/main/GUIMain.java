package de.tisan.church.untertitelinator.gui.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.gui.keyer.GUIKeyer;
import de.tisan.church.untertitelinator.gui.presentator.GUIPresentator;
import de.tisan.church.untertitelinator.main.Loader;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.ftextbox.FlatTextBox;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class GUIMain extends JFrame
{

	private static final long serialVersionUID = 6255477384834005517L;
	private FlatTextBox boxCurrentLine1;
	private FlatTextBox boxCurrentLine2;

	private SentenceModel sentenceModel;
	private JTable table;

	public GUIMain()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
		        | UnsupportedLookAndFeelException e1)
		{
			e1.printStackTrace();
		}
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// Dies ist ein Test.
		setUndecorated(true);
		setSize(880, 650);
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


		

		JLabel lblCurrentLine = new JLabel("Aktuell angezeigte Zeilen");
		lblCurrentLine.setFont(FlatFont.getInstance(18, Font.PLAIN));
		lblCurrentLine.setForeground(FlatColors.WHITE);
		lblCurrentLine.setBounds(230, 110, 550, 30);
		contentPane.add(lblCurrentLine);

		boxCurrentLine1 = new FlatTextBox(man);
		boxCurrentLine1.setBounds(230, 140, 625, 25);
		boxCurrentLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		boxCurrentLine1.setEditable(false);
		contentPane.add(boxCurrentLine1);

		boxCurrentLine2 = new FlatTextBox(man);
		boxCurrentLine2.setBounds(230, 165, 625, 25);
		boxCurrentLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		boxCurrentLine2.setEditable(false);
		contentPane.add(boxCurrentLine2);

		table = new JTable();
		sentenceModel = new SentenceModel(table);
		table.setModel(sentenceModel);

		JScrollPane scrollPane = new JScrollPane(table);

		scrollPane.setBounds(230, 200, 625, 300);
		contentPane.add(scrollPane);

		
		GUIMainControllerPanel pnlController = new GUIMainControllerPanel(man, this, new Dimension(625, 50));
		pnlController.setLocation(230, 50);
		contentPane.add(pnlController);

		GUIMainSongListPanel pnlSongList = new GUIMainSongListPanel(man, this, new Dimension(200, 590));
		pnlSongList.setLocation(10, 50);
		contentPane.add(pnlSongList);

		GUIMainKeyerPanel pnlKeyer = new GUIMainKeyerPanel(man, this, new Dimension(625, 110));
		pnlKeyer.setLocation(pnlSongList.getX() + pnlSongList.getWidth() + 20, scrollPane.getY() + scrollPane.getHeight() + 20);
		contentPane.add(pnlKeyer);
		
		
		getAllComponents(this).forEach(this::registerKeyListener);

	}

	public void loadSongs()
	{
		
	}

	public static List<Component> getAllComponents(final Container c)
	{
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps)
		{
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}

	private void registerKeyListener(Component c)
	{
		c.addKeyListener(new LukasWillsSoKeyListener(this));
	}

	public void updateUIComponents()
	{
		getAllComponents(this).parallelStream().filter(c -> c instanceof AGUIMainPanel).map(AGUIMainPanel.class::cast)
		        .forEach(AGUIMainPanel::updateThisComponent);

		sentenceModel.changeSong(Untertitelinator.get().getCurrentPlayer().getSong(),
		        Untertitelinator.get().getCurrentPlayer().getCurrentIndex());
		String[] currentLines = Untertitelinator.get().getCurrentPlayer().getCurrentLine().split("\n", 2);
		String[] nextLines = Untertitelinator.get().getCurrentPlayer().getNextLine().split("\n", 2);

		boxCurrentLine1.setText(currentLines.length > 0 ? currentLines[0] : "");
		boxCurrentLine2.setText(currentLines.length > 1 ? currentLines[1] : "");

		sentenceModel.scrollToVisible(table, Untertitelinator.get().getCurrentPlayer().getCurrentIndex(), 0);

		GUIPresentator.get().showNewTextLines(Untertitelinator.get().getCurrentPlayer().getTitle(), currentLines[0],
		        (currentLines.length > 1 ? currentLines[1] : ""), nextLines[0],
		        (nextLines.length > 1 ? nextLines[1] : ""),
		        (Integer) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIPRESENTATORDELAY, 1200),
		        Untertitelinator.get().getCurrentPlayer().isPaused());

		GUIKeyer.get().showNewTextLines(Untertitelinator.get().getCurrentPlayer().getTitle(), currentLines[0],
		        (currentLines.length > 1 ? currentLines[1] : ""),
		        (Integer) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIPRESENTATORDELAY, 1200),
		        Untertitelinator.get().getCurrentPlayer().isPaused());

	}

}
