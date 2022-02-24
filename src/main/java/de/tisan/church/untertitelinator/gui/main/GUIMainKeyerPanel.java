package de.tisan.church.untertitelinator.gui.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.GUIKeyerLayerChangePacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.flisteners.ActionListener;
import de.tisan.flatui.components.flisteners.MouseClickedHandler;
import de.tisan.flatui.components.flisteners.MouseListenerImpl;
import de.tisan.flatui.components.flisteners.MouseReleaseHandler;
import de.tisan.flatui.components.flisteners.Priority;
import de.tisan.tools.persistencemanager.JSONPersistence;
import de.tisan.tools.persistencemanager.SubTypeReference;

public class GUIMainKeyerPanel extends AGUIMainPanel {
	private static final long serialVersionUID = 5033708428105225125L;

	Color btnActiveColor = FlatColors.ALIZARINRED;
	Color btnInactiveColor = FlatColors.HIGHLIGHTBACKGROUND;

	private FlatButton btnUntertitel;

	private FlatButton btnKollekte;

	private FlatButton btnLogo;

	private FlatButton btnPictureRotation;

	private Map<String, String> videoList;

	private FlatButton btnVideo;

	private JComboBox cmbvideoList;

	public GUIMainKeyerPanel(FlatLayoutManager man, GUIMain instance, Dimension preferredSize) {
		super(man, instance, preferredSize);
		int x = 0;
		int y = 0;
		int widthBtn = 100;
		int heightBtn = 70;

		this.videoList = JSONPersistence.get().getSetting(UTPersistenceConstants.VIDEO_FILES,
				new HashMap<String, String>(), new SubTypeReference<HashMap<String, String>>() {
				});

		JLabel lblViewHideElements = new JLabel("Ebenen im Keyer ein-/ausblenden [Unterste --> Oberste]");
		lblViewHideElements.setFont(FlatFont.getInstance(18, Font.PLAIN));
		lblViewHideElements.setForeground(FlatColors.WHITE);
		lblViewHideElements.setBounds(x, y, 550, 30);
		add(lblViewHideElements);

		y = lblViewHideElements.getY() + lblViewHideElements.getHeight() + 10;

		btnUntertitel = new FlatButton("Untertitel", man);
		btnUntertitel.setBounds(x, y, widthBtn, heightBtn);
		btnUntertitel.disableEffects();
		btnUntertitel.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				UTEventHub.get().publish(new CommandPacket(Command.TOGGLE_UNTERTITEL));
			}
		});
		add(btnUntertitel);

		x += widthBtn + 5;

		btnKollekte = new FlatButton("Kollekte", man);
		btnKollekte.setBounds(x, y, widthBtn, heightBtn);
		btnKollekte.disableEffects();
		btnKollekte.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				UTEventHub.get().publish(new CommandPacket(Command.TOGGLE_KOLLEKTE));
			}
		});
		add(btnKollekte);

		x += widthBtn + 5;

		btnVideo = new FlatButton("Videoplayer", man);
		btnVideo.setBounds(x, y, widthBtn, heightBtn);
		btnVideo.disableEffects();
		btnVideo.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				String url = videoList.get(cmbvideoList.getSelectedItem());
				if(url != null && url.isEmpty() == false) {
					UTEventHub.get().publish(new CommandPacket(Command.TOGGLE_VIDEO));
				}
			}
		});
		add(btnVideo);

		x += widthBtn + 5;

		btnPictureRotation = new FlatButton("Bildrotation", man);
		btnPictureRotation.setBounds(x, y, widthBtn, heightBtn);
		btnPictureRotation.disableEffects();
		btnPictureRotation.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				UTEventHub.get().publish(new CommandPacket(Command.TOGGLE_PICTURE_ROTATION));
			}
		});
		add(btnPictureRotation);

		x += widthBtn + 5;

		btnLogo = new FlatButton("Logo", man);
		btnLogo.setBounds(x, y, widthBtn, heightBtn);
		btnLogo.disableEffects();
		btnLogo.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				UTEventHub.get().publish(new CommandPacket(Command.TOGGLE_LOGO));
			}
		});
		add(btnLogo);

		y = btnLogo.getY() + btnLogo.getHeight() + 10;
		x = 0;

		JLabel lblVideofile = new JLabel("Videofile: ");
		lblVideofile.setFont(FlatFont.getInstance(18, Font.PLAIN));
		lblVideofile.setForeground(FlatColors.WHITE);
		lblVideofile.setBounds(x, y, 80, 30);
		add(lblVideofile);

		x += 90;

		cmbvideoList = new JComboBox(videoList.keySet().toArray());
		cmbvideoList.setBounds(x, y, 250, 30);
		add(cmbvideoList);
		cmbvideoList.addActionListener(new java.awt.event.ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				UTEventHub.get().publish(new CommandPacket(Command.SET_VIDEO_FILE, videoList.get(cmbvideoList.getSelectedItem())));
				
			}
		});

		UTEventHub.get().registerListener(new UTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				if (packet instanceof GUIKeyerLayerChangePacket) {
					GUIKeyerLayerChangePacket bPacket = (GUIKeyerLayerChangePacket) packet;
					switch (bPacket.getLayerName()) {
					case KOLLEKTE:
						btnKollekte.setBackground(bPacket.isVisible() ? btnActiveColor : btnInactiveColor, true);
						break;
					case UNTERTITEL:
						btnUntertitel.setBackground(bPacket.isVisible() ? btnActiveColor : btnInactiveColor, true);
						break;
					case LOGO:
						btnLogo.setBackground(bPacket.isVisible() ? btnActiveColor : btnInactiveColor, true);
						break;
					case PICTURE_ROTATION:
						btnPictureRotation.setBackground(bPacket.isVisible() ? btnActiveColor : btnInactiveColor, true);
						break;
					case VIDEO:
						btnVideo.setBackground(bPacket.isVisible() ? btnActiveColor : btnInactiveColor, true);
						break;
					default:
						break;
					}
				}
			}
		});
	}
}
