package de.tisan.church.untertitelinator.server.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.lukasdietrich.lambdatra.reaction.http.Query;
import com.lukasdietrich.lambdatra.reaction.http.WrappedRequest;
import com.lukasdietrich.lambdatra.reaction.http.WrappedResponse;

import de.tisan.church.untertitelinator.ccli.CCLIParser;
import de.tisan.church.untertitelinator.server.WebsiteListener;

public class CCLIAcceptSong extends WebsiteListener {

	public CCLIAcceptSong() {
		super("accept");
	}

	@Override
	public Object onRequest(String[] data, Query[] queries, WrappedRequest<Map<String, String>> req,
			WrappedResponse<Map<String, String>> res) throws IOException {
		System.out.println(Arrays.toString(data));
		System.out.println(Arrays.toString(queries));
		
		System.out.println(req);
		System.out.println(res);
		
		return "YES";

	}

}
