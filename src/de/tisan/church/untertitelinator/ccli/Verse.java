package de.tisan.church.untertitelinator.ccli;

import java.util.List;

public class Verse {
	private String name;
	private List<String> lines;

	public Verse() {
	}

	public Verse(String name, List<String> lines) {
		super();
		this.name = name;
		this.lines = lines;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	@Override
	public String toString() {
		String s = "";
		for (String sss : lines) {
			s += sss + "\n";
		}
		return s;
	}
}
