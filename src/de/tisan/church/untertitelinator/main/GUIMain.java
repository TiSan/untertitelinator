package de.tisan.church.untertitelinator.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.ficon.FlatIconFont;
import de.tisan.flatui.components.flisteners.ActionListener;
import de.tisan.flatui.components.flisteners.MouseClickedHandler;
import de.tisan.flatui.components.flisteners.Priority;
import de.tisan.flatui.components.ftextbox.FlatTextBox;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;

public class GUIMain extends JFrame {

	private Untertitelinator un;
	private FlatTextBox boxCurrentLine;

	public GUIMain(Untertitelinator un) {
		this.un = un;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		setUndecorated(true);
		setSize(800, 550);
		setLocation((int) (dim.getWidth() / 2 - (getWidth() / 2)), (int) (dim.getHeight() / 2 - (getHeight() / 2)));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel contentPane = new JPanel();
		setContentPane(contentPane);

		contentPane.setLayout(null);
		contentPane.setBackground(FlatColors.BACKGROUND);

		FlatLayoutManager man = FlatLayoutManager.get(this);

		FlatTitleBarWin10 bar = new FlatTitleBarWin10(man, "EVKO Untertitelinator v0.2");
		bar.setBounds(0, 0, getWidth(), 30);
		bar.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		bar.addFlatTitleBarListener(new DefaultFlatTitleBarListener(this));

		contentPane.add(bar);

		DefaultListModel<String> model = new DefaultListModel<String>();

		JList<String> list = new JList<String>(model);
		list.setBounds(10, 50, 200, getHeight() - 70);
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				changeSong(e.getFirstIndex());
			}
		});
		contentPane.add(list);

		un.getSongs().stream().map(song -> song.getTitle()).forEach(model::addElement);

		FlatButton btnBack = new FlatButton(null, FlatIconFont.BACKWARD, man);
		btnBack.setBounds(230, 50, 70, 50);
		btnBack.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onAction(MouseClickedHandler arg0) {
				previousLine();
			}
		});
		
		contentPane.add(btnBack);

		FlatButton btnPause = new FlatButton(null, FlatIconFont.PAUSE, man);
		btnPause.setBounds(310, 50, 70, 50);
		btnPause.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onAction(MouseClickedHandler arg0) {
				pause();
			}
		});
		
		contentPane.add(btnPause);

		FlatButton btnForward = new FlatButton(null, FlatIconFont.FORWARD, man);
		btnForward.setBounds(390, 50, 70, 50);
		btnForward.addActionListener(Priority.NORMAL, new ActionListener() {

			@Override
			public void onAction(MouseClickedHandler arg0) {
				nextLine();
			}
		});
		contentPane.add(btnForward);

		JLabel lblCurrentLine = new JLabel("Aktuell angezeigte Zeile");
		lblCurrentLine.setFont(FlatFont.getInstance(18, Font.PLAIN));
		lblCurrentLine.setForeground(FlatColors.WHITE);
		lblCurrentLine.setBounds(230, 110, 550, 30);
		contentPane.add(lblCurrentLine);

		boxCurrentLine = new FlatTextBox(man);
		boxCurrentLine.setBounds(230, 140, 550, 50);
		boxCurrentLine.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		boxCurrentLine.setEditable(false);
		contentPane.add(boxCurrentLine);
		changeSong(0);

	}

	private void changeSong(int index) {
		un.switchSong(un.getSongs().get(index));
		boxCurrentLine.setText(un.getCurrentPlayer().getCurrentLine());
	}

	private void nextLine() {
		un.getCurrentPlayer().nextLine();
		boxCurrentLine.setText(un.getCurrentPlayer().getCurrentLine());
	}

	private void previousLine() {
		un.getCurrentPlayer().previousLine();
		boxCurrentLine.setText(un.getCurrentPlayer().getCurrentLine());
	}

	private void pause() {
		un.getCurrentPlayer().pause();
		boxCurrentLine.setText(un.getCurrentPlayer().getCurrentLine());
	}

}
