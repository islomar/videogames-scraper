package com.otogami.server.rest;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otogami.core.model.Availability;
import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;
import com.otogami.server.model.VideogameEntity;
import com.otogami.server.service.VideogameService;

@Path("/videogame")
public class VideogameRestController {

	private final static Logger LOG = LoggerFactory.getLogger(VideogameRestController.class);

	@GET
	@Path("/healthcheck")
	public Response healthcheck() {

		LOG.info("Healthcheck successful");
		return Response.ok().build();
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByCriteria(@QueryParam("title") String title, @QueryParam("availability") Availability availability,
			@QueryParam("price") BigDecimal price, @QueryParam("platform") Platform platform) {

		LOG.info("Getting all videogames matching {}, {}, {}, {}", title, availability, price, platform);
		Videogame searchCriteria = new Videogame(platform, availability, title, price);
		VideogameService service = new VideogameService();
		List<VideogameEntity> videogames = service.findByCriteria(searchCriteria);
		LOG.info("Found {} videogames, it should be returned!!", videogames.size());
		return Response.ok().entity(videogames).build();
	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Collection<Videogame> videogameList) {

		LOG.info("Updating {} videogames...", videogameList.size());

		VideogameService service = new VideogameService();
		try {
			service.update(videogameList);
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

}
