package com.playlist.domain;

import org.apache.commons.lang3.StringUtils;
import j2html.TagCreator;

/**
 * @author anco62000465 2017-09-27
 *
 */
public class M3UChannel
{
	// #EXTINF:-1,RTP 1 HD
	// http://megaiptv.dynu.com:6969/live/AndreConrado/F8MYMoq33L/2438.ts
	private String	url;
	private int		id	= 0;
	private String	name;
	private String	groupName;
	private String	image;

	/**
	 * Getter for url <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-27
	 * 
	 * @return the url {@link String}
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * Setter for url <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-27
	 * 
	 * @param url the url to set
	 */
	public void setUrl( String url )
	{
		this.url = url;
	}

	/**
	 * Getter for name <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-27
	 * 
	 * @return the name {@link String}
	 */
	public String getName()
	{
		if ( StringUtils.isNotBlank( image ) )
		{
			return TagCreator.img().attr( "src", image ).render() + name;
		}
		else
		{
			return name;
		}
	}

	/**
	 * Setter for name <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-27
	 * 
	 * @param name the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * Getter for groupName <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 * 
	 * @return the groupName {@link String}
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * Setter for groupName <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 * 
	 * @param groupName the groupName to set
	 */
	public void setGroupName( String groupName )
	{
		this.groupName = groupName;
	}

	/**
	 * Getter for id <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 * 
	 * @return the id {@link int}
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Setter for id <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 * 
	 * @param id the id to set
	 */
	public void setId( int id )
	{
		this.id = id;
	}

	/**
	 * Getter for image
	 * 
	 * @author 62000465 2019-03-18
	 * @return the image {@link String}
	 */
	public String getImage()
	{
		if ( StringUtils.isNotBlank( image ) )
		{
			return TagCreator.img().attr( "src", image ).render() + name;
		}
		else
		{
			return "";
		}
	}

	/**
	 * Setter for image
	 * 
	 * @author 62000465 2019-03-18
	 * @param image the image to set
	 */
	public void setImage( String image )
	{
		this.image = image;
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
		builder.append( "M3UChannel [url=" );
		builder.append( url );
		builder.append( ", id=" );
		builder.append( id );
		builder.append( ", name=" );
		builder.append( name );
		builder.append( ", groupName=" );
		builder.append( groupName );
		builder.append( ", image=" );
		builder.append( image );
		builder.append( "]" );
		return builder.toString();
	}

}
