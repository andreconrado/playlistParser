
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
@JsonPropertyOrder({ "tvShow" })
public class TvShow {

	@JsonProperty("tvShow")
	private TvShow_ tvShow;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public TvShow() {
	}

	/**
	 * 
	 * @param tvShow
	 */
	public TvShow(TvShow_ tvShow) {
		super();
		this.tvShow = tvShow;
	}

	@JsonProperty("tvShow")
	public TvShow_ getTvShow() {
		return tvShow;
	}

	@JsonProperty("tvShow")
	public void setTvShow(TvShow_ tvShow) {
		this.tvShow = tvShow;
	}

	public TvShow withTvShow(TvShow_ tvShow) {
		this.tvShow = tvShow;
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

	public TvShow withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("tvShow", tvShow).append("additionalProperties", additionalProperties)
				.toString();
	}

}
