package de.tisan.church.untertitelinator.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.settings.JSONPersistence;
import de.tisan.church.untertitelinator.settings.PersistenceConstants;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;

public class GUIKeyer extends JFrame {

	private static final long serialVersionUID = 7666681011188876592L;
	private static GUIKeyer instance;

	public static GUIKeyer get() {
		if (instance == null) {
			instance = new GUIKeyer();
			instance.setVisible(true);
		}
		return instance;
	}

	private FlatButton currentLine1;
	private FlatButton currentLine2;
	private FlatButton nextLine1;
	private FlatButton nextLine2;
	
	public GUIKeyer() {
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			screenSize = new Dimension(
					(Integer) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERWIDTH, 1024),
					(Integer) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERHEIGHT, 768));
			setUndecorated(true);
			setLocation((Integer) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERX, 0),
					(Integer) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERY, 0));
			setSize(screenSize);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			Color bg;

			bg = (Color) Color.class
					.getField(
							(String) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERBACKGROUND, "GREEN"))
					.get(new Color(0));

			setBackground(bg);

			JPanel contentPane = new JPanel();
			contentPane.setLayout(null);
			setContentPane(contentPane);
			contentPane.setBackground(bg);
			FlatLayoutManager man = FlatLayoutManager.get(this);

			FlatTitleBarWin10 bar = new FlatTitleBarWin10(man,
					(String) JSONPersistence.get().getSetting(PersistenceConstants.CHURCHNAME,
							"Evangelische Kirchengemeinde Oberstedten") + " - Untertitelinator v"
							+ Untertitelinator.VERSION);
			bar.setBounds(0, 0, getWidth(), 30);
			bar.setBackground(bg);
			contentPane.add(bar);
			bar.addFlatTitleBarListener(new DefaultFlatTitleBarListener(this));
			bar.setMaximizable(true);
			bar.setMinimizable(false);
			bar.setMoveable(false);
			bar.setAnchor(Anchor.LEFT, Anchor.RIGHT);

			
			Font font = FlatFont.getInstance(70, Font.BOLD);
			final int height = 100;
			int spaceY = (int) (screenSize.height  - (height * 2));
			int spaceX = 30;
			int width = getWidth() - (spaceX * 2);

			currentLine1 = new FlatButton("Aktuelle Zeile 1", man);
			currentLine1.setBounds(spaceX, spaceY, width, height);
			currentLine1.setFont(font);
			currentLine1.setBackground(bg);
			currentLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
			contentPane.add(currentLine1);

			currentLine2 = new FlatButton("Aktuelle Zeile 2", man);
			currentLine2.setBounds(spaceX, currentLine1.getY() + currentLine1.getHeight(), width, height);
			currentLine2.setFont(font);
			currentLine2.setBackground(bg);
			currentLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
			contentPane.add(currentLine2);
			
			addComponentListener(new ComponentAdapter() {
			    public void componentResized(ComponentEvent componentEvent) {
			    	currentLine1.setBounds(spaceX, (getHeight() - (height * 2)), getWidth() - (spaceX * 2), height);
			    	currentLine2.setBounds(spaceX, currentLine1.getY() + currentLine1.getHeight(), getWidth() - (spaceX * 2), height);
			    }
			});
			
			if((boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERSECONELINEENABLED, false)) {
				int height2 = 40;
				spaceY = 10;
				font = FlatFont.getInstance(30, Font.BOLD);
				
				nextLine1 = new FlatButton("Nächste Zeile 1", man);
				nextLine1.setBounds(spaceX, currentLine2.getY() + currentLine2.getHeight() + spaceY, width, height2);
				nextLine1.setFont(font);
				nextLine1.setBackground(bg);
				nextLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
				contentPane.add(nextLine1);
				
				nextLine2 = new FlatButton("Nächste Zeile 2", man);
				nextLine2.setBounds(spaceX, nextLine1.getY() + nextLine1.getHeight() + spaceY, width, height2);
				nextLine2.setFont(font);
				nextLine2.setBackground(bg);
				nextLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
				contentPane.add(nextLine2);	
			}
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public void showNewTextLines(String title, String line1, String line2, String line3, String line4, int delay,
			boolean paused) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
				currentLine1.setText(line1);
				currentLine2.setText(line2);
				if((boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERSECONELINEENABLED, false)) {
					nextLine1.setText(line3);
					nextLine2.setText(line4);
				}
			}
		}).start();
	}
}
