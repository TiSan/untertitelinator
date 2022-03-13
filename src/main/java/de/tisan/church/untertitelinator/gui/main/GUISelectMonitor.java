package de.tisan.church.untertitelinator.gui.main;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tisan.church.untertitelinator.instancer.packets.Monitor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ftitlebar.DefaultFlatTitleBarListener;
import de.tisan.flatui.components.ftitlebar.FlatTitleBarWin10;

public class GUISelectMonitor extends JFrame {

	private static final long serialVersionUID = 8232356324203743683L;

	public static void main(String[] args) {
		new GUISelectMonitor(null).setVisible(true);
	}

	public GUISelectMonitor(List<Monitor> monitorList) {
		try {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setUndecorated(true);
			setBounds(0, 0, 800, 300);

			JPanel contentPane = new JPanel();
			contentPane.setLayout(null);
			setContentPane(contentPane);
			contentPane.setBackground(FlatColors.BACKGROUND);
			FlatLayoutManager.get(this).setResizable(false);
			FlatTitleBarWin10 bar = new FlatTitleBarWin10(FlatLayoutManager.get(this), "Monitorauswahl");
			bar.setBounds(0, 0, getWidth(), 30);
			bar.addFlatTitleBarListener(new DefaultFlatTitleBarListener(this));
			bar.setMaximizable(false);
			bar.setMinimizable(false);
			contentPane.add(bar);
			
			MonitorPainter painter = new MonitorPainter(monitorList);
			painter.setBounds(10, 40, getWidth(), getHeight());
			contentPane.add(painter);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
