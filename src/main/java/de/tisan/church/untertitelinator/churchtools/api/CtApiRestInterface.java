package de.tisan.church.untertitelinator.churchtools.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.tisan.church.untertitelinator.churchtools.api.objects.EventResponse;

@Path("api")
public interface CtApiRestInterface {
	
	@GET
	@Path("events")
	public EventResponse getEvents(@QueryParam("login_token") String loginToken);
	
	
}
