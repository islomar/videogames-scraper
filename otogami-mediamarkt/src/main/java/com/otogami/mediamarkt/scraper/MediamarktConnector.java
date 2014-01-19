package com.otogami.mediamarkt.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.otogami.core.model.Platform;
import com.otogami.core.model.Store;
import com.otogami.core.model.Videogame;

public class MediamarktConnector {

	private final static Logger LOG = LoggerFactory.getLogger(MediamarktConnector.class);

	public static final int NUMBER_OF_RESULTS_PER_PAGE = 50;

	private Platform platform;
	private WebClient webClient;
	private WebRequest webRequest;
	private MediamarktVideogamesParser parser;

	private boolean videogamesLeft = true;

	public MediamarktConnector(Platform platform) {

		this.platform = platform;
		this.webClient = createWebClient();
		this.webRequest = new WebRequest(MediamarktPlatformToUrlMapper.getHomePageFor(platform));
		this.parser = new MediamarktVideogamesParser();
		LOG.info("Created Mediamarkt connector for platform " + platform);
	}

	/**
	 * Get all the videogames existing in this pageNumber.
	 * 
	 * @param pageNumber
	 *            - page number to parse
	 * @return - list of videogames in this pageNumber
	 */
	public Set<Videogame> getVideogamesForPage(int pageNumber) {

		Set<Videogame> videogamesFound = new HashSet<Videogame>();

		try {

			HtmlPage page = getPage(pageNumber);

			List<HtmlAnchor> unparsedVideogameList = parser.getUnparsedVideogameList(page);

			if (unparsedVideogameList.size() > 0) {
				for (HtmlAnchor videogameHtmlAnchor : unparsedVideogameList) {

					String videogameURL = videogameHtmlAnchor.getHrefAttribute();
					HtmlPage videogameDetailedPage = getVideogameDetailedPage(page, videogameURL);

					Videogame videogame = getVideogame(videogameDetailedPage, page, videogameURL);
					LOG.info("Extracted " + videogame.toString());

					videogamesFound.add(videogame);
				}
			} else {
				videogamesLeft = false;
			}
		} catch (FailingHttpStatusCodeException e) {
			LOG.error("Error while parsing pageNumber " + pageNumber, e);
		} catch (IOException e) {
			LOG.error("Error while parsing pageNumber " + pageNumber, e);
		}

		return videogamesFound;
	}

	/**
	 * Are there still videogames pending to be scraped? Only used within
	 * single-threaded version.
	 * 
	 * @return <code>false</code> if the last parsing returned ZERO videogames,
	 *         <code>true</code> otherwise.
	 */
	public boolean isVideogamesLeft() {
		return videogamesLeft;
	}

	public int getTotalNumberOfPages() {

		int totalNumberOfPages = 0;
		try {
			HtmlPage page = getPage(1);
			int totalNumberOfVideogames = parser.getTotalNumberOfVideogames(page);
			LOG.info("Total number of videogames to be scraped: " + totalNumberOfVideogames);
			return (int) Math.ceil((double) totalNumberOfVideogames / NUMBER_OF_RESULTS_PER_PAGE);
		} catch (IOException e) {
			LOG.error("Error while gettint the total number of pages", e);
		}
		return totalNumberOfPages;
	}

	private WebClient createWebClient() {

		webClient = new WebClient();
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		return webClient;
	}

	private Videogame getVideogame(HtmlPage videogameDetailedPage, HtmlPage page, String videogameURL) throws MalformedURLException {

		Videogame videogame = parser.extractVideogameFrom(videogameDetailedPage);
		videogame.setPlatform(platform);
		videogame.setStore(Store.Mediamarkt);
		videogame.setUrl(page.getFullyQualifiedUrl(videogameURL).toString());
		return videogame;
	}

	private HtmlPage getPage(int pageNumber) throws IOException {

		webRequest.setRequestParameters(getRequestParameters(pageNumber));
		LOG.info("Getting URL " + webRequest.getUrl() + " with params " + webRequest.getRequestParameters());
		return webClient.getPage(webRequest);
	}

	private HtmlPage getVideogameDetailedPage(final HtmlPage page, String videogameURL) throws IOException, MalformedURLException {

		return webClient.getPage(page.getFullyQualifiedUrl(videogameURL));
	}

	private List<NameValuePair> getRequestParameters(int pageNumber) {

		List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();
		requestParameters.add(new NameValuePair("page", String.valueOf(pageNumber)));
		requestParameters.add(new NameValuePair("perPage", String.valueOf(NUMBER_OF_RESULTS_PER_PAGE)));
		return requestParameters;
	}

}
