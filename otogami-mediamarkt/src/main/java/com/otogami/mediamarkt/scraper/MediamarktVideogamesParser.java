package com.otogami.mediamarkt.scraper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.otogami.core.model.Availability;
import com.otogami.core.model.Videogame;

public class MediamarktVideogamesParser {
	
	private static final String WHITESPACE = " ";
	private static final String PRE_ORDER = "Pre-Order";
	
	private Videogame videogame;

	public Videogame extractVideogameFrom(HtmlPage videogameDetailedPage) {

		videogame = new Videogame();
		videogame.setAvailability(getAvailability(videogameDetailedPage));
		videogame.setPrice(getPrice(videogameDetailedPage));
		videogame.setTitle(getTitle(videogameDetailedPage));
		videogame.setId(getProductId(videogameDetailedPage));
		
		setPreorderAvailabilityIfNeeded();

		return videogame;
	}

	private Availability getAvailability(HtmlPage page) {

		List<HtmlLink> availability = (List<HtmlLink>) page.getByXPath("//link[contains(@itemprop, 'availability')]");
		if (CollectionUtils.isNotEmpty(availability)) {
			if (availability.get(0).getHrefAttribute().contains(Availability.InStock.toString())) {
				return Availability.InStock;
			}
		}
		return Availability.OutofStock;
	}

	private BigDecimal getPrice(HtmlPage page) {

		List<HtmlMeta> priceHtmlMetaList = (List<HtmlMeta>) page.getByXPath("//meta[contains(@itemprop, 'price')]");
		if (CollectionUtils.isNotEmpty(priceHtmlMetaList)) {
			String price = priceHtmlMetaList.get(0).getAttribute("content");
			return new BigDecimal(StringUtils.substringBefore(price, WHITESPACE));
		}
		return null;
	}

	private String getTitle(HtmlPage page) {

		List<HtmlHeading1> titleHtmlMetaList = (List<HtmlHeading1>) page.getByXPath("//h1[contains(@itemprop, 'name')]");
		if (CollectionUtils.isNotEmpty(titleHtmlMetaList)) {
			return titleHtmlMetaList.get(0).getTextContent();
		}
		return null;
	}

	private void setPreorderAvailabilityIfNeeded() {
		if (titleContainsPreorderText()) {
			this.videogame.setAvailability(Availability.Preorder);
		}
	}

	private boolean titleContainsPreorderText() {
		return StringUtils.isNotBlank(videogame.getTitle()) && videogame.getTitle().contains(PRE_ORDER);
	}

	private String getProductId(HtmlPage page) {

		List<HtmlHiddenInput> productIdList = (List<HtmlHiddenInput>) page.getByXPath("//input[contains(@name, 'productId')]");
		if (CollectionUtils.isNotEmpty(productIdList)) {
			return productIdList.get(0).getValueAttribute();
		}
		return null;
	}

	public List<HtmlAnchor> getUnparsedVideogameList(HtmlPage page) {
		return (List<HtmlAnchor>) page.getByXPath("//a[contains(@class, 'product9MoreInformation')]");
	}
	
	public int getTotalNumberOfVideogames(HtmlPage page) {

		List<HtmlDivision> resumePaginatorList = (List<HtmlDivision>)page.getByXPath("//*[contains(@class, 'resumePaginator')]");
		String resumePaginatorTextContent = resumePaginatorList.get(0).getTextContent();
		if (StringUtils.isNotBlank(resumePaginatorTextContent)) {
			return Integer.valueOf(resumePaginatorTextContent.split("de")[1].trim());
		}
		return 0;
	}

}
