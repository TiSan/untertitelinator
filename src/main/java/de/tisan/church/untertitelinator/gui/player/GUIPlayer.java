package de.tisan.church.untertitelinator.gui.player;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.helpers.fadehelper.FadeHelper;
import de.tisan.flatui.helpers.resizehelpers.SwingResizeHelper;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentVideoSurface;

public class GUIPlayer extends JFrame {

	public static void main(String[] args) {
		GUIPlayer pl = new GUIPlayer();
		pl.setVisible(true);
		pl.play();
	}

	private EmbeddedMediaPlayerComponent vlc;

	public GUIPlayer() {
		FlatLayoutManager man = FlatLayoutManager.get(this);
		setLayout(null);
		setSize(500, 500);
		setLocation(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);
		contentPane.setBackground(FlatColors.BLACK);

		vlc = new EmbeddedMediaPlayerComponent();
		vlc.setBounds(0, 0, getWidth(), getHeight());
		contentPane.add(vlc);
		SwingResizeHelper.getResizeHelperForSwing(this).addComponent(vlc, Anchor.LEFT, Anchor.RIGHT, Anchor.DOWN);

	}

	private void play() {
		MediaRef media = vlc.mediaPlayerFactory().media()
				.newMediaRef("Wildlife.mp4");
		vlc.mediaPlayer().media().play(media);
	}
}
