package com.otogami.server.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.common.base.Objects;
import com.otogami.core.model.Videogame;

@Entity(name = "videogame")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "storeGameId", "storeId" }) })
@SequenceGenerator(name = "SEQ_VIDEOGAME_ID", initialValue = 1)
public class VideogameEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VIDEOGAME_ID")
	private Long id;
	// Original game id in the store
	private String storeGameId;
	// Game Title
	private String title;
	// Game Platform as String
	private String platform;
	// Complete game Url
	private String url;
	// Game Availability as String
	private String availability;
	// Game Price. Null if it doesn't have
	private BigDecimal price;
	// Id or Name of the store
	private String storeId;

	public VideogameEntity() {
	}

	public VideogameEntity(Videogame videogame) {
		buildEntityFrom(videogame);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStoreGameId() {
		return storeGameId;
	}

	public void setStoreGameId(String storeGameId) {
		this.storeGameId = storeGameId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public void buildEntityFrom(Videogame videogame) {
		setStoreGameId(videogame.getId());
		setPlatform(videogame.getPlatform().toString());
		setPrice(videogame.getPrice());
		setAvailability(videogame.getAvailability().toString());
		setTitle(videogame.getTitle());
		setUrl(videogame.getUrl());
		setStoreId(videogame.getStore().toString());
		// TODO - Create table STORE, with STORE.ID and STORE.NAME;
	}

	@Override
	public String toString() {

		return Objects.toStringHelper(this).add("id", id).add("storeId", storeId).add("storeGameId", storeGameId).add("platform", platform)
				.add("price", price).add("url", url).toString();
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VideogameEntity other = (VideogameEntity) obj;
		return Objects.equal(id, other.id) && Objects.equal(storeId, other.storeId);
	}

}
