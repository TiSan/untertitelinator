package de.tisan.church.untertitelinator.server.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.lukasdietrich.lambdatra.reaction.http.Query;
import com.lukasdietrich.lambdatra.reaction.http.WrappedRequest;
import com.lukasdietrich.lambdatra.reaction.http.WrappedResponse;

import de.tisan.church.untertitelinator.ccli.CCLIParser;
import de.tisan.church.untertitelinator.server.WebsiteListener;

public class CCLISongParserTest extends WebsiteListener {

	public CCLISongParserTest() {
		super("cclitest");
	}

	@Override
	public Object onRequest(String[] data, Query[] queries, WrappedRequest<Map<String, String>> req,
			WrappedResponse<Map<String, String>> res) throws IOException {
		System.out.println(data);
		System.out.println(queries);

		return CCLIParser.parseSong(new File("ccli/10000reasons.sng"));

	}

}
