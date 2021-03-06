package com.playlist.domain;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

/**
 * @author anco62000465 2017-09-29
 *
 */
public class Preferences
{
	private HashMap< String, PreferencesChannel > preferences = new HashMap<>();

	/**
	 * Getter for preferences <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 *
	 * @return the preferences {@link HashMap<String,PreferencesChannel>}
	 */
	public HashMap< String, PreferencesChannel > getPreferences()
	{
		return preferences;
	}

	/**
	 * Setter for preferences <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 *
	 * @param preferences the preferences to set
	 */
	public void setPreferences( HashMap< String, PreferencesChannel > preferences )
	{
		this.preferences = preferences;
	}

	/**
	 *
	 * The <b>findPreferenceChannel</b> method returns {@link Optional<PreferencesChannel>} <br>
	 * <br>
	 * <b>author</b> anco62000465 2017-09-29
	 *
	 * @param m3uChannel.getName()
	 * @return
	 */
	public Optional< PreferencesChannel > findPreferenceChannel( M3UChannel m3uChannel )
	{
		for ( Entry< String, PreferencesChannel > entry : preferences.entrySet() )
		{
			String channelKey = entry.getKey();
			PreferencesChannel value = entry.getValue();
			if ( m3uChannel.getName().toLowerCase().contains( channelKey.toLowerCase() ) )
			{
				return Optional.of( value );
			}
			if ( StringUtils.isNotBlank( value.getRegex() ) )
			{
				Pattern pattern = Pattern.compile( value.getRegex() );
				Matcher matcher = pattern.matcher( m3uChannel.getName() );
				if ( matcher.find() )
				{
					return Optional.of( value );
				}
			}
			if ( m3uChannel.getGroupName().toLowerCase().contains( "Filme".toLowerCase() ) ||
				m3uChannel.getGroupName().toLowerCase().contains( "Serie".toLowerCase() ) ||
				m3uChannel.getGroupName().toLowerCase().contains( "Fimes".toLowerCase() ) )
			{
				value.setGroup( WordUtils.capitalizeFully( m3uChannel.getGroupName().trim() ) );
				return Optional.of( value );
			}
		}
		return Optional.empty();
	}
}
