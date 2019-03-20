package com.playlist.domain;

import java.time.Instant;

/**
 * @author 62000465 2019-03-20
 *
 */
public class Log
{
	private Instant	creationDate	= Instant.now();
	private String	strMessage;

	/**
	 * constructor
	 * 
	 * @author 62000465 2019-03-20
	 * @param message
	 */
	public Log( String message )
	{
		super();
		strMessage = message;
	}

	/**
	 * Getter for creationDate
	 * 
	 * @author 62000465 2019-03-20
	 * @return the creationDate {@link Instant}
	 */
	public Instant getCreationDate()
	{
		return creationDate;
	}

	/**
	 * Setter for creationDate
	 * 
	 * @author 62000465 2019-03-20
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate( Instant creationDate )
	{
		this.creationDate = creationDate;
	}

	/**
	 * Getter for message
	 * 
	 * @author 62000465 2019-03-20
	 * @return the message {@link String}
	 */
	public String getMessage()
	{
		return strMessage;
	}

	/**
	 * Setter for message
	 * 
	 * @author 62000465 2019-03-20
	 * @param message the message to set
	 */
	public void setMessage( String message )
	{
		strMessage = message;
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
		builder.append( "Log [creationDate=" );
		builder.append( creationDate );
		builder.append( ", strMessage=" );
		builder.append( strMessage );
		builder.append( "]" );
		return builder.toString();
	}
}
