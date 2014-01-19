package com.otogami.frontend.facade;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otogami.core.model.Videogame;
import com.otogami.frontend.exception.VideogameSearchException;
import com.otogami.mediamarkt.rest.client.RestClientException;
import com.otogami.mediamarkt.rest.client.VideogamesRestClient;

public class VideogameSearchFacade {

	private final static Logger LOG = LoggerFactory.getLogger(VideogameSearchFacade.class);

	private final static String URI_GET_VIDEOGAME = "http://localhost:8080/otogami-server/rest/videogame";

	public List<Videogame> findVideogames(Videogame criteria) throws VideogameSearchException {
		VideogamesRestClient restClient = new VideogamesRestClient();

		try {
			URI uri = buildUri(criteria);
			Response response = restClient.doGet(uri);
			LOG.info("status={}, hasEntity={}", response.getStatus(), response.hasEntity());
			if (response.getStatus() == Status.OK.getStatusCode() && response.hasEntity()) {
				LOG.info("We got something! Surprise, surprise!!");
				List<Videogame> videogames = response.readEntity(List.class);
				LOG.info("Number of videogames received: " + videogames.size());
				return videogames;
			} else {
				LOG.error("HTTP status code {} received: something went wrong!!", response.getStatus());
				LOG.error("Nothing received, something went wrong!!");
				throw new VideogameSearchException("Error while trying to retrieve the games...");
			}
		} catch (RestClientException e) {
			String errorMessage = "Problems when accessing the REST service";
			LOG.error(errorMessage, e);
			throw new VideogameSearchException(errorMessage, e);
		} catch (URISyntaxException e) {
			String errorMessage = "Problems in the URI syntax";
			LOG.error(errorMessage, e);
			throw new VideogameSearchException(errorMessage, e);
		}
	}

	private URI buildUri(Videogame criteria) throws URISyntaxException {
		UriBuilder uriBuilder = UriBuilder.fromUri(URI_GET_VIDEOGAME);
		fillTitleParam(uriBuilder, criteria);
		fillPlatformParam(uriBuilder, criteria);
		fillAvailabilityParam(uriBuilder, criteria);
		fillPriceParam(uriBuilder, criteria);
		LOG.info("URI built: " + uriBuilder.toString());
		return uriBuilder.build();
	}

	private void fillTitleParam(UriBuilder uriBuilder, Videogame criteria) {
		if (StringUtils.isNotBlank(criteria.getTitle())) {
			uriBuilder.queryParam("title", criteria.getTitle());
		}
	}

	private void fillPlatformParam(UriBuilder uriBuilder, Videogame criteria) {
		if (criteria.getPlatform() != null) {
			uriBuilder.queryParam("platform", criteria.getPlatform());
		}
	}

	private void fillAvailabilityParam(UriBuilder uriBuilder, Videogame criteria) {
		if (criteria.getAvailability() != null) {
			uriBuilder.queryParam("availability", criteria.getAvailability());
		}
	}

	private void fillPriceParam(UriBuilder uriBuilder, Videogame criteria) {
		BigDecimal price = criteria.getPrice();
		if (price != null && price.intValue() != 0) {
			uriBuilder.queryParam("price", price);
		}
	}
}
