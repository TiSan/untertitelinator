package de.tisan.church.untertitelinator.ccli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CCLIParser {

	public static CCLISong parseSong(File file) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		CCLISong song = new CCLISong();

		String line = null;
		List<String> collectedLines = new ArrayList<String>();
		String titleVerse = null;
		while ((line = r.readLine()) != null) {

			if (line.equalsIgnoreCase("---")) {
				if (collectedLines.size() > 0) {
					song.getVerses().add(new Verse(titleVerse, collectedLines));
				}
				titleVerse = null;
				collectedLines = new ArrayList<String>();
				continue;
			}

			if (line.startsWith("#")) {
				String[] spl = line.split("=");
				switch (spl[0].toLowerCase()) {
				case "#title":
					song.setTitle(spl[1]);
					break;
				case "#editor":
					song.setEditor(spl[1]);
					break;
				case "#version":
					song.setVersion(spl[1]);
					break;
				case "#author":
					song.setAuthor(spl[1]);
					break;
				case "#ccli":
					song.setCcliId(spl[1]);
					break;
				case "#(c)":
					song.setCopyright(spl[1]);
					break;
				case "#verseorder":
					song.setVerseOrder(spl[1]);
					break;
				default:
					break;
				}
				continue;
			}

			if (titleVerse == null) {
				titleVerse = line;
				continue;
			}
			collectedLines.add(line);

		}
		r.close();
		song.getVerses().add(new Verse(titleVerse, collectedLines));
		return song;
	}
}
