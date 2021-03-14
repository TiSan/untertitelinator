package de.tisan.church.untertitelinator.churchtools.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.tisan.church.untertitelinator.churchtools.api.objects.Agenda;
import de.tisan.church.untertitelinator.churchtools.api.objects.AgendaResponse;
import de.tisan.church.untertitelinator.churchtools.api.objects.Event;
import de.tisan.church.untertitelinator.churchtools.api.objects.EventResponse;
import de.tisan.church.untertitelinator.churchtools.api.objects.EventResponse2;
import de.tisan.church.untertitelinator.churchtools.api.objects.Service;
import de.tisan.church.untertitelinator.churchtools.api.objects.ServiceResponse;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tools.persistencemanager.JSONPersistence;
import de.tisan.tools.persistencemanager.SubTypeReference;

public class ChurchToolsApi {
	private static ChurchToolsApi instance;
	private static final String CACHE_EVENTS = "EVENTS";
	private static final String CACHE_EVENTDETAIL = "EVENTDETAIL";
	private static final String CACHE_SERVICES = "SERVICES";
	private static final String CACHE_AGENDAS = "AGENDAS";

	private String accessToken;
	private Client client;
	private String baseUrl = "https://oberstedten.church.tools/";
	private ObjectMapper mapper;
	private String user;
	private String password;

	private Map<String, CacheEntry> cache;

	public static ChurchToolsApi get() {
		return (instance == null ? (instance = new ChurchToolsApi()) : instance);
	}

	private ChurchToolsApi() {
		this.client = ResteasyClientBuilder.newClient();
		this.mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		this.user = (String) JSONPersistence.get().getSetting(UTPersistenceConstants.CHURCHTOOLSUSER, "ctuser");
		this.password = (String) JSONPersistence.get().getSetting(UTPersistenceConstants.CHURCHTOOLSPASSWORD,
				"ctpassword");
		this.baseUrl = (String) JSONPersistence.get().getSetting(UTPersistenceConstants.CHURCHTOOLSBASEURL,
				"https://oberstedten.church.tools");

		String accessToken = JSONPersistence.get().getSetting(UTPersistenceConstants.CTLASTACCESSTOKEN, "<ACCESSTOKEN>",
				String.class);
		long accessTokenUpdate = JSONPersistence.get().getSetting(UTPersistenceConstants.CTLASTACCESSTOKENUPDATED,
				System.currentTimeMillis(), Long.class);
		if (accessToken.equals("<ACCESSTOKEN>") == false && (System.currentTimeMillis() - accessTokenUpdate <= 600000)) {
			this.accessToken = new String(Base64.getDecoder().decode(accessToken));
			System.out.println("ChurchTools Access Token found. Use this.");
		}
		this.cache = new HashMap<String, CacheEntry>();
		Map<String, Map<String, String>> cacheTmp = JSONPersistence.get().getSetting(UTPersistenceConstants.CTCACHE,
				new HashMap<String, Map<String, String>>(), new SubTypeReference<Map<String, Map<String, String>>>() {
				});
		for (String key : cacheTmp.keySet()) {
			this.cache.put(key, mapper.convertValue(cacheTmp.get(key), CacheEntry.class));
		}

	}

	public void login() {
		try {
			Form f = new Form();
			f.param("func", "getUserLoginToken");
			f.param("email", user);
			f.param("password", password);
			Entity<Form> entity = Entity.form(f);

			Response response = client.target(baseUrl).queryParam("q", "login/ajax").request(MediaType.APPLICATION_JSON)
					.post(entity);

			System.out.println("Status: " + response.getStatus());
			String token;
			String rString = (String) response.readEntity(String.class);
			System.out.println(rString);
			JsonNode rStatus = mapper.readTree(rString);
			if (rStatus.get("status").asText().equalsIgnoreCase("fail")) {
				JOptionPane.showMessageDialog(null,
						"Es konnten keine Events bei Churchtools geladen werden. Fehler: "
								+ rStatus.get("data").asText(),
						"Fehler beim ChurchTools Login", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			token = rStatus.get("data").get("token").asText();

			System.out.println("Token: " + token);
			this.accessToken = token;
			JSONPersistence.get().setSetting(UTPersistenceConstants.CTLASTACCESSTOKEN,
					Base64.getEncoder().encodeToString(token.getBytes()));
			JSONPersistence.get().setSetting(UTPersistenceConstants.CTLASTACCESSTOKENUPDATED,
					System.currentTimeMillis());

		} catch (Exception e) {
			System.out
					.println("FEHLER! Der AccessToken konnte nicht erfolgreich vom ChurchTools Server geholt werden.");
		}
	}

	public Optional<List<Event>> getEvents() {
		// TODO https://oberstedten.church.tools/api/events/409 Event anreichern!
		if (accessToken == null) {
			System.out.println("FEHLER! Der AccessToken ist nicht gesetzt.");
			login();
			// return Optional.empty();
		}
		try {
			String response = null;
			CacheEntry entry = cache.get(CACHE_EVENTS);
			if (entry != null && isValid(entry)) {
				response = entry.getDataDecoded();
			} else {
				response = client.target(baseUrl).path("api").path("events").queryParam("login_token", accessToken)
						.request(MediaType.APPLICATION_JSON).get().readEntity(String.class);
				saveIntoCache(CACHE_EVENTS, response);
			}

			EventResponse r = mapper.readValue(response, new TypeReference<EventResponse>() {
			});
			List<Event> rawEvents = r.getData();
			List<Event> enrichedEvents = new ArrayList<Event>();

			for (Event rawEvent : rawEvents) {
				String response2 = null;
				CacheEntry entry2 = cache.get(CACHE_EVENTDETAIL + "_" + rawEvent.getId());
				if (entry2 != null && isValid(entry2)) {
					response2 = entry2.getDataDecoded();
				} else {
					response2 = client.target(baseUrl).path("api").path("events").path("" + rawEvent.getId())
							.queryParam("login_token", accessToken).request(MediaType.APPLICATION_JSON).get()
							.readEntity(String.class);
					saveIntoCache(CACHE_EVENTDETAIL + "_" + rawEvent.getId(), response2);
				}
				EventResponse2 r2 = mapper.readValue(response2, new TypeReference<EventResponse2>() {
				});
				enrichedEvents.add(r2.getData());

			}

			return Optional.ofNullable(enrichedEvents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public Optional<List<Service>> getServices() {
		if (accessToken == null) {
			System.out.println("FEHLER! Der AccessToken ist nicht gesetzt.");
			login();
			// return Optional.empty();
		}
		try {
			String response = null;
			CacheEntry entry = cache.get(CACHE_SERVICES);
			if (entry != null && isValid(entry)) {
				response = entry.getDataDecoded();
			} else {
				response = client.target(baseUrl).path("api").path("services").queryParam("login_token", accessToken)
						.request(MediaType.APPLICATION_JSON).get().readEntity(String.class);
				saveIntoCache(CACHE_SERVICES, response);
			}
			ServiceResponse r = mapper.readValue(response, new TypeReference<ServiceResponse>() {
			});
			return Optional.ofNullable(r.getData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public Optional<Event> getNextEvent() {
		if (accessToken == null) {
			System.out.println("FEHLER! Der AccessToken ist nicht gesetzt.");
			login();
		}
		Optional<List<Event>> allEvents = getEvents();
		if (allEvents.isPresent() && allEvents.get().size() > 0) {
			return Optional.ofNullable(allEvents.get().get(0));
		} else {
			return Optional.empty();
		}
	}

	public Optional<Agenda> getAgendaForEvent(long id) {
		if (accessToken == null) {
			System.out.println("FEHLER! Der AccessToken ist nicht gesetzt.");
			return Optional.empty();
		}
		try {
			String response = null;
			CacheEntry entry = cache.get(CACHE_AGENDAS);
			if (entry != null && isValid(entry)) {
				response = entry.getDataDecoded();
			} else {
				response = client.target(baseUrl).path("api").path("events").path("" + id).path("agenda")
						.queryParam("login_token", accessToken).request(MediaType.APPLICATION_JSON).get()
						.readEntity(String.class);
				saveIntoCache(CACHE_AGENDAS, response);
			}
			AgendaResponse r = mapper.readValue(response, new TypeReference<AgendaResponse>() {
			});
			return Optional.ofNullable(r.getData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private boolean isValid(CacheEntry entry) {
		return (System.currentTimeMillis() - entry.created) <= 600000;
	}

	private void saveIntoCache(String key, String value) {
		cache.put(key, new CacheEntry(Base64.getEncoder().encodeToString(value.getBytes())));
		JSONPersistence.get().setSetting(UTPersistenceConstants.CTCACHE, cache);
	}

}
