package com.otogami.mediamarkt.rest.client;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Ignore;
import org.junit.Test;

import com.otogami.core.model.Availability;
import com.otogami.core.model.Platform;
import com.otogami.core.model.Store;
import com.otogami.core.model.Videogame;

public class VideogamesRestClientIntegrationTest {

	@Test
	public void healthcheck() throws RestClientException, URISyntaxException {
		VideogamesRestClient restClient = new VideogamesRestClient();

		Response response = restClient.doGet(new URI("http://localhost:8080/otogami-server/rest/videogame/healthcheck"));
		assertTrue(response.getStatus() == Status.OK.getStatusCode());
	}

	@Ignore
	@Test
	public void assertPostingOneVideogame() throws RestClientException, URISyntaxException {
		VideogamesRestClient restClient = new VideogamesRestClient();

		Response response = restClient.doPut(new URI("http://localhost:8080/otogami-server/rest/videogame/update"), createVideogameList(300));
		assertTrue(response.getStatus() == Status.OK.getStatusCode());
	}

	@Test
	public void assertGettingVideogame() throws RestClientException, URISyntaxException {
		VideogamesRestClient restClient = new VideogamesRestClient();

		Response response = restClient.doGet(new URI("http://localhost:8080/otogami-server/rest/videogame"));
		assertTrue(response.getStatus() == Status.OK.getStatusCode());
		assertTrue(response.hasEntity());
		System.out.println(response.getEntity());
		List entity = response.readEntity(List.class);
		System.out.println("Size received: " + entity.size());
	}

	@Ignore("To be activated when when could also remove the inserted rows (pending DELETE method)")
	@Test
	public void assertPosting() throws RestClientException, URISyntaxException {
		VideogamesRestClient restClient = new VideogamesRestClient();

		Response response = restClient.doPut(new URI("http://localhost:8080/otogami-server/rest/videogame"), createVideogameList(10));
		assertTrue(response.getStatus() == Status.OK.getStatusCode());
	}

	private Collection<Videogame> createVideogameList(int numberOfVideogamesToCreate) {

		Collection<Videogame> videogames = new ArrayList<Videogame>();
		for (int id = 0; id < numberOfVideogamesToCreate; id++) {
			videogames.add(createVideogame(id));
		}
		return videogames;
	}

	private Videogame createVideogame(int id) {

		Videogame videogame = new Videogame();
		videogame.setId(String.valueOf("TestGame" + id));
		videogame.setPrice(new BigDecimal(5387));
		videogame.setAvailability(Availability.OutofStock);
		videogame.setStore(Store.Mediamarkt);
		videogame.setTitle("This is the test game number " + id);
		videogame.setPlatform(Platform.ps3);
		return videogame;
	}

}
