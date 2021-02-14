package de.tisan.church.untertitelinator.churchtools.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.tisan.church.untertitelinator.churchtools.api.objects.Agenda;
import de.tisan.church.untertitelinator.churchtools.api.objects.AgendaItem;
import de.tisan.church.untertitelinator.churchtools.api.objects.AgendaResponse;
import de.tisan.church.untertitelinator.churchtools.api.objects.Event;
import de.tisan.church.untertitelinator.churchtools.api.objects.EventResponse;
import de.tisan.church.untertitelinator.churchtools.api.objects.EventResponse2;
import de.tisan.church.untertitelinator.churchtools.api.objects.Service;
import de.tisan.church.untertitelinator.churchtools.api.objects.ServiceResponse;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class ChurchToolsApi {
	private static ChurchToolsApi instance;

	private String accessToken;
	private Client client;
	private String baseUrl = "https://oberstedten.church.tools/";
	private ObjectMapper mapper;
	private String user;
	private String password;

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

	}

	public static void main(String[] args) {
		ChurchToolsApi.get().login();
		List<Event> events = ChurchToolsApi.get().getEvents().get();
		System.out.println(events);
		for (Event e : events) {
			System.out.println(e.getName() + ": " + e.getDescription() + "(time: " + e.getStartDate().toString() + ")");
			for (AgendaItem i : ChurchToolsApi.get().getAgendaForEvent(e.getId()).orElse(new Agenda()).getItems()) {
				System.out.println(i.getTitle());
			}
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
			token = mapper.readTree(rString).get("data").get("token").asText();

			System.out.println("Token: " + token);
			this.accessToken = token;
		} catch (IOException e) {
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
			Response response = client.target(baseUrl).path("api").path("events").queryParam("login_token", accessToken)
					.request(MediaType.APPLICATION_JSON).get();
			EventResponse r = mapper.readValue((String) response.readEntity(String.class),
					new TypeReference<EventResponse>() {
					});
			List<Event> rawEvents = r.getData();
			List<Event> enrichedEvents = new ArrayList<Event>();

			for (Event rawEvent : rawEvents) {
				Response responseEnriched = client.target(baseUrl).path("api").path("events")
						.path("" + rawEvent.getId()).queryParam("login_token", accessToken)
						.request(MediaType.APPLICATION_JSON).get();
				EventResponse2 r2 = mapper.readValue((String) responseEnriched.readEntity(String.class),
						new TypeReference<EventResponse2>() {
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
			Response response = client.target(baseUrl).path("api").path("services")
					.queryParam("login_token", accessToken).request(MediaType.APPLICATION_JSON).get();
			ServiceResponse r = mapper.readValue((String) response.readEntity(String.class),
					new TypeReference<ServiceResponse>() {
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
			Response response = client.target(baseUrl).path("api").path("events").path("" + id).path("agenda")
					.queryParam("login_token", accessToken).request(MediaType.APPLICATION_JSON).get();
			AgendaResponse r = mapper.readValue((String) response.readEntity(String.class),
					new TypeReference<AgendaResponse>() {
					});
			return Optional.ofNullable(r.getData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
