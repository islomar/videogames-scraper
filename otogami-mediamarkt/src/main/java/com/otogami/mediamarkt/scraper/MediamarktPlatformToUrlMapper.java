package com.otogami.mediamarkt.scraper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otogami.core.model.Platform;

public class MediamarktPlatformToUrlMapper {

	private final static Logger LOG = LoggerFactory.getLogger(MediamarktPlatformToUrlMapper.class);

	private static final String MEDIAMARKT_HOME = "http://tiendas.mediamarkt.es";

	private static final Map<Platform, URL> URL_PLATFORM_MAPPER = new HashMap<Platform, URL>();

	private static final String N3DS_HOME_PAGE = MEDIAMARKT_HOME + "/3ds/sc/juegos-3ds";
	private static final String PC_HOME_PAGE = MEDIAMARKT_HOME + "/pc/sc/juegos-pc";
	private static final String PS3_HOME_PAGE = MEDIAMARKT_HOME + "/sony-ps3/sc/juegos-ps3";
	private static final String PS4_HOME_PAGE = MEDIAMARKT_HOME + "/sony-ps4/sc/juegos-sony-ps4-10001596";
	private static final String PSVITA_HOME_PAGE = MEDIAMARKT_HOME + "/sony-ps-vita/sc/juegos-ps-vita";
	private static final String WII_HOME_PAGE = MEDIAMARKT_HOME + "/nintendo-wii/sc/juegos-nintendo-wii";
	private static final String WIIU_HOME_PAGE = MEDIAMARKT_HOME + "/nintendo-wii-u/sc/juegos-wii-u";
	private static final String XBOX_360_HOME_PAGE = MEDIAMARKT_HOME + "/xbox-360/sc/juegos-xbox-360";
	private static final String XBOX_ONE_HOME_PAGE = MEDIAMARKT_HOME + "/xbox-one/sc/juegos-es";

	static {
		try {
			URL_PLATFORM_MAPPER.put(Platform.n3ds, new URL(N3DS_HOME_PAGE));
			URL_PLATFORM_MAPPER.put(Platform.pc, new URL(PC_HOME_PAGE));
			URL_PLATFORM_MAPPER.put(Platform.ps3, new URL(PS3_HOME_PAGE));
			URL_PLATFORM_MAPPER.put(Platform.ps4, new URL(PS4_HOME_PAGE));
			URL_PLATFORM_MAPPER.put(Platform.psvita, new URL(PSVITA_HOME_PAGE));
			URL_PLATFORM_MAPPER.put(Platform.wii, new URL(WII_HOME_PAGE));
			URL_PLATFORM_MAPPER.put(Platform.wiiu, new URL(WIIU_HOME_PAGE));
			URL_PLATFORM_MAPPER.put(Platform.xbox360, new URL(XBOX_360_HOME_PAGE));
			URL_PLATFORM_MAPPER.put(Platform.xboxone, new URL(XBOX_ONE_HOME_PAGE));
		} catch (MalformedURLException e) {
			LOG.error("Error while mapping the platforms to its corresponding homepage", e);
		}
	}

	private MediamarktPlatformToUrlMapper() {
	}

	public static URL getHomePageFor(Platform platform) {
		return URL_PLATFORM_MAPPER.get(platform);
	}

}
