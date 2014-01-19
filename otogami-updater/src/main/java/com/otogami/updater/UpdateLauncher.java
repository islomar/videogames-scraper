package com.otogami.updater;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otogami.core.model.Platform;
import com.otogami.core.model.Store;
import com.otogami.core.model.Videogame;
import com.otogami.mediamarkt.rest.client.RestClientException;
import com.otogami.mediamarkt.rest.client.VideogamesRestClient;
import com.otogami.mediamarkt.scraper.robots.MediamarktConcurrentRobot;

public class UpdateLauncher {

	private final static Logger LOG = LoggerFactory.getLogger(UpdateLauncher.class);

	private Collection<Videogame> videogames;

	public static void main(String[] args) {
		new UpdateLauncher().go();
	}

	public void go() {

		try {
			getAllVideogamesFrom(Store.Mediamarkt, Platform.ps3);
			saveVideogames();
		} catch (OperationNotSupportedException e) {
			LOG.error(e.getMessage());
		}
	}

	private void getAllVideogamesFrom(Store mediamarkt, Platform platform) throws OperationNotSupportedException {

		LOG.info("Getting all the videogames for store {} and platform {}", mediamarkt, platform);

		switch (mediamarkt) {
		case Mediamarkt:
			MediamarktConcurrentRobot robot = new MediamarktConcurrentRobot();
			videogames = robot.getVideogamesOnPlatform(platform);
			return;
		default:
			throw new OperationNotSupportedException("Currently, the only store allowed is Mediamarkt!");
		}
	}

	private void saveVideogames() {

		VideogamesRestClient restClient = new VideogamesRestClient();

		try {
			URI updateURI = new URI("http://localhost:8080/otogami-server/rest/videogame/update");
			restClient.doPut(updateURI, videogames);
		} catch (RestClientException e) {
			LOG.error("Problems when accessing the REST service", e);
		} catch (URISyntaxException e) {
			LOG.error("Problems when accessing the REST service", e);
		}
	}
}
