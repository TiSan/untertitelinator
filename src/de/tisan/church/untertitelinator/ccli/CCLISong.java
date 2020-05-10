package de.tisan.church.untertitelinator.ccli;

import java.util.ArrayList;
import java.util.List;

public class CCLISong {
	private String title;
	private String editor;
	private String version;
	private String author;
	private String ccliId;
	private String copyright;
	private List<Verse> verses;
	private List<String> verseOrder;

	public CCLISong() {
		this.verseOrder = new ArrayList<String>();
		this.verses = new ArrayList<Verse>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCcliId() {
		return ccliId;
	}

	public void setCcliId(String ccliId) {
		this.ccliId = ccliId;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public List<Verse> getVerses() {
		return verses;
	}

	public void setVerses(List<Verse> verses) {
		this.verses = verses;
	}

	public void setVerseOrder(String verseOrder) {
		String[] spl = verseOrder.split(",");
		this.verseOrder.clear();
		for (String s : spl) {
			this.verseOrder.add(s);
		}
	}

	public List<String> getVerseOrder() {
		return verseOrder;
	}
}
