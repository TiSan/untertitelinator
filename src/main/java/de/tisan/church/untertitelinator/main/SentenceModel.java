package de.tisan.church.untertitelinator.main;

import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import de.tisan.church.untertitelinator.data.Song;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class SentenceModel extends AbstractTableModel {
	private static final long serialVersionUID = -2951927435443254091L;
	private String[] columnNames = new String[] { "Aktiv", "Vers" };
	private Object[][] data = new Object[0][2];
	private JTable table;

	public SentenceModel(JTable table) {
		this.table = table;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void changeSong(Song song, int currentIndex) {
		data = new String[song.getSongLines().size()][2];
		int index = 0;
		for (String line : song.getSongLines()) {
			if (index == currentIndex) {
				data[index][0] = "*";
			} else {
				data[index][0] = "";
			}
			data[index][1] = line.replaceAll("\n",
					(String) JSONPersistence.get().getSetting(UTPersistenceConstants.LINESEPARATOR, "<BR>"));

			index++;
		}
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(500);
		fireTableDataChanged();
	}

	public void scrollToVisible(JTable table, int rowIndex, int vColIndex) {
		Rectangle cellRect = table.getCellRect(rowIndex, 0, true);

		table.scrollRectToVisible(cellRect);

	}

}