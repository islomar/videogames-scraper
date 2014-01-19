package com.otogami.mediamarkt.scraper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;
import com.otogami.mediamarkt.scraper.MediamarktConnector;

public class MediamarktConnectorIntegrationTest {

	private static final WebClient webClient = new WebClient();
	private static final Platform PLATFORM_PS3 = Platform.ps3;

	private MediamarktConnector mediamarktConnector;

	
	@Before
	public void setUp() {
		mediamarktConnector = new MediamarktConnector(PLATFORM_PS3);
		initWebClient();
	}

	private void initWebClient() {
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
	}

	@Test
	public void assertAccessToMediamarktHomepage() throws Exception {
		final HtmlPage page = webClient.getPage("http://tiendas.mediamarkt.es");

		final String pageAsText = page.asText();
		assertTrue(pageAsText.contains("Consolas y videojuegos"));

		webClient.closeAllWindows();
	}
	
	@Test
	public void assertThatAVideogameScrapedContainsAllTheImportantInformation() {
		Set<Videogame> videogamesForPage1 = mediamarktConnector.getVideogamesForPage(1);
		
		assertTrue(videogamesForPage1.size() > 0);
		
		Videogame videogame = videogamesForPage1.iterator().next();
		
		assertTrue(StringUtils.isNotBlank(videogame.getId()));
		assertTrue(StringUtils.isNotBlank(videogame.getTitle()));
		assertThat(videogame.getAvailability(), is(notNullValue()));
		assertTrue(StringUtils.isNotBlank(videogame.getUrl()));
		assertEquals(PLATFORM_PS3, videogame.getPlatform());
	}
}
