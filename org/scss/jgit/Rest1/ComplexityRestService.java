package org.scss.jgit.Rest1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
 
import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
@Path("/")
public class ComplexityRestService {
	@POST
	@Path("/crunchifyService")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crunchifyREST(InputStream incomingData) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + crunchifyBuilder.toString());
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(crunchifyBuilder.toString()).build();
	}
 
	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService(InputStream incomingData) {
		String result = "ComplexityRestService Successfully started..";
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
	
	@POST
	@Path("/getComplexityCount")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getComplexityCountRest(InputStream incomingData)
	{
		StringBuilder complexityBuilder = new StringBuilder();
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while((line = in.readLine()) != null){
				complexityBuilder.append(line);
			}
		} catch(Exception e )
		{
			System.out.println("Error Parsing: -");
		}
		
		System.out.println("Data Received :" + complexityBuilder.toString());
		return Response.status(200).entity(complexityBuilder.toString()).build();
	}
 
}