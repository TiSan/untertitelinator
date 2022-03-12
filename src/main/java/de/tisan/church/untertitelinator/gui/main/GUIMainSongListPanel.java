package de.tisan.church.untertitelinator.gui.main;

import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.tisan.church.untertitelinator.data.Song;
import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.instancer.packets.SongListChangedPacket;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.fhintbox.FlatHintBoxEntry;
import de.tisan.flatui.components.fhintbox.FlatHintBoxManager;
import de.tisan.flatui.components.ficon.FlatIcon;
import de.tisan.flatui.components.flisteners.MouseListenerImpl;
import de.tisan.flatui.components.flisteners.MouseReleaseHandler;
import de.tisan.flatui.components.flisteners.Priority;

public class GUIMainSongListPanel extends AGUIMainPanel {

	private static final long serialVersionUID = 8890430331107288284L;
	private DefaultListModel<String> songListModel;
	private JList<String> list;
	protected List<Song> songList;

	public GUIMainSongListPanel(FlatLayoutManager man, GUIMain instance, Dimension preferredSize) {
		super(man, instance, preferredSize);
		songListModel = new DefaultListModel<String>();

		list = new JList<String>(songListModel);

		list.setBounds(0, 0, preferredSize.width -65, preferredSize.height - 50);

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = list.getSelectedIndex();
				if (index >= 0 && index < songListModel.getSize()) {
					changeSong(songListModel.getElementAt(list.getSelectedIndex()));
				}
			}
		});
		add(list);

		int widthBtn = 55;
		int heightBtn = 50;
		int x = preferredSize.width -widthBtn ;
		int y = 0;

		FlatButton btnMoveUp = new FlatButton("", FlatIcon.ARROW_UP, man);
		btnMoveUp.setBounds(x, y, widthBtn, heightBtn);
		btnMoveUp.setBackground(FlatColors.BLUE);
		btnMoveUp.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				if (list.getSelectedIndex() > 0) {
					String tmp = songListModel.getElementAt(list.getSelectedIndex() - 1);
					songListModel.set(list.getSelectedIndex() - 1, songListModel.getElementAt(list.getSelectedIndex()));
					songListModel.set(list.getSelectedIndex(), tmp);
					list.setSelectedIndex(list.getSelectedIndex() - 1);
				}
			}
		});
		add(btnMoveUp);

		y += heightBtn + 5;

		FlatButton btnMoveDown = new FlatButton("", FlatIcon.ARROW_DOWN, man);
		btnMoveDown.setBounds(x, y, widthBtn, heightBtn);
		btnMoveDown.setBackground(FlatColors.BLUE);
		btnMoveDown.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				if (list.getSelectedIndex() != -1 && list.getSelectedIndex() < songListModel.getSize() - 1) {
					String tmp = songListModel.getElementAt(list.getSelectedIndex() + 1);
					songListModel.set(list.getSelectedIndex() + 1, songListModel.getElementAt(list.getSelectedIndex()));
					songListModel.set(list.getSelectedIndex(), tmp);
					list.setSelectedIndex(list.getSelectedIndex() + 1);
				}
			}
		});
		add(btnMoveDown);

		y += heightBtn + 5;
		FlatHintBoxManager hManager = new FlatHintBoxManager();

		FlatButton btnUpdate = new FlatButton("", FlatIcon.SYNC, man);
		btnUpdate.setBounds(x, y, widthBtn, heightBtn);
		btnUpdate.setBackground(FlatColors.BLUE);
		btnUpdate.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				int result = JOptionPane.showConfirmDialog(instance,
						"Möchtest du wirklich alle Songs neu laden? Deine Reihenfolge geht dadurch verloren!",
						"Songs neu laden", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION) {
					songListModel.clear();
					UTEventHub.get().publish(new CommandPacket(Command.LOAD_SONGS));
					hManager.showHintBox(new FlatHintBoxEntry("Songs neu geladen", "Alle Songs im Song-Verzeichnis wurden erneut eingelesen!", 2));
				}
			}
		});
		add(btnUpdate);

		y += heightBtn + 5;

		FlatButton btnChangeEvent = new FlatButton("", FlatIcon.CALENDAR, man);
		btnChangeEvent.setBounds(x, y, widthBtn, heightBtn);
		btnChangeEvent.setBackground(FlatColors.BLUE);
		btnChangeEvent.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				GUISelectGodi ui = new GUISelectGodi();
			}
		});
		add(btnChangeEvent);

		list.setSelectedIndex(0);
		
		UTEventHub.get().registerListener(new UTEventListener() {
			
			@Override
			public void onEventReceived(Packet packet) {
				if(packet instanceof SongListChangedPacket) {
					SongListChangedPacket sPacket = (SongListChangedPacket) packet;
					songList = sPacket.getSongList();
					updateSongList();
				}
			}
		});
	}

	private void changeSong(String name) {
		UTEventHub.get().publish(new CommandPacket(Command.CHANGE_SONG, name));
	}

	private void updateSongList() {
		list.setSelectedIndex(0);
		songList.stream().map(song -> song.getTitle()).forEach(songListModel::addElement);
	}

}
