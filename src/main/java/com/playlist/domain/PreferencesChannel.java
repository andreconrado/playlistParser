package com.playlist.domain;

/**
 * @author anco62000465 2017-09-27
 *
 */
public class PreferencesChannel
{
	private String	name	= "";
	private String	logo	= "";
	private String	group	= "";
	private String	regex	= "";
	private int		id		= 0;
	private boolean	disable	= false;

	/**
	 * Getter for name <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 *
	 * @return the name {@link String}
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Setter for name <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 *
	 * @param name the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * Getter for logo <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 *
	 * @return the logo {@link String}
	 */
	public String getLogo()
	{
		return logo;
	}

	/**
	 * Setter for logo <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 *
	 * @param logo the logo to set
	 */
	public void setLogo( String logo )
	{
		this.logo = logo;
	}

	/**
	 * Getter for group <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 *
	 * @return the group {@link String}
	 */
	public String getGroup()
	{
		return group;
	}

	/**
	 * Setter for group <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 *
	 * @param group the group to set
	 */
	public void setGroup( String group )
	{
		this.group = group;
	}

	/**
	 * Getter for disable <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 * 
	 * @return the disable {@link boolean}
	 */
	public boolean isDisable()
	{
		return disable;
	}

	/**
	 * Setter for disable <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 * 
	 * @param disable the disable to set
	 */
	public void setDisable( boolean disable )
	{
		this.disable = disable;
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
	 * @return the regex
	 */
	public String getRegex()
	{
		return regex;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex( String regex )
	{
		this.regex = regex;
	}
}
