package de.hr.cms.edp.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;

/**
 * setzt die HTTP-Header für Basic-Authentifizierung
 * 
 * @author all
 */
public class BasicAuthenticationFilter implements ClientRequestFilter {
	
	
	String user;

	String passwd;

	public BasicAuthenticationFilter(String user, String passwd)
	{
		this.user = user;
		this.passwd = passwd;
	}

	public void filter(ClientRequestContext requestContext) throws IOException {
		MultivaluedMap<String, Object> headers = requestContext.getHeaders();
		headers.add("Authorization", getBasicAuthentication());
	}

	private String getBasicAuthentication() {
		String token = user + ":" + passwd;
		try {
			return "BASIC "
					+ DatatypeConverter.printBase64Binary(token
							.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException("Cannot encode with UTF-8", ex);
		}
	}
}
