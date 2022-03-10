package de.hr.cms.edp.russiapenetrator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.resteasy.plugins.providers.multipart.OutputPart;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hr.cms.edp.security.DigitalSignature;

public class VerkehrskameraRussiaClient
{
	
	
	public static void main(String[] args) throws GeneralSecurityException, IOException
	{

		VerkehrskameraRussiaClient client = new VerkehrskameraRussiaClient();
		ObjectMapper mapper = new ObjectMapper();
		
		String[] cams = mapper.readValue(readData("cams.json"), String[].class);
		

//		for (int i = 1; i <= 16; i++)
//		{
//			String source = "iglz";
//			if (i < 10)
//			{
//				source += "0" + i;
//			}
//			else
//			{
//				source += i;
//			}
//			client.sendPost(source, readData("cam_fallback_preview.png"), readData("cam_fallback_preview.png"),
//			        readData("cam_fallback.mp4"), true, new Date());
//		}
//		for (int i = 1; i <= 56; i++)
//		{
//			String source = "vzh";
//			if (i < 10)
//			{
//				source += "0" + i;
//			}
//			else
//			{
//				source += i;
//			}
//			client.sendPost(source, readData("cam_fallback_preview.png"), readData("cam_fallback_preview.png"),
//			        readData("cam_fallback.mp4"), true, new Date());
//		}
//		
		long lastRun = System.currentTimeMillis() - 300001;
		
		while(true) {
			if(System.currentTimeMillis() - lastRun > 300000) {
				System.out.println("Beliefere EDP...");
				for(String camName : cams) {
					client.sendPost(camName, readData("cam_fallback_preview.png"), readData("cam_fallback_preview.png"),
							readData("cam_fallback.mp4"), true, new Date());
				}
				lastRun = System.currentTimeMillis();
			}
			
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void sendPost(String name, byte[] imagemedium, byte[] imagesmall, byte[] video, boolean online, Date date)
	        throws GeneralSecurityException, IOException
	{
		String[] urls = {"http://hrcms-edp-of.hr.de/edp/verkehrskameras/v1.0/" + name, "http://hrcms-edp-rod.hr.de/edp/verkehrskameras/v1.0/" + name};

		for(String url : urls) {
			ResteasyClient client = new ResteasyClientBuilder().build();
			
			ResteasyWebTarget target = client.target(url);
			
			MultipartFormDataOutput upload = new MultipartFormDataOutput();
			upload.addFormData("name", name, MediaType.TEXT_PLAIN_TYPE);
			upload.addFormData("online", online, MediaType.TEXT_PLAIN_TYPE);
			upload.addFormData("date", date.getTime(), MediaType.TEXT_PLAIN_TYPE);
			upload.addFormData("imagemedium", imagemedium, MediaType.TEXT_PLAIN_TYPE);
			upload.addFormData("imagesmall", imagesmall, MediaType.TEXT_PLAIN_TYPE);
			upload.addFormData("video", video, MediaType.TEXT_PLAIN_TYPE);
			
			//Objektliste für Normalisierung erstellen
			List<OutputPart> parts = upload.getParts();
			List<Object> list = new ArrayList<Object>();
			for(OutputPart part : parts){
				list.add(part.getEntity());
			}
			
			DigitalSignature ds = new DigitalSignature();
			ds.init();
			//Daten signieren
			byte[] signature = ds.signData(list, "private.key");
			
			upload.addFormData("signature", signature, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			
			GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(upload)
			{
			};
			Response response = target.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
			if (response.getStatus() != 200)
			{
				System.out.println(name + " to url "  + url + " failed : HTTP error code : " + response.getStatus());
				System.out.println(response.toString());
			} else {
				System.out.println(name + " erfolgreich an " + url + " beliefert.");
			}
			
			response.close();
		}
		
	}

	private static byte[] readData(String source)
	{

		try
		{
			return Files.readAllBytes(Paths.get("" + source));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
