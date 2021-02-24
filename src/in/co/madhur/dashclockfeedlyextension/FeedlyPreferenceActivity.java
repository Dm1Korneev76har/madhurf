package in.co.madhur.dashclockfeedlyextension;

import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;

import com.google.android.apps.dashclock.configuration.AppChooserPreference;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.TextUtils;

public class FeedlyPreferenceActivity extends PreferenceActivity
{
	private AppPreferences appPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_layout);

		appPreferences = new AppPreferences(this);

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		SetListeners();

		UpdateLabel((ListPreference) findPreference(Keys.SYNC_INTERVAL.key), null);
		UpdateLabel((ListPreference) findPreference(Keys.MINIMUM_UNREAD.key), null);
	}

	protected void UpdateLabel(ListPreference listPreference, String newValue)
	{

		if (newValue == null)
		{
			newValue = listPreference.getValue();
		}

		int index = listPreference.findIndexOfValue(newValue);
		if (index != -1)
		{
			newValue = (String) listPreference.getEntries()[index];
			listPreference.setTitle(newValue);
		}

	}

	protected void SetListeners()
	{

		final Keys clickIntentKey = Keys.CLICK_INTENT;

		CharSequence intentSummary = AppChooserPreference.getDisplayValue(this, appPreferences.getMetadata(clickIntentKey));
		getPreferenceScreen().findPreference(clickIntentKey.key).setSummary(TextUtils.isEmpty(intentSummary)
				|| intentSummary.equals(getString(R.string.pref_shortcut_default)) ? ""
				: intentSummary);

		getPreferenceScreen().findPreference(clickIntentKey.key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				CharSequence intentSummary = AppChooserPreference.getDisplayValue(getBaseContext(), newValue.toString());
				getPreferenceScreen().findPreference(clickIntentKey.key).setSummary(TextUtils.isEmpty(intentSummary)
						|| intentSummary.equals(getResources().getString(R.string.pref_shortcut_default)) ? ""
						: intentSummary);
				return true;
			}

		});
	}

}
