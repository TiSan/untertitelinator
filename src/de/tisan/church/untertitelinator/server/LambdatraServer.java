package de.tisan.church.untertitelinator.server;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lukasdietrich.lambdatra.Lambdatra;
import com.lukasdietrich.lambdatra.reaction.http.Query;

import de.tisan.church.untertitelinator.settings.JSONPersistence;
import de.tisan.church.untertitelinator.settings.PersistenceConstants;
import io.netty.handler.codec.http.HttpResponseStatus;

public class LambdatraServer {
	public static LambdatraServer get() {
		return (instance == null ? (instance = new LambdatraServer()) : instance);
	}

	private static LambdatraServer instance;
	private ArrayList<WebsiteListener> listeners;
	private ObjectMapper mapper;

	public LambdatraServer addListener(WebsiteListener l) {
		this.listeners.add(l);
		return this;
	}

	private LambdatraServer() {

		this.listeners = new ArrayList<WebsiteListener>();
		this.mapper = new ObjectMapper();
		this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
		this.mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		startServer();

	}

	private void startServer() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Lambdatra.create((Integer) JSONPersistence.get().getSetting(PersistenceConstants.SERVERPORT, 8080),
						resource -> {
							resource.on("/*", (req, res) -> {
								System.out.println("Request page... " + req.getPath() + "   ");
								String path = req.getPath();
								path = path.substring(path.indexOf("/") + 1);

								boolean b = false;

								for (WebsiteListener l : listeners) {
									try {
										String lPath = l.getReferrer();
										String path1 = path;
										if (lPath.contains("*")) {
											lPath = lPath.substring(0, lPath.indexOf("*") - 1);
											if (lPath.length() > path1.length()) {
												continue;
											}
											path1 = path1.substring(0, lPath.length());
										}
										if (lPath.equalsIgnoreCase(path1)) {
											Object response = l.onRequest(path.substring(1).split("/"),
													req.getQueries().toArray(new Query[req.getQueries().size()]), req,
													res);
											if (response != null) {
												b = true;
												res.setStatus(HttpResponseStatus.OK);
												res.setHeader("Content-Type", "application/json");
												res.write(mapper.writeValueAsString(response));
												return;
											}
										}
									} catch (Exception ex) {
										res.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
										res.setHeader("Content-Type", "application/json");
										res.write("{err: 500, message: \"" + ex.getMessage() + "\"}\n");
										ex.printStackTrace();
										return;
									}

								}
								if (!b) {
									res.setStatus(HttpResponseStatus.NOT_FOUND);
									res.setHeader("Content-Type", "application/json");
									res.write("{err: 404}\n");
								}
							});
						});
			}
		}).start();

	}
}
