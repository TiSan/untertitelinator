package de.tisan.church.untertitelinator.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class GUIKeyerUntertitelPanel extends JPanel {

	private static final long serialVersionUID = 4135343673389911574L;
	private FlatButton currentLine1;
	private FlatButton currentLine2;
	private FlatButton nextLine1;
	private FlatButton nextLine2;

	public GUIKeyerUntertitelPanel(FlatLayoutManager man, GUIKeyer instance, Dimension preferredSize) {
		setLayout(null);
		setBackground(instance.bg);

		Font font = FlatFont.getInstance(70, Font.BOLD);
		final int height = 100;
		int spaceY = (int) (preferredSize.height - (height * 3));
		int spaceX = 30;
		int width = getWidth() - (spaceX * 2);

		currentLine1 = new FlatButton("Aktuelle Zeile 1", man);
		currentLine1.setBounds(spaceX, spaceY, width, height);
		currentLine1.setFont(font);
		currentLine1.setBackground(instance.bg);
		currentLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		add(currentLine1);

		currentLine2 = new FlatButton("Aktuelle Zeile 2", man);
		currentLine2.setBounds(spaceX, currentLine1.getY() + currentLine1.getHeight(), width, height);
		currentLine2.setFont(font);
		currentLine2.setBackground(instance.bg);
		currentLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		add(currentLine2);

		if ((boolean) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERSECONELINEENABLED, false)) {
			int height2 = 40;
			spaceY = 10;
			font = FlatFont.getInstance(30, Font.BOLD);

			nextLine1 = new FlatButton("N�chste Zeile 1", man);
			nextLine1.setBounds(spaceX, currentLine2.getY() + currentLine2.getHeight() + spaceY, width, height2);
			nextLine1.setFont(font);
			nextLine1.setBackground(instance.bg);
			nextLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
			add(nextLine1);

			nextLine2 = new FlatButton("N�chste Zeile 2", man);
			nextLine2.setBounds(spaceX, nextLine1.getY() + nextLine1.getHeight() + spaceY, width, height2);
			nextLine2.setFont(font);
			nextLine2.setBackground(instance.bg);
			// nextLine2.setBackground(new Color(1, 1, 1, 1));
			nextLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
			add(nextLine2);
		}

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				currentLine1.setBounds(spaceX, (getHeight() - (height * 3)), getWidth() - (spaceX * 2), height);
				currentLine2.setBounds(spaceX, currentLine1.getY() + currentLine1.getHeight(),
						getWidth() - (spaceX * 2), height);

			}
		});
	}
	public void showNewTextLines(String line1, String line2, String line3, String line4) {
		currentLine1.setText(line1);
		currentLine2.setText(line2);
		if ((boolean) JSONPersistence.get().getSetting(UTPersistenceConstants.GUIKEYERSECONELINEENABLED, false)) {
			nextLine1.setText(line3);
			nextLine2.setText(line4);
		}
	}
}
