package de.tisan.church.untertitelinator.gui.main;

import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.CTEventListener;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.SongLinePacket;
import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;

public class GUIMainSongTextPanel extends AGUIMainPanel {
	private static final long serialVersionUID = 8492314016575322257L;
	private FlatButton boxCurrentLine1;
	private FlatButton boxCurrentLine2;
	private JTable table;
	private SentenceModel sentenceModel;

	public GUIMainSongTextPanel(FlatLayoutManager man, GUIMain instance, Dimension preferredSize) {
		super(man, instance, preferredSize);

		JLabel lblCurrentLine = new JLabel("Aktuell angezeigte Zeilen");
		lblCurrentLine.setFont(FlatFont.getInstance(18, Font.PLAIN));
		lblCurrentLine.setForeground(FlatColors.WHITE);
		lblCurrentLine.setBounds(0, 0, preferredSize.width, 30);
		add(lblCurrentLine);

		boxCurrentLine1 = new FlatButton("", man);
		boxCurrentLine1.setBounds(0, 35, preferredSize.width, 25);
		boxCurrentLine1.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		boxCurrentLine1.disableEffects();
		add(boxCurrentLine1);

		boxCurrentLine2 = new FlatButton("", man);
		boxCurrentLine2.setBounds(0, 60, preferredSize.width, 25);
		boxCurrentLine2.setAnchor(Anchor.LEFT, Anchor.RIGHT);
		boxCurrentLine2.disableEffects();
		add(boxCurrentLine2);

		table = new JTable();
		sentenceModel = new SentenceModel(table);
		table.setModel(sentenceModel);

		JScrollPane scrollPane = new JScrollPane(table);

		scrollPane.setBounds(0, 95, preferredSize.width, 300);
		add(scrollPane);

		CTEventHub.get().registerListener(new CTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				if (packet instanceof SongLinePacket) {
					SongLinePacket sPacket = (SongLinePacket) packet;
					List<String> currentLines = sPacket.getCurrentLines();
					boxCurrentLine1.setText(currentLines.size() > 0 ? currentLines.get(0) : "");
					boxCurrentLine2.setText(currentLines.size() > 1 ? currentLines.get(1) : "");
					sentenceModel.changeSong(Untertitelinator.get().getCurrentPlayer().getSong(),
							Untertitelinator.get().getCurrentPlayer().getCurrentIndex());
					sentenceModel.scrollToVisible(table, Untertitelinator.get().getCurrentPlayer().getCurrentIndex(), 0);
				}
			}
		});
	}

	@Override
	public void updateThisComponent() {
		

	}
}
