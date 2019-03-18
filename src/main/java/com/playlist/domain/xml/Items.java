package com.playlist.domain.xml;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author 62000465 2019-03-18
 *
 */
@JacksonXmlRootElement( localName = "items" )
public class Items
{
	@JacksonXmlElementWrapper( useWrapping = false )
	private List< Channel > channel = new ArrayList< Channel >();

	public List< Channel > getChannel()
	{
		return channel;
	}

	@Override
	public String toString()
	{
		return "Items [channel = " + channel + "]";
	}
}
