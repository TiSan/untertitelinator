package de.tisan.church.untertitelinator.gui.main;

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

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.EventListPacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.main.Loader;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;
import de.tisan.tisanapi.logger.Logger;

public class GUISelectGodi extends JFrame {

	private static final long serialVersionUID = -6722718983818902404L;
	private UTEventListener listener;
	private DefaultComboBoxModel<String> comboBoxModel;

	public GUISelectGodi() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			Logger.getInstance().err("LookAndFeel couldnt be set! " + e.getMessage(), e, getClass());

		}

		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setAlwaysOnTop(true);
		setSize(500, 300);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width / 2) - (getWidth() / 2), (d.height / 2) - (getHeight() / 2));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
					UTEventHub.get()
							.publish(new CommandPacket(Command.SELECT_EVENT, comboBox.getSelectedIndex() + ""));
					GUISelectGodi.this.setVisible(false);
					GUISelectGodi.this.dispose();
					UTEventHub.get().unregisterListener(listener);
				}
			}
		});

		UTEventHub.get().registerListener((listener = new UTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				if (packet instanceof EventListPacket) {
					EventListPacket sPacket = (EventListPacket) packet;
					comboBoxModel.removeAllElements();
					sPacket.getEventList().forEach(event -> {
						comboBoxModel.addElement(event.getStartDateString() + " - " + event.getName());
					});
					setVisible(true);
				}
			}
		}));

		UTEventHub.get().publish(new CommandPacket(Command.GET_EVENT_LIST));
	}

}
