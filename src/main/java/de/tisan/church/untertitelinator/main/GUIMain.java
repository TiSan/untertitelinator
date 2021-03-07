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
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.ficon.FlatIconFont;
import de.tisan.flatui.components.flisteners.ActionListener;
import de.tisan.flatui.components.flisteners.MouseClickedHandler;
import de.tisan.flatui.components.flisteners.MouseReleaseHandler;
import de.tisan.flatui.components.flisteners.Priority;
import de.tisan.flatui.components.ftextbox.FlatTextBox;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class GUIMain extends JFrame {

	private static final long serialVersionUID = 6255477384834005517L;
	private FlatTextBox boxCurrentLine1;
	private FlatTextBox boxCurrentLine2;

	private SentenceModel sentenceModel;
	private JTable table;
	private JList<String> list;
	private FlatButton btnPause;
	private DefaultListModel<String> songListModel;

	public GUIMain() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
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

		songListModel = new DefaultListModel<String>();

		list = new JList<String>(songListModel);

		list.setBounds(10, 50, 200, getHeight() - 110);

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				changeSong(songListModel.getElementAt(list.getSelectedIndex()));

			}
		});
		contentPane.add(list);

		FlatButton btnStart = new FlatButton(null, FlatIconFont.FAST_BACKWARD, man);
		btnStart.setBounds(230, 50, 121, 50);
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
		btnBack.setBounds(btnStart.getX() + btnStart.getWidth() + 5, 50, 121, 50);

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
		btnPause.setBounds(btnBack.getX() + btnBack.getWidth() + 5, 50, 121, 50);
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
		btnForward.setBounds(btnPause.getX() + btnPause.getWidth() + 5, 50, 121, 50);
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
		btnEnd.setBounds(btnForward.getX() + btnForward.getWidth() + 5, 50, 121, 50);
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

		JLabel lblViewHideElements = new JLabel("Ebenen im Keyer ein-/ausblenden [Unterste --> Oberste]");
		lblViewHideElements.setFont(FlatFont.getInstance(18, Font.PLAIN));
		lblViewHideElements.setForeground(FlatColors.WHITE);
		lblViewHideElements.setBounds(list.getX() + list.getWidth() + 20,
				scrollPane.getY() + scrollPane.getHeight() + 20, 550, 30);
		contentPane.add(lblViewHideElements);

		FlatButton btnUntertitel = new FlatButton("Untertitel", man);
		btnUntertitel.setBounds(lblViewHideElements.getX(),
				lblViewHideElements.getY() + lblViewHideElements.getHeight() + 10, 100, 70);
		btnUntertitel.setBackground(FlatColors.ALIZARINRED);
		btnUntertitel.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				boolean state = GUIKeyer.get().toggleUntertitel();
				btnUntertitel.setBackground(state ? FlatColors.GREEN : FlatColors.ALIZARINRED);
			}

			@Override
			public void onAction(MouseClickedHandler arg0) {

			}
		});
		contentPane.add(btnUntertitel);

		FlatButton btnKollekte = new FlatButton("Kollekte", man);
		btnKollekte.setBounds(btnUntertitel.getX() + btnUntertitel.getWidth() + 5, btnUntertitel.getY(), 100, 70);
		btnKollekte.setBackground(FlatColors.ALIZARINRED);
		btnKollekte.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				boolean state = GUIKeyer.get().toggleKollekte();
				btnKollekte.setBackground(state ? FlatColors.GREEN : FlatColors.ALIZARINRED);
			}

			@Override
			public void onAction(MouseClickedHandler arg0) {

			}
		});
		contentPane.add(btnKollekte);

		FlatButton btnEndcard = new FlatButton("Endcard", man);
		btnEndcard.setBounds(btnKollekte.getX() + btnKollekte.getWidth() + 5, btnKollekte.getY(), 100, 70);
		btnEndcard.setBackground(FlatColors.ALIZARINRED);
		btnEndcard.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				boolean state = GUIKeyer.get().toggleEndcard();
				btnEndcard.setBackground(state ? FlatColors.GREEN : FlatColors.ALIZARINRED);
			}

			@Override
			public void onAction(MouseClickedHandler arg0) {

			}
		});
		contentPane.add(btnEndcard);

		FlatButton btnTitelfolie = new FlatButton("Begincard", man);
		btnTitelfolie.setBounds(btnEndcard.getX() + btnEndcard.getWidth() + 5, btnEndcard.getY(), 100, 70);
		btnTitelfolie.setBackground(FlatColors.GREEN);
		btnTitelfolie.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				boolean state = GUIKeyer.get().toggleBeginLayer();
				btnTitelfolie.setBackground(state ? FlatColors.GREEN : FlatColors.ALIZARINRED);
			}

			@Override
			public void onAction(MouseClickedHandler arg0) {

			}
		});
		contentPane.add(btnTitelfolie);

		FlatButton btnLogo = new FlatButton("Logo", man);
		btnLogo.setBounds(btnTitelfolie.getX() + btnTitelfolie.getWidth() + 5, btnTitelfolie.getY(), 100, 70);
		btnLogo.setBackground(FlatColors.GREEN);
		btnLogo.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				boolean state = GUIKeyer.get().toggleLogo();
				btnLogo.setBackground(state ? FlatColors.GREEN : FlatColors.ALIZARINRED);
			}

			@Override
			public void onAction(MouseClickedHandler arg0) {

			}
		});
		contentPane.add(btnLogo);

		FlatButton btnWindowBar = new FlatButton("Max-Button", man);
		btnWindowBar.setBounds(btnLogo.getX() + btnLogo.getWidth() + 5, btnTitelfolie.getY(), 100, 70);
		btnWindowBar.setBackground(FlatColors.GREEN);
		btnWindowBar.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				boolean state = GUIKeyer.get().toggleWindowBar();
				btnWindowBar.setBackground(state ? FlatColors.GREEN : FlatColors.ALIZARINRED);
			}

			@Override
			public void onAction(MouseClickedHandler arg0) {

			}
		});
		contentPane.add(btnWindowBar);

		FlatButton btnMoveUp = new FlatButton("", FlatIconFont.ARROW_UP, man);
		btnMoveUp.setBounds(list.getX(), list.getY() + list.getHeight() + 5, 60, 40);
		btnMoveUp.setBackground(FlatColors.BLUE);
		btnMoveUp.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				if (list.getSelectedIndex() > 0) {
					String tmp = songListModel.getElementAt(list.getSelectedIndex() - 1);
					songListModel.set(list.getSelectedIndex() - 1, songListModel.getElementAt(list.getSelectedIndex()));
					songListModel.set(list.getSelectedIndex(), tmp);
					list.setSelectedIndex(list.getSelectedIndex() - 1);
				}
			}

			@Override
			public void onAction(MouseClickedHandler arg0) {

			}
		});
		contentPane.add(btnMoveUp);

		FlatButton btnMoveDown = new FlatButton("", FlatIconFont.ARROW_DOWN, man);
		btnMoveDown.setBounds(btnMoveUp.getX() + btnMoveUp.getWidth() + 5, btnMoveUp.getY(), 60, 40);
		btnMoveDown.setBackground(FlatColors.BLUE);
		btnMoveDown.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				if (list.getSelectedIndex() < songListModel.getSize() - 1) {
					String tmp = songListModel.getElementAt(list.getSelectedIndex() + 1);
					songListModel.set(list.getSelectedIndex() + 1, songListModel.getElementAt(list.getSelectedIndex()));
					songListModel.set(list.getSelectedIndex(), tmp);
					list.setSelectedIndex(list.getSelectedIndex() + 1);
				}
			}

			@Override
			public void onAction(MouseClickedHandler arg0) {

			}
		});
		contentPane.add(btnMoveDown);

		getAllComponents(this).forEach(this::registerKeyListener);

	}

	public void loadSongs() {
		Untertitelinator.get().getSongs().stream().map(song -> song.getTitle()).forEach(songListModel::addElement);
		changeSong(0);
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
		Untertitelinator.get().getCurrentPlayer().jumpToStart();
		updateUI();
	}

	private void jumpToEnd() {
		Untertitelinator.get().getCurrentPlayer().jumpToEnd();
		updateUI();
	}

	private void changeSong(String name) {
		Untertitelinator.get().switchSong(
				Untertitelinator.get().getSongs().stream().filter(s -> s.getTitle().equals(name)).findFirst().get());
		updateUI();
	}

	private void changeSong(int index) {
		if (Untertitelinator.get().getSongs().size() > index) {
			Untertitelinator.get().switchSong(Untertitelinator.get().getSongs().get(index));
			list.setSelectedIndex(index);
			updateUI();

		}
	}

	public void nextLine() {
		Untertitelinator.get().getCurrentPlayer().nextLine();
		updateUI();
	}

	public void previousLine() {
		Untertitelinator.get().getCurrentPlayer().previousLine();
		updateUI();
	}

	public void pause() {
		Untertitelinator.get().getCurrentPlayer().pause();
		updateUI();
		if (Untertitelinator.get().getCurrentPlayer().isPaused()) {
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
		sentenceModel.changeSong(Untertitelinator.get().getCurrentPlayer().getSong(), Untertitelinator.get().getCurrentPlayer().getCurrentIndex());
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
