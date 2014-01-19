package com.otogami.core.model;

import java.math.BigDecimal;

import com.google.common.base.Objects;

public class Videogame {

	private String id;
	private String title;
	private Platform platform;
	private String url;
	private Availability availability;
	private BigDecimal price;
	private Store store;

	public Videogame() {
	}

	public Videogame(Platform platform, Availability availability, String title, BigDecimal price) {
		this.platform = platform;
		this.title = title;
		this.availability = availability;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Availability getAvailability() {
		return availability;
	}

	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@Override
	public String toString() {

		return Objects.toStringHelper(this).add("id", id).add("store", store).add("platform", platform).add("price", price).add("title", title)
				.add("url", url).add("availability", availability).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Videogame other = (Videogame) obj;
		return Objects.equal(id, other.id) && Objects.equal(store, other.store);
	}

}
