package de.tisan.church.untertitelinator.gui.player;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import de.tisan.church.untertitelinator.gui.keyer.GUIKeyerLayer;
import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.GUIKeyerLayerChangePacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class GUIVideoPanel extends JPanel {

	private static final long serialVersionUID = 5623162033757903847L;
	private EmbeddedMediaPlayerComponent vlc;
	private String url;

	public GUIVideoPanel(FlatLayoutManager man, Dimension preferredSize) {
		try {
			
		
		setLayout(null);
		setOpaque(false);
		setBackground(FlatColors.BLACK);

		vlc = new EmbeddedMediaPlayerComponent();
		vlc.setBounds(0, 0, getWidth(), getHeight());
		add(vlc);

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				vlc.setBounds(0, 0, getWidth(), getHeight());
			}
		});
		UTEventHub.get().registerListener(new UTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				if (packet instanceof CommandPacket) {
					CommandPacket sPacket = (CommandPacket) packet;
					if (sPacket.getCommand().equals(Command.SET_VIDEO_FILE)) {
						setVideoUrl(sPacket.getArgs().get(0));
					} else if (sPacket.getCommand().equals(Command.STOP_VIDEO)) {
						stopVideo();
					}
				} else if (packet instanceof GUIKeyerLayerChangePacket) {
					GUIKeyerLayerChangePacket sPacket = (GUIKeyerLayerChangePacket) packet;
					if (sPacket.getLayerName().equals(GUIKeyerLayer.VIDEO)) {
						if (sPacket.isVisible() && vlc.mediaPlayer().status().isPlaying() == false) {
							showVideo();
						} else if(sPacket.isVisible() == false && vlc.mediaPlayer().status().isPlaying() == true){
							stopVideo();
						}
					}
				}
			}
		});
		} catch(java.lang.UnsatisfiedLinkError e) {
			e.printStackTrace();
		}
		repaint();
	}

	private void setVideoUrl(String url) {
		this.url = url;
	}

	private void showVideo() {
		MediaPlayerFactory factory = vlc.mediaPlayerFactory();
		MediaRef media = factory.media().newMediaRef(url);
		MediaPlayer player = vlc.mediaPlayer();
		player.media().play(media);
	}

	private void stopVideo() {
		vlc.mediaPlayer().controls().stop();
	}

}
