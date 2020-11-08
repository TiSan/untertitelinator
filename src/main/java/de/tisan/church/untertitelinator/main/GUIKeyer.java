package de.tisan.church.untertitelinator.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.tisan.church.untertitelinator.churchtools.api.ChurchToolsApi;
import de.tisan.church.untertitelinator.churchtools.api.objects.Event;
import de.tisan.church.untertitelinator.churchtools.api.objects.EventService;
import de.tisan.church.untertitelinator.churchtools.api.objects.Service;
import de.tisan.church.untertitelinator.settings.JSONPersistence;
import de.tisan.church.untertitelinator.settings.PersistenceConstants;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
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
	private JLabel layerImage;
	private BufferedImage image;
	private FlatButton layerTitle;
	private FlatButton layerSubTitle;
	private Map<String, String> serviceListStr;
	private FlatButton layerDate;
	private ArrayList<FlatButton> listCastLayers;
	private FlatTitleBarWin10 bar;
	Color bg;
	private FlatButton layerNextStream;

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
			man.disableAllEffects();
			bar = new FlatTitleBarWin10(man, "");
			bar.setOptionMenuToggleEnabled(false);
			bar.setBounds(0, 0, getWidth(), 30);
			bar.setBackground(bg);
			bar.disableEffects();
			contentPane.add(bar);
			bar.addFlatTitleBarListener(new DefaultFlatTitleBarListener(this));
			bar.setCloseable((boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERCLOSEABLE, false));
			bar.setMaximizable(
					(boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERMAXIMIZABLE, true));
			bar.setMinimizable(
					(boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERMINIMIZABLE, false));
			bar.setMoveable((boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERMOVEABLE, false));
			bar.setAnchor(Anchor.LEFT, Anchor.RIGHT);
			
			Font font = FlatFont.getInstance(70, Font.BOLD);
			final int height = 100;
			int spaceY = (int) (screenSize.height - (height * 3));
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
					currentLine1.setBounds(spaceX, (getHeight() - (height * 3)), getWidth() - (spaceX * 2), height);
					currentLine2.setBounds(spaceX, currentLine1.getY() + currentLine1.getHeight(),
							getWidth() - (spaceX * 2), height);

				}
			});
			if ((boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERSECONELINEENABLED, false)) {
				int height2 = 40;
				spaceY = 10;
				font = FlatFont.getInstance(30, Font.BOLD);

				nextLine1 = new FlatButton("N�chste Zeile 1", man);
				nextLine1.setBounds(spaceX, currentLine2.getY() + currentLine2.getHeight() + spaceY, width, height2);
				nextLine1.setFont(font);
				nextLine1.setBackground(bg);
				nextLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
				contentPane.add(nextLine1);

				nextLine2 = new FlatButton("N�chste Zeile 2", man);
				nextLine2.setBounds(spaceX, nextLine1.getY() + nextLine1.getHeight() + spaceY, width, height2);
				nextLine2.setFont(font);
				nextLine2.setBackground(bg);
				// nextLine2.setBackground(new Color(1, 1, 1, 1));
				nextLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
				contentPane.add(nextLine2);
			}

			/* Begin Layer */

			int height3 = 70;
			int x1 = 150;
			int y1 = 100;
			font = FlatFont.getInstance(50, Font.BOLD);
			
			layerNextStream = new FlatButton("Nächster Stream: ", man);
			layerNextStream.setBounds(x1, y1, width, height3);
			layerNextStream.setFont(font);
			layerNextStream.setBackground(new Color(0, 0, 0, 0));
			layerNextStream.setCenterText(false);
			layerNextStream.setAnchor(Anchor.LEFT, Anchor.RIGHT);
			layerNextStream.disableEffects();
			contentPane.add(layerNextStream);
			
			y1 += layerNextStream.getHeight() + 5;
			font = FlatFont.getInstance(90, Font.BOLD);
			height3 = 130;
			
			layerTitle = new FlatButton("Titel der Veranstaltung", man);
			layerTitle.setBounds(x1, y1, width, height3);
			layerTitle.setFont(font);
			layerTitle.setBackground(new Color(0, 0, 0, 0));
			layerTitle.setCenterText(false);
			layerTitle.setAnchor(Anchor.LEFT, Anchor.RIGHT);
			layerTitle.disableEffects();
			contentPane.add(layerTitle);

			y1 += layerTitle.getHeight() + 5;
			font = FlatFont.getInstance(70, Font.BOLD);
			height3 = 100;
			
			layerSubTitle = new FlatButton("Untertitel der Veranstaltung", man);
			layerSubTitle.setBounds(x1, y1, width, height3);
			layerSubTitle.setFont(font);
			layerSubTitle.setBackground(new Color(0, 0, 0, 0));
			layerSubTitle.setCenterText(false);
			layerSubTitle.setAnchor(Anchor.LEFT, Anchor.RIGHT);
			layerSubTitle.disableEffects();
			contentPane.add(layerSubTitle);

			y1 += layerTitle.getHeight() + 5;
			font = FlatFont.getInstance(50, Font.BOLD);
			height3 = 70;
			
			layerDate = new FlatButton("Datum", man);
			layerDate.setBounds(x1, y1, width, height3);
			layerDate.setFont(font);
			layerDate.setBackground(new Color(0, 0, 0, 0));
			layerDate.setCenterText(false);
			layerDate.setAnchor(Anchor.LEFT, Anchor.RIGHT);
			layerDate.disableEffects();
			contentPane.add(layerDate);

			y1 += layerSubTitle.getHeight() + 100;
			prepare();
			font = FlatFont.getInstance(30, Font.BOLD);
			height3 = 50;
			listCastLayers = new ArrayList<FlatButton>();
			for (String castItem : serviceListStr.keySet()) {
				FlatButton layerCast = new FlatButton(castItem + ": " + serviceListStr.get(castItem), man);
				layerCast.setBounds(x1, y1, width, height3);
				layerCast.setFont(font);
				layerCast.setBackground(new Color(0, 0, 0, 0));
				layerCast.setAnchor(Anchor.LEFT, Anchor.RIGHT);
				layerCast.setCenterText(false);
				layerCast.disableEffects();
				contentPane.add(layerCast);
				y1 += layerCast.getHeight() + 5;
				listCastLayers.add(layerCast);
			}

			/* Bild */
			layerImage = new JLabel();
			image = ImageIO
					.read(GUIKeyer.class.getResourceAsStream("/de/tisan/church/untertitelinator/resources/bg.jpg"));
			fitImage(getWidth(), getHeight());
			contentPane.add(layerImage);
			layerImage.setVisible(false);
			addComponentListener(new ComponentAdapter() {

				@Override
				public void componentResized(ComponentEvent e) {
					fitImage(getWidth(), getHeight());
				}
			});
			toggleBeginLayer();
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
				| IOException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void fitImage(int width, int height) {
		if (layerImage.isVisible()) {
			layerImage.setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
			// layerImage.setBackground(FlatColors.BACKGROUND);
		}
		layerImage.setBounds(0, 0, width, height);
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
				if ((boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERSECONELINEENABLED, false)) {
					nextLine1.setText(line3);
					nextLine2.setText(line4);
				}
			}
		}).start();
	}

	public void prepare() {
		Optional<Event> event = ChurchToolsApi.get().getNextEvent();
		if (event.isPresent()) {
			String titleName = event.get().getName();
			System.out.println(titleName);
			List<Service> services = ChurchToolsApi.get().getServices().get();

			serviceListStr = new TreeMap<String, String>();
			for (EventService es : event.get().getEventServices()) {
				Service s = services.parallelStream().filter(ss -> ss.getId() == es.getServiceId()).findFirst().get();
				if (s.getComment().equals("<NOT_VISIBLE>")) {
					continue;
				}
				String key = s.getComment().isEmpty() == false ? s.getComment() : s.getName();
				if (serviceListStr.containsKey(key)) {
					serviceListStr.put(key, serviceListStr.get(key) + ", " + es.getName());
				} else {
					serviceListStr.put(key, es.getName());
					
				}
			}

			//serviceListStr.sort(String.CASE_INSENSITIVE_ORDER);

			layerTitle.setText(event.get().getDescription());
			layerSubTitle.setText("Thema: \"" + titleName + "\"");
			layerDate.setText(
					event.get().getStartDate().plusHours(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy - hh:mm"))
							+ " Uhr");
		}
	}

	public void toggleBeginLayer() {
		if (layerImage.isVisible()) {
			layerImage.setVisible(false);
			layerTitle.setVisible(false);
			layerSubTitle.setVisible(false);
			layerDate.setVisible(false);
			bar.setBackground(bg);
			listCastLayers.forEach(e -> e.setVisible(false));

			currentLine1.setVisible(true);
			currentLine2.setVisible(true);
			if ((boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERSECONELINEENABLED, false)) {
				nextLine1.setVisible(true);
				nextLine2.setVisible(true);
			}
		} else {
			layerImage.setVisible(true);
			layerTitle.setVisible(true);
			layerSubTitle.setVisible(true);
			layerDate.setVisible(true);
			listCastLayers.forEach(e -> e.setVisible(true));
			bar.setBackground(new Color(0, 0, 0, 0));
			fitImage(getWidth(), getHeight());
			currentLine1.setVisible(false);
			currentLine2.setVisible(false);
			if ((boolean) JSONPersistence.get().getSetting(PersistenceConstants.GUIKEYERSECONELINEENABLED, false)) {
				nextLine1.setVisible(false);
				nextLine2.setVisible(false);
			}

			repaint();
		}
	}
}
