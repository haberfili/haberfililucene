package service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/HealthCheck")
public class HealthCheck {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("healthCheck")
	public String addRegisteredProduct()
			throws Exception {
		return "OK";
	}

}
