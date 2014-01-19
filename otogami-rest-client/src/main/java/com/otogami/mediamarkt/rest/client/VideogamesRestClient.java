package com.otogami.mediamarkt.rest.client;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;

public class VideogamesRestClient {

	private final static Logger LOG = LoggerFactory.getLogger(VideogamesRestClient.class);

	private Client client;

	public VideogamesRestClient() {

		client = ClientBuilder.newBuilder().register(JacksonFeatures.class).property(ClientProperties.FOLLOW_REDIRECTS, false)
				.property(ClientProperties.CONNECT_TIMEOUT, 10 * 1000).property(ClientProperties.READ_TIMEOUT, 10 * 1000).build();
	}

	public Response doGet(URI uri) throws RestClientException {

		LOG.info("> doGet uri={}", uri);
		try {
			Response response = client.target(uri).request(MediaType.APPLICATION_JSON).get(Response.class);
			LOG.info("< doGet uri={} httpStatusCode={}", uri, response.getStatus());
			return response;
		} catch (RuntimeException e) {
			throw new RestClientException("error getting document " + uri, e);
		}
	}

	public Response doPut(URI uri, Object bodyToPut) throws RestClientException {

		LOG.info("> doPut uri={} objectToPut={}", new Object[] { uri, bodyToPut });
		try {
			Response response = client.target(uri).request(MediaType.APPLICATION_JSON)
					.put(Entity.entity(bodyToPut, MediaType.APPLICATION_JSON), Response.class);
			LOG.info("< doPut uri={} httpStatusCode= {}", uri, response.getStatus());
			return response;
		} catch (RuntimeException e) {
			throw new RestClientException("error posting document uri=" + uri, e);
		}
	}

}
