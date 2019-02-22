
package com.playlist.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "id", "name", "permalink", "url", "description", "description_source", "start_date", "end_date",
		"country", "status", "runtime", "network", "youtube_link", "image_path", "image_thumbnail_path", "rating",
		"rating_count", "countdown", "genres", "pictures", "episodes" })
public class TvShow_ {

	@JsonProperty("id")
	private Integer id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("permalink")
	private String permalink;
	@JsonProperty("url")
	private String url;
	@JsonProperty("description")
	private String description;
	@JsonProperty("description_source")
	private Object descriptionSource;
	@JsonProperty("start_date")
	private String startDate;
	@JsonProperty("end_date")
	private Object endDate;
	@JsonProperty("country")
	private String country;
	@JsonProperty("status")
	private String status;
	@JsonProperty("runtime")
	private Integer runtime;
	@JsonProperty("network")
	private String network;
	@JsonProperty("youtube_link")
	private String youtubeLink;
	@JsonProperty("image_path")
	private String imagePath;
	@JsonProperty("image_thumbnail_path")
	private String imageThumbnailPath;
	@JsonProperty("rating")
	private String rating;
	@JsonProperty("rating_count")
	private String ratingCount;
	@JsonProperty("countdown")
	private Object countdown;
	@JsonProperty("genres")
	private List<String> genres = new ArrayList<>();
	@JsonProperty("pictures")
	private List<String> pictures = new ArrayList<>();
	@JsonProperty("episodes")
	private List<Episode> episodes = new ArrayList<>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public TvShow_() {
	}

	/**
	 * 
	 * @param imageThumbnailPath
	 * @param startDate
	 * @param genres
	 * @param youtubeLink
	 * @param ratingCount
	 * @param imagePath
	 * @param status
	 * @param pictures
	 * @param runtime
	 * @param descriptionSource
	 * @param endDate
	 * @param episodes
	 * @param url
	 * @param network
	 * @param country
	 * @param id
	 * @param countdown
	 * @param permalink
	 * @param description
	 * @param name
	 * @param rating
	 */
	public TvShow_(Integer id, String name, String permalink, String url, String description, Object descriptionSource,
			String startDate, Object endDate, String country, String status, Integer runtime, String network,
			String youtubeLink, String imagePath, String imageThumbnailPath, String rating, String ratingCount,
			Object countdown, List<String> genres, List<String> pictures, List<Episode> episodes) {
		super();
		this.id = id;
		this.name = name;
		this.permalink = permalink;
		this.url = url;
		this.description = description;
		this.descriptionSource = descriptionSource;
		this.startDate = startDate;
		this.endDate = endDate;
		this.country = country;
		this.status = status;
		this.runtime = runtime;
		this.network = network;
		this.youtubeLink = youtubeLink;
		this.imagePath = imagePath;
		this.imageThumbnailPath = imageThumbnailPath;
		this.rating = rating;
		this.ratingCount = ratingCount;
		this.countdown = countdown;
		this.genres = genres;
		this.pictures = pictures;
		this.episodes = episodes;
	}

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	public TvShow_ withId(Integer id) {
		this.id = id;
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

	public TvShow_ withName(String name) {
		this.name = name;
		return this;
	}

	@JsonProperty("permalink")
	public String getPermalink() {
		return permalink;
	}

	@JsonProperty("permalink")
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public TvShow_ withPermalink(String permalink) {
		this.permalink = permalink;
		return this;
	}

	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	public TvShow_ withUrl(String url) {
		this.url = url;
		return this;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	public TvShow_ withDescription(String description) {
		this.description = description;
		return this;
	}

	@JsonProperty("description_source")
	public Object getDescriptionSource() {
		return descriptionSource;
	}

	@JsonProperty("description_source")
	public void setDescriptionSource(Object descriptionSource) {
		this.descriptionSource = descriptionSource;
	}

	public TvShow_ withDescriptionSource(Object descriptionSource) {
		this.descriptionSource = descriptionSource;
		return this;
	}

	@JsonProperty("start_date")
	public String getStartDate() {
		return startDate;
	}

	@JsonProperty("start_date")
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public TvShow_ withStartDate(String startDate) {
		this.startDate = startDate;
		return this;
	}

	@JsonProperty("end_date")
	public Object getEndDate() {
		return endDate;
	}

	@JsonProperty("end_date")
	public void setEndDate(Object endDate) {
		this.endDate = endDate;
	}

	public TvShow_ withEndDate(Object endDate) {
		this.endDate = endDate;
		return this;
	}

	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	@JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}

	public TvShow_ withCountry(String country) {
		this.country = country;
		return this;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public TvShow_ withStatus(String status) {
		this.status = status;
		return this;
	}

	@JsonProperty("runtime")
	public Integer getRuntime() {
		return runtime;
	}

	@JsonProperty("runtime")
	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}

	public TvShow_ withRuntime(Integer runtime) {
		this.runtime = runtime;
		return this;
	}

	@JsonProperty("network")
	public String getNetwork() {
		return network;
	}

	@JsonProperty("network")
	public void setNetwork(String network) {
		this.network = network;
	}

	public TvShow_ withNetwork(String network) {
		this.network = network;
		return this;
	}

	@JsonProperty("youtube_link")
	public String getYoutubeLink() {
		return youtubeLink;
	}

	@JsonProperty("youtube_link")
	public void setYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
	}

	public TvShow_ withYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
		return this;
	}

	@JsonProperty("image_path")
	public String getImagePath() {
		return imagePath;
	}

	@JsonProperty("image_path")
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public TvShow_ withImagePath(String imagePath) {
		this.imagePath = imagePath;
		return this;
	}

	@JsonProperty("image_thumbnail_path")
	public String getImageThumbnailPath() {
		return imageThumbnailPath;
	}

	@JsonProperty("image_thumbnail_path")
	public void setImageThumbnailPath(String imageThumbnailPath) {
		this.imageThumbnailPath = imageThumbnailPath;
	}

	public TvShow_ withImageThumbnailPath(String imageThumbnailPath) {
		this.imageThumbnailPath = imageThumbnailPath;
		return this;
	}

	@JsonProperty("rating")
	public String getRating() {
		return rating;
	}

	@JsonProperty("rating")
	public void setRating(String rating) {
		this.rating = rating;
	}

	public TvShow_ withRating(String rating) {
		this.rating = rating;
		return this;
	}

	@JsonProperty("rating_count")
	public String getRatingCount() {
		return ratingCount;
	}

	@JsonProperty("rating_count")
	public void setRatingCount(String ratingCount) {
		this.ratingCount = ratingCount;
	}

	public TvShow_ withRatingCount(String ratingCount) {
		this.ratingCount = ratingCount;
		return this;
	}

	@JsonProperty("countdown")
	public Object getCountdown() {
		return countdown;
	}

	@JsonProperty("countdown")
	public void setCountdown(Object countdown) {
		this.countdown = countdown;
	}

	public TvShow_ withCountdown(Object countdown) {
		this.countdown = countdown;
		return this;
	}

	@JsonProperty("genres")
	public List<String> getGenres() {
		return genres;
	}

	@JsonProperty("genres")
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public TvShow_ withGenres(List<String> genres) {
		this.genres = genres;
		return this;
	}

	@JsonProperty("pictures")
	public List<String> getPictures() {
		return pictures;
	}

	@JsonProperty("pictures")
	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

	public TvShow_ withPictures(List<String> pictures) {
		this.pictures = pictures;
		return this;
	}

	@JsonProperty("episodes")
	public List<Episode> getEpisodes() {
		return episodes;
	}

	@JsonProperty("episodes")
	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}

	public TvShow_ withEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
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

	public TvShow_ withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).append("name", name).append("permalink", permalink)
				.append("url", url).append("description", description).append("descriptionSource", descriptionSource)
				.append("startDate", startDate).append("endDate", endDate).append("country", country)
				.append("status", status).append("runtime", runtime).append("network", network)
				.append("youtubeLink", youtubeLink).append("imagePath", imagePath)
				.append("imageThumbnailPath", imageThumbnailPath).append("rating", rating)
				.append("ratingCount", ratingCount).append("countdown", countdown).append("genres", genres)
				.append("pictures", pictures).append("episodes", episodes)
				.append("additionalProperties", additionalProperties).toString();
	}

}
