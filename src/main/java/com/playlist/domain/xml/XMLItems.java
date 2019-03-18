package com.playlist.domain.xml;

/**
 * @author 62000465 2019-03-18
 *
 */
public class XMLItems
{
	private Items items = new Items();

	public Items getItems()
	{
		return items;
	}

	public void setItems( Items items )
	{
		this.items = items;
	}

	@Override
	public String toString()
	{
		return "XMLItems [items = " + items + "]";
	}
}
