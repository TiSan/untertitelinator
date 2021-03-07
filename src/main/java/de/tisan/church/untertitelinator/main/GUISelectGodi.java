package de.tisan.church.untertitelinator.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;

public class GUISelectGodi extends JFrame {

	private static final long serialVersionUID = -6722718983818902404L;
	private DefaultComboBoxModel<String> comboBoxModel;

	public GUISelectGodi() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setAlwaysOnTop(true);
		setSize(500, 300);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width / 2) - (getWidth() / 2), (d.height / 2) - (getHeight() / 2));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		FlatLayoutManager man = FlatLayoutManager.get(this);
		contentPane.setBackground(FlatColors.BACKGROUND);
		setUndecorated(true);
		FlatTitleBarWin10 bar = new FlatTitleBarWin10(man, "Eventauswahl");
		bar.setBounds(0, 0, getWidth(), 30);
		bar.addFlatTitleBarListener(new DefaultFlatTitleBarListener(this));
		bar.setMaximizable(false);
		contentPane.add(bar);

		JLabel lblTitle = new JLabel("Wähle ein Event aus:");
		lblTitle.setBounds(20, 50, getWidth() - 40, 50);
		lblTitle.setFont(FlatFont.getInstance(18, Font.PLAIN));
		lblTitle.setForeground(FlatColors.WHITE);
		contentPane.add(lblTitle);

		comboBoxModel = new DefaultComboBoxModel<String>();
		comboBoxModel.addElement("Event wählen...");
		JComboBox<String> comboBox = new JComboBox<String>(comboBoxModel);
		comboBox.setBounds(20, 100, getWidth() - 40, 30);
		contentPane.add(comboBox);
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (isVisible()) {
					Untertitelinator.get().selectEvent(comboBox.getSelectedIndex() - 1);
					Loader.selectionTaken();
					setVisible(false);
					dispose();
				}
			}
		});
	}

	public void loadUi() {
		Untertitelinator.get().getAllEvents().forEach(event -> {
			comboBoxModel.addElement(
					event.getStartDateString() + " - " + event.getName());

		});
	}
}
