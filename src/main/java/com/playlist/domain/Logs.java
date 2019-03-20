package com.playlist.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 62000465 2019-03-20
 *
 */
public class Logs
{
	private List< Log > lstLogs = new ArrayList<>();

	/**
	 * Getter for logs
	 * 
	 * @author 62000465 2019-03-20
	 * @return the logs {@link List<Log>}
	 */
	public List< Log > getLogs()
	{
		return lstLogs;
	}

	/**
	 * Setter for logs
	 * 
	 * @author 62000465 2019-03-20
	 * @param logs the logs to set
	 */
	public void setLogs( List< Log > logs )
	{
		lstLogs = logs;
	}

	/**
	 * The <b>addLog</b> method returns {@link void}
	 * 
	 * @author 62000465 2019-03-20
	 * @param strMessage
	 */
	public void addLog( String strMessage )
	{
		lstLogs.add( new Log( strMessage ) );
	}
}
