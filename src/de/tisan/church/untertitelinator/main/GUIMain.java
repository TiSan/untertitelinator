package de.tisan.church.untertitelinator.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.lang.reflect.Field;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.settings.JSONPersistence;
import de.tisan.church.untertitelinator.settings.PersistenceConstants;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.ficon.FlatIconFont;
import de.tisan.flatui.components.flatoptionpanes.FlatOptionPane;
import de.tisan.flatui.components.flisteners.ActionListener;
import de.tisan.flatui.components.flisteners.MouseClickedHandler;
import de.tisan.flatui.components.flisteners.MouseReleaseHandler;
import de.tisan.flatui.components.flisteners.Priority;
import de.tisan.flatui.components.ftextbox.FlatTextBox;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;

public class GUIMain extends JFrame {

	private static final long serialVersionUID = 6255477384834005517L;
	private Untertitelinator un;
	private FlatTextBox boxCurrentLine1;
	private FlatTextBox boxCurrentLine2;

	private SentenceModel sentenceModel;
	private JTable table;
	private JList<String> list;
	private FlatButton btnPause;

	public GUIMain(Untertitelinator un) {
		this.un = un;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		setUndecorated(true);
		setSize(800, 550);
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
				(String) JSONPersistence.get().getSetting(PersistenceConstants.CHURCHNAME,
						"Evangelische Kirchengemeinde Oberstedten") + " - Untertitelinator v"
						+ Untertitelinator.VERSION);
		bar.setBounds(0, 0, getWidth(), 30);
		bar.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		bar.setMaximizable(false);
		bar.addFlatTitleBarListener(new DefaultFlatTitleBarListener(this));

		contentPane.add(bar);

		DefaultListModel<String> model = new DefaultListModel<String>();

		list = new JList<String>(model);

		list.setBounds(10, 50, 200, getHeight() - 70);

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				changeSong(list.getSelectedIndex());

			}
		});
		contentPane.add(list);

		un.getSongs().stream().map(song -> song.getTitle()).forEach(model::addElement);

		FlatButton btnStart = new FlatButton(null, FlatIconFont.FAST_BACKWARD, man);
		btnStart.setBounds(230, 50, 70, 50);
		btnStart.addActionListener(Priority.NORMAL, new ActionListener() {
			
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				jumpToStart();
				
			}
			
			@Override
			public void onAction(MouseClickedHandler arg0) {
			}
		});

		contentPane.add(btnStart);

		FlatButton btnBack = new FlatButton(null, FlatIconFont.BACKWARD, man);
		btnBack.setBounds(310, 50, 70, 50);

		btnBack.addActionListener(Priority.NORMAL, new ActionListener() {
			
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				previousLine();
			}

			@Override
			public void onAction(MouseClickedHandler arg0) {
				
			}
		});

		contentPane.add(btnBack);

		btnPause = new FlatButton(null, FlatIconFont.PAUSE, man);
		btnPause.setBackground(FlatColors.ALIZARINRED);
		btnPause.setBounds(390, 50, 70, 50);
		btnPause.addActionListener(Priority.NORMAL, new ActionListener() {
			
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				pause();
				
			}
			
			@Override
			public void onAction(MouseClickedHandler arg0) {
			}
		});

		contentPane.add(btnPause);

		FlatButton btnForward = new FlatButton(null, FlatIconFont.FORWARD, man);
		btnForward.setBounds(470, 50, 70, 50);
		btnForward.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				nextLine();
				
			}
			
			@Override
			public void onAction(MouseClickedHandler arg0) {
			}
		});
		contentPane.add(btnForward);

		FlatButton btnEnd = new FlatButton(null, FlatIconFont.FAST_FORWARD, man);
		btnEnd.setBounds(550, 50, 70, 50);
		btnEnd.addActionListener(Priority.NORMAL, new ActionListener() {
			
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				jumpToEnd();
				
			}
			
			@Override
			public void onAction(MouseClickedHandler arg0) {
			}
		});

		contentPane.add(btnEnd);

		JLabel lblCurrentLine = new JLabel("Aktuell angezeigte Zeilen");
		lblCurrentLine.setFont(FlatFont.getInstance(18, Font.PLAIN));
		lblCurrentLine.setForeground(FlatColors.WHITE);
		lblCurrentLine.setBounds(230, 110, 550, 30);
		contentPane.add(lblCurrentLine);

		boxCurrentLine1 = new FlatTextBox(man);
		boxCurrentLine1.setBounds(230, 140, 550, 25);
		boxCurrentLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		boxCurrentLine1.setEditable(false);
		contentPane.add(boxCurrentLine1);

		boxCurrentLine2 = new FlatTextBox(man);
		boxCurrentLine2.setBounds(230, 165, 550, 25);
		boxCurrentLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		boxCurrentLine2.setEditable(false);
		contentPane.add(boxCurrentLine2);

		table = new JTable();
		sentenceModel = new SentenceModel(table);
		table.setModel(sentenceModel);

		JScrollPane scrollPane = new JScrollPane(table);

		scrollPane.setBounds(230, 200, 550, 300);
		contentPane.add(scrollPane);
		if (un.getSongs().isEmpty()) {
			FlatOptionPane errorPane = FlatOptionPane.getMessageDialog("Fehler",
					"Es sind aktuell noch keine Songs angelegt. Ohne einen Song kann Untertitelinator nicht gestartet werden. Bitte legen Sie die passenden Song-Dateien ins Song-Verzeichnis ab (befindet sich im gleichen Ordner wie die JAR).");
			errorPane.setAlwaysOnTop(true);
			errorPane.showDialog();
			errorPane.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
		changeSong(0);
		getAllComponents(this).forEach(this::registerKeyListener);

	}

	public static List<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}

	private void registerKeyListener(Component c) {
		c.addKeyListener(new LukasWillsSoKeyListener(this));
	}

	private void jumpToStart() {
		un.getCurrentPlayer().jumpToStart();
		updateUI();
	}

	private void jumpToEnd() {
		un.getCurrentPlayer().jumpToEnd();
		updateUI();
	}

	private void changeSong(int index) {
		if (un.getSongs().size() > index) {
			un.switchSong(un.getSongs().get(index));
			list.setSelectedIndex(index);
			updateUI();

		}
	}

	public void nextLine() {
		un.getCurrentPlayer().nextLine();
		updateUI();
	}

	public void previousLine() {
		un.getCurrentPlayer().previousLine();
		updateUI();
	}

	public void pause() {
		un.getCurrentPlayer().pause();
		updateUI();
		if (un.getCurrentPlayer().isPaused()) {
			btnPause.setBackground(FlatColors.GREEN);
			try {
				Field icon = btnPause.getClass().getDeclaredField("icon");
				icon.setAccessible(true);
				icon.set(btnPause, FlatIconFont.PLAY);
			} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			btnPause.setBackground(FlatColors.ALIZARINRED);
			try {
				Field icon = btnPause.getClass().getDeclaredField("icon");
				icon.setAccessible(true);
				icon.set(btnPause, FlatIconFont.PAUSE);
			} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

	private void updateUI() {
		sentenceModel.changeSong(un.getCurrentPlayer().getSong(), un.getCurrentPlayer().getCurrentIndex());
		String[] currentLines = un.getCurrentPlayer().getCurrentLine().split("\n", 2);
		String[] nextLines = un.getCurrentPlayer().getNextLine().split("\n", 2);

		boxCurrentLine1.setText(currentLines.length > 0 ? currentLines[0] : "");
		boxCurrentLine2.setText(currentLines.length > 1 ? currentLines[1] : "");

		sentenceModel.scrollToVisible(table, un.getCurrentPlayer().getCurrentIndex(), 0);

		GUIPresentator.get().showNewTextLines(un.getCurrentPlayer().getTitle(), currentLines[0],
				(currentLines.length > 1 ? currentLines[1] : ""), nextLines[0],
				(nextLines.length > 1 ? nextLines[1] : ""),
				(Integer) JSONPersistence.get().getSetting(PersistenceConstants.GUIPRESENTATORDELAY, 1200),
				un.getCurrentPlayer().isPaused());

		GUIKeyer.get().showNewTextLines(un.getCurrentPlayer().getTitle(), currentLines[0],
				(currentLines.length > 1 ? currentLines[1] : ""), nextLines[0],
				(nextLines.length > 1 ? nextLines[1] : ""),
				(Integer) JSONPersistence.get().getSetting(PersistenceConstants.GUIPRESENTATORDELAY, 1200),
				un.getCurrentPlayer().isPaused());

	}

}
