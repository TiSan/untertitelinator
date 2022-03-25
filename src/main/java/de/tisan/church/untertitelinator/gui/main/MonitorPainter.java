package de.tisan.church.untertitelinator.gui.main;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTInstance;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.Monitor;
import de.tisan.church.untertitelinator.instancer.packets.MonitorListPacket;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatUI;

public class MonitorPainter extends JPanel {
	private static final long serialVersionUID = 6424235908522469165L;
	List<Monitor> monitors;
	int selectedMonitorIndex = -1;

	public MonitorPainter(MonitorListPacket monitorList) {
		this.monitors= monitorList.getMonitorList();
		monitors.stream().forEach(Monitor::preRender);
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Point mousePoint = e.getPoint();
				if (mousePoint.y < 150) {
					selectedMonitorIndex = mousePoint.x / 210;
					repaint();
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {

			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(selectedMonitorIndex >= 0 && selectedMonitorIndex < monitors.size()) {
					if(monitorList.getInstanceType().equals(UTInstance.KEYER)) {
						UTEventHub.get().publish(new CommandPacket(Command.KEYER_CHANGE_DISPLAY, monitors.get(selectedMonitorIndex).getName()));
						
					} else if(monitorList.getInstanceType().equals(UTInstance.PRESENTATOR)) {
						UTEventHub.get().publish(new CommandPacket(Command.PRESENTATOR_CHANGE_DISPLAY, monitors.get(selectedMonitorIndex).getName()));
						
					}
				}
			}

		});
	}

	@Override
	protected void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				!FlatUI.isFastRendering() ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setColor(FlatColors.BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());
		int x = 0;
		int y = 20;
		int width = 200;
		int height = 100;
		int standThickness = 10;
		int thickness = 5;
		int index = 0;
		for (Monitor monitor : monitors) {
			g.setColor(index == selectedMonitorIndex ? FlatColors.RED : FlatColors.WHITE);

			g.drawString(monitor.getName() + " (" + monitor.getBounds()[0] + "x" + monitor.getBounds()[1] + ")",
					x + thickness, y / 2);
			g.drawImage(monitor.getPrerenderedImage(), x + thickness, y, null);
			g.setStroke(new BasicStroke(5));
			g.drawRoundRect(x + thickness, y, width, height, thickness, thickness);

			g.fillRect(x + thickness + (width / 2 - (standThickness / 2)), y + height, standThickness, 50);

			g.fillRoundRect(x + thickness + (width / 2 - (width / 4)), y + height + 50, width / 2, standThickness / 2,
					thickness, thickness);

			x += width + 10;
			index++;
		}
	}
}
