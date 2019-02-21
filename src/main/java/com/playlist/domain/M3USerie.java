package com.playlist.domain;

/**
 * @author 62000465 2019-02-21
 *
 */
public class M3USerie extends M3UChannel implements Comparable< M3USerie >
{
	private String	strSerieName;
	private Integer	iSeason;
	private Integer	iEpisodio;
	private String	strNomeEpisodio;

	/**
	 * Getter for serieName
	 * 
	 * @author 62000465 2019-02-21
	 * @return the serieName {@link String}
	 */
	public String getSerieName()
	{
		return strSerieName;
	}

	/**
	 * Setter for serieName
	 * 
	 * @author 62000465 2019-02-21
	 * @param serieName the serieName to set
	 */
	public void setSerieName( String serieName )
	{
		strSerieName = serieName;
	}

	/**
	 * Getter for season
	 * 
	 * @author 62000465 2019-02-21
	 * @return the season {@link Integer}
	 */
	public Integer getSeason()
	{
		return iSeason;
	}

	/**
	 * Setter for season
	 * 
	 * @author 62000465 2019-02-21
	 * @param season the season to set
	 */
	public void setSeason( Integer season )
	{
		iSeason = season;
	}

	/**
	 * Getter for episodio
	 * 
	 * @author 62000465 2019-02-21
	 * @return the episodio {@link Integer}
	 */
	public Integer getEpisodio()
	{
		return iEpisodio;
	}

	/**
	 * Setter for episodio
	 * 
	 * @author 62000465 2019-02-21
	 * @param episodio the episodio to set
	 */
	public void setEpisodio( Integer episodio )
	{
		iEpisodio = episodio;
	}

	/**
	 * Getter for nomeEpisodio
	 * 
	 * @author 62000465 2019-02-21
	 * @return the nomeEpisodio {@link Integer}
	 */
	public String getNomeEpisodio()
	{
		return strNomeEpisodio;
	}

	/**
	 * Setter for nomeEpisodio
	 * 
	 * @author 62000465 2019-02-21
	 * @param nomeEpisodio the nomeEpisodio to set
	 */
	public void setNomeEpisodio( String nomeEpisodio )
	{
		strNomeEpisodio = nomeEpisodio;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "M3USerie [strSerieName=" + strSerieName + ", iSeason=" + iSeason + ", iEpisodio=" + iEpisodio + ", strNomeEpisodio=" +
			strNomeEpisodio + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo( M3USerie o )
	{
		if ( getSeason() != o.getSeason() )
		{
			return getEpisodio().compareTo( o.getEpisodio() );
		}
		return getSeason().compareTo( o.getSeason() );
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( iEpisodio == null ) ? 0 : iEpisodio.hashCode() );
		result = prime * result + ( ( iSeason == null ) ? 0 : iSeason.hashCode() );
		result = prime * result + ( ( strNomeEpisodio == null ) ? 0 : strNomeEpisodio.hashCode() );
		result = prime * result + ( ( strSerieName == null ) ? 0 : strSerieName.hashCode() );
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
		{
			return true;
		}
		if ( obj == null )
		{
			return false;
		}
		if ( getClass() != obj.getClass() )
		{
			return false;
		}
		M3USerie other = ( M3USerie ) obj;
		if ( iEpisodio == null )
		{
			if ( other.iEpisodio != null )
			{
				return false;
			}
		}
		else if ( !iEpisodio.equals( other.iEpisodio ) )
		{
			return false;
		}
		if ( iSeason == null )
		{
			if ( other.iSeason != null )
			{
				return false;
			}
		}
		else if ( !iSeason.equals( other.iSeason ) )
		{
			return false;
		}
		if ( strNomeEpisodio == null )
		{
			if ( other.strNomeEpisodio != null )
			{
				return false;
			}
		}
		else if ( !strNomeEpisodio.equals( other.strNomeEpisodio ) )
		{
			return false;
		}
		if ( strSerieName == null )
		{
			if ( other.strSerieName != null )
			{
				return false;
			}
		}
		else if ( !strSerieName.equals( other.strSerieName ) )
		{
			return false;
		}
		return true;
	}
	
	
}
