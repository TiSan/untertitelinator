package de.tisan.church.untertitelinator.gui.player;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

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
		contentPane.setBackground(FlatColors.WHITE);

		vlc = new EmbeddedMediaPlayerComponent();
		vlc.setBounds(0, 0, getWidth(), getHeight());
		contentPane.add(vlc);
		//SwingResizeHelper.getResizeHelperForSwing(this).addComponent(vlc, Anchor.LEFT, Anchor.RIGHT, Anchor.DOWN);

		 addComponentListener(new ComponentAdapter() {
             public void componentResized(ComponentEvent componentEvent) {
            	 vlc.setBounds(0, 0, getWidth(), getHeight());
             }
		 });
	}

	private void play() {
		MediaPlayerFactory factory = vlc.mediaPlayerFactory();
		MediaRef media = factory.media()
				.newMediaRef("Wildlife.mp4");
		MediaPlayer player = vlc.mediaPlayer();
		player.media().play(media);
		
		
	}
}
