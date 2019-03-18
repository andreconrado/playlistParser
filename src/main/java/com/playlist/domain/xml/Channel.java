package com.playlist.domain.xml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;

/**
 * @author 62000465 2019-03-18
 *
 */
@JsonPropertyOrder(
{ "title", "description", "stream_url" } )
public class Channel
{
	@JacksonXmlCData
	private String	description;
	@JacksonXmlCData
	private String	title;
	@JacksonXmlCData
	private String	stream_url;
	@JacksonXmlCData
	private String	group;

	/**
	 * Getter for description
	 * 
	 * @author 62000465 2019-03-18
	 * @return the description {@link String}
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Setter for description
	 * 
	 * @author 62000465 2019-03-18
	 * @param description the description to set
	 */
	public void setDescription( String description )
	{
		this.description = description;
	}

	/**
	 * Getter for title
	 * 
	 * @author 62000465 2019-03-18
	 * @return the title {@link String}
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Setter for title
	 * 
	 * @author 62000465 2019-03-18
	 * @param title the title to set
	 */
	public void setTitle( String title )
	{
		this.title = title;
	}

	/**
	 * Getter for stream_url
	 * 
	 * @author 62000465 2019-03-18
	 * @return the stream_url {@link String}
	 */
	public String getStream_url()
	{
		return stream_url;
	}

	/**
	 * Setter for stream_url
	 * 
	 * @author 62000465 2019-03-18
	 * @param stream_url the stream_url to set
	 */
	public void setStream_url( String stream_url )
	{
		this.stream_url = stream_url;
	}

	/**
	 * Getter for group
	 * 
	 * @author 62000465 2019-03-18
	 * @return the group {@link String}
	 */
	public String getGroup()
	{
		return group;
	}

	/**
	 * Setter for group
	 * 
	 * @author 62000465 2019-03-18
	 * @param group the group to set
	 */
	public void setGroup( String group )
	{
		this.group = group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "Channel [description=" );
		builder.append( description );
		builder.append( ", title=" );
		builder.append( title );
		builder.append( ", stream_url=" );
		builder.append( stream_url );
		builder.append( ", group=" );
		builder.append( group );
		builder.append( "]" );
		return builder.toString();
	}
}
