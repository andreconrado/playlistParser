
package com.playlist.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "season", "episode", "name", "air_date" })
public class Episode {

	@JsonProperty("season")
	private Integer season;
	@JsonProperty("episode")
	private Integer episode;
	@JsonProperty("name")
	private String name;
	@JsonProperty("air_date")
	private String airDate;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Episode() {
	}

	/**
	 * 
	 * @param airDate
	 * @param episode
	 * @param season
	 * @param name
	 */
	public Episode(Integer season, Integer episode, String name, String airDate) {
		super();
		this.season = season;
		this.episode = episode;
		this.name = name;
		this.airDate = airDate;
	}

	@JsonProperty("season")
	public Integer getSeason() {
		return season;
	}

	@JsonProperty("season")
	public void setSeason(Integer season) {
		this.season = season;
	}

	public Episode withSeason(Integer season) {
		this.season = season;
		return this;
	}

	@JsonProperty("episode")
	public Integer getEpisode() {
		return episode;
	}

	@JsonProperty("episode")
	public void setEpisode(Integer episode) {
		this.episode = episode;
	}

	public Episode withEpisode(Integer episode) {
		this.episode = episode;
		return this;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	public Episode withName(String name) {
		this.name = name;
		return this;
	}

	@JsonProperty("air_date")
	public String getAirDate() {
		return airDate;
	}

	@JsonProperty("air_date")
	public void setAirDate(String airDate) {
		this.airDate = airDate;
	}

	public Episode withAirDate(String airDate) {
		this.airDate = airDate;
		return this;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public Episode withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("season", season).append("episode", episode).append("name", name)
				.append("airDate", airDate).append("additionalProperties", additionalProperties).toString();
	}

}
