package de.tisan.church.untertitelinator.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.fprogressbar.FlatProgressBar;

public class GUILoad extends JFrame {

	private static final long serialVersionUID = 975819350295945678L;
	private JPanel contentPane;
	private JLabel lblLoading;

	public GUILoad() {
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((d.width / 2) - (471 / 2), (d.height / 2) - (180 / 2), 471, 180);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		FlatLayoutManager man = FlatLayoutManager.get(this);
		man.setResizable(false);
		final FlatProgressBar pro = new FlatProgressBar(true, man);
		pro.setBounds(0, 156, 526, 24);

		lblLoading = new JLabel("Loading ...");
		lblLoading.setBounds(0, 129, 526, 24);
		contentPane.add(lblLoading);
		JLabel label = new JLabel("");
		label.setBounds(0, 0, 538, 158);
		try {
			label.setIcon(new ImageIcon(ImageIO
					.read(GUILoad.class.getResourceAsStream("/de/tisan/church/untertitelinator/resources/logo.png"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		contentPane.add(label);

		new Thread(new Runnable() {

			@Override
			public void run() {
				int i = 0;
				Random rnd = new Random();
				while (i <= 100) {
					switch (i) {
					case 0:
						say("Lade ProgrammGUI...");
						Loader.loadUi();
						break;
					case 10:
						say("Lade Untertitelinator-Songs...");
						Loader.loadUntertitelinatorSongs();
						break;
					case 20:
						say("Lade Songs in Hauptoberfläche...");
						Loader.loadSongsIntoMainGui();
						break;
					case 30:
						say("Lade ChurchTools Veranstaltungen...");
						Loader.loadChurchtoolsEvents();
						break;
					case 40:
						say("Lade Eventauswahl...");
						Loader.loadSelectionMenu();
						break;
					case 50:
						say("Warte auf Benutzer...");
						Loader.showSelectionMenu();
						setVisible(false);
						break;
					case 51:
						say("Lade Event...");
						setVisible(true);
						break;
					case 60:
						say("Lade Greenscreen-Keyer...");
						Loader.loadKeyerUi();
						break;
					case 70:
						say("Creating internal frameworks...");
						break;
					case 95:
						say("Zeige Oberflächen an...");
						Loader.showUi();
						break;
					}
					try {
						Thread.sleep(rnd.nextInt(100) + 1);
					} catch (InterruptedException e) {
					}
					pro.setValue(i);
					if (i >= 50 && Loader.isReadyToContinue() == false) {

					} else {
						i++;
					}
				}

				dispose();
			}

		}).start();

	}

	private void say(String say) {
		lblLoading.setText(say);

	}
}
