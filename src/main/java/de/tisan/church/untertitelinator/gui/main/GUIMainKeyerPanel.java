package de.tisan.church.untertitelinator.gui.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Command;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.gui.keyer.GUIKeyer;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.flisteners.MouseListenerImpl;
import de.tisan.flatui.components.flisteners.MouseReleaseHandler;
import de.tisan.flatui.components.flisteners.Priority;

public class GUIMainKeyerPanel extends AGUIMainPanel {
	private static final long serialVersionUID = 5033708428105225125L;

	Color btnActiveColor = FlatColors.ALIZARINRED;
	Color btnInactiveColor = FlatColors.HIGHLIGHTBACKGROUND;

	private FlatButton btnUntertitel;

	private FlatButton btnKollekte;

	private FlatButton btnLogo;

	private FlatButton btnMaxButton;

	public GUIMainKeyerPanel(FlatLayoutManager man, GUIMain instance, Dimension preferredSize) {
		super(man, instance, preferredSize);
		int x = 0;
		int y = 0;
		int widthBtn = 100;
		int heightBtn = 70;

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
				CTEventHub.get().publish(new CommandPacket(Command.TOGGLE_UNTERTITEL));
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
				CTEventHub.get().publish(new CommandPacket(Command.TOGGLE_KOLLEKTE));
			}
		});
		add(btnKollekte);

		x += widthBtn + 5;



		btnLogo = new FlatButton("Logo", man);
		btnLogo.setBounds(x, y, widthBtn, heightBtn);
		btnLogo.disableEffects();
		btnLogo.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				CTEventHub.get().publish(new CommandPacket(Command.TOGGLE_LOGO));
			}
		});
		add(btnLogo);

		x += widthBtn + 5;

		btnMaxButton = new FlatButton("Max-Button", man);
		btnMaxButton.setBounds(x, y, widthBtn, heightBtn);
		btnMaxButton.disableEffects();
		btnMaxButton.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				CTEventHub.get().publish(new CommandPacket(Command.TOGGLE_WINDOW_BAR));
			}
		});
		add(btnMaxButton);
		
		updateThisComponent();
	}

	@Override
	public void updateThisComponent() {
		if (GUIKeyer.get().isKollekteVisible()) {
			btnKollekte.setBackground(btnActiveColor, true);
		} else {
			btnKollekte.setBackground(btnInactiveColor, true);
		}

		if (GUIKeyer.get().isUntertitelVisible()) {
			btnUntertitel.setBackground(btnActiveColor, true);
		} else {
			btnUntertitel.setBackground(btnInactiveColor, true);
		}



		if (GUIKeyer.get().isLogoVisible()) {
			btnLogo.setBackground(btnActiveColor, true);
		} else {
			btnLogo.setBackground(btnInactiveColor, true);
		}

		if (GUIKeyer.get().isWindowBarVisible()) {
			btnMaxButton.setBackground(btnActiveColor, true);
		} else {
			btnMaxButton.setBackground(btnInactiveColor, true);
		}
	}
}
