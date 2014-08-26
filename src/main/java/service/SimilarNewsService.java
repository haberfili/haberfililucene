package service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import container.NewsContainer;
import container.SimilarNewsFinder;

@Path("/SimilarNews")
public class SimilarNewsService {



	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findSimilarNews/{id}")
	public String findSimilarNews(@PathParam("id") String id)throws Exception {
		
		NewsContainer.queue.add(id);
		if(!NewsContainer.running){
			NewsContainer.running=true;
			Runnable r= new SimilarNewsFinder();
			new Thread(r).start();
		}
		return "OK";
	}

	
}

