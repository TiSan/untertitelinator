package de.tisan.church.untertitelinator.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;

import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;

public class GUIKeyerKollektePanel extends JPanel {

	private static final long serialVersionUID = 8148577345643611052L;
	private FlatButton currentLine1;
	private FlatButton currentLine2;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	public GUIKeyerKollektePanel(FlatLayoutManager man, GUIKeyer instance, Dimension preferredSize) {
		setLayout(null);
		setBackground(instance.bg);

		Font font = FlatFont.getInstance(70, Font.BOLD);
		final int height = 100;
		int spaceY = (int) (preferredSize.height - (height * 3));
		int spaceX = 30;
		int width = getWidth() - (spaceX * 2);

		currentLine1 = new FlatButton("Kollektenkonto: DE76 5006 1741 0000 0096 87", man);
		currentLine1.setBounds(spaceX, spaceY, width, height);
		currentLine1.setFont(font);
		currentLine1.setBackground(instance.bg);
		currentLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		add(currentLine1);

		currentLine2 = new FlatButton("Verwendungszweck: 'Kollekte " + sdf.format(new Date()) + "'", man);
		currentLine2.setBounds(spaceX, currentLine1.getY() + currentLine1.getHeight(), width, height);
		currentLine2.setFont(font);
		currentLine2.setBackground(instance.bg);
		currentLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		add(currentLine2);

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
	}
}
