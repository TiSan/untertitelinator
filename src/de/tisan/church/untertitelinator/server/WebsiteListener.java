package de.tisan.church.untertitelinator.server;
import java.util.Map;

import com.lukasdietrich.lambdatra.reaction.http.Query;
import com.lukasdietrich.lambdatra.reaction.http.WrappedRequest;
import com.lukasdietrich.lambdatra.reaction.http.WrappedResponse;

public abstract class WebsiteListener {
	private String ref;

	public WebsiteListener(String ref) {
		this.ref = ref;
	}

	public String getReferrer() {
		return ref;
	}

	public abstract Object onRequest(String[] data, Query[] queries, WrappedRequest<Map<String, String>> req, WrappedResponse<Map<String, String>> res) throws Exception;
}
