package in.co.madhur.dashclockfeedlyextension;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.apps.dashclock.configuration.AppChooserPreference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class AppPreferences
{
	private Context context;
	private SharedPreferences sharedPreferences;

	public AppPreferences(Context context)
	{

		this.context = context;
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public enum Keys
	{
		SELECTED_VALUES_NOTIFICATIONS("selected_values_notifications"),
		NOTIFICATION_SEEK_STATES("notifications_seek_states"),
		ENABLE_NOTIFICATIONS("enable_notifications"),
		ENABLE_SOUND("enable_sound"),
		ENABLE_VIBRATE("enable_vibrate"),
		ENABLE_LED("enable_led"),
		CLICK_INTENT("widget_click_intent"),
		NOTIFICATION_CLICK_INTENT("notification_click_intent"),
		SYNC_INTERVAL("sync_interval"),
		MINIMUM_UNREAD("minimum_unread"),
		ENABLE_AUTOSYNC("enable_autosync"),
		LAST_SUCCESSFUL_SYNC("last_successful_sync"),
		ACCESS_TOKEN("access_token"),
		REFRESH_TOKEN("refresh_token"),
		TOKEN_EXPIRES_IN("expires_in"),
		PICK_THEME("pick_theme"),
		FOLLOW_TWITTER("follow_twitter"),
		ACTION_ABOUT("action_about"),
		WIDGET_TITLE_COLOR("widget_title_color"),
		WIDGET_COUNT_COLOR("widget_count_color"),
		WIDGET_BACKGROUND_COLOR("widget_background_color"),
		WIDGET_TEXT_SIZE("widget_text_size");

		public final String key;

		private Keys(String key)
		{
			this.key = key;

		}

	}

	public int GetColor(Keys key)
	{
		int color = sharedPreferences.getInt(key.key, 0);
		return color;
	}

	public int GetFontSize()
	{
		int intFontSize;
		String fontSize = sharedPreferences.getString(Keys.WIDGET_TEXT_SIZE.key, Defaults.FONT_SIZE);

		try
		{
			intFontSize = Integer.parseInt(fontSize);
		}
		catch (NumberFormatException e)
		{
			Log.e(App.TAG, e.getMessage());
			return 14;
		}

		return intFontSize;

	}

	public Intent GetWidgetIntent()
	{
		return AppChooserPreference.getIntentValue(getMetadata(Keys.CLICK_INTENT), null);
	}

	public Intent GetNotificationIntent()
	{
		return AppChooserPreference.getIntentValue(getMetadata(Keys.NOTIFICATION_CLICK_INTENT), null);

	}

	public boolean IsTokenPresent()
	{

		String accessToken = sharedPreferences.getString(context.getString(R.string.feedly_api_access_token), "");
		if (TextUtils.isEmpty(accessToken))
			return false;

		return true;

	}

	public int GetMinimumUnreadDefault()
	{
		int num;

		String s = sharedPreferences.getString(Keys.MINIMUM_UNREAD.key, Defaults.MIN_UNREAD);
		try
		{
			num = Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			Log.e(App.TAG, e.getMessage());
			return 10;
		}

		return num;
	}

	public boolean IsSyncEnabled()
	{
		return sharedPreferences.getBoolean(Keys.ENABLE_AUTOSYNC.key, Defaults.SYNC_ENABLED);

	}

	public boolean GetBoolPreferences(Keys key)
	{
		return sharedPreferences.getBoolean(key.key, true);
	}

	public String GetToken()
	{
		String accessToken = sharedPreferences.getString(context.getString(R.string.feedly_api_access_token), "");
		return accessToken;

	}

	public int GetSyncInterval()
	{

		int interval;

		String s = sharedPreferences.getString(Keys.SYNC_INTERVAL.key, Defaults.SYNC_INTERVAL);
		try
		{
			interval = Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			Log.e(App.TAG, e.getMessage());
			return 1;
		}

		return interval;

	}

	public void ClearTokens()
	{

		String empty = "";
		Editor edit = sharedPreferences.edit();
		edit.putString(Keys.ACCESS_TOKEN.key, empty);
		edit.putString(Keys.REFRESH_TOKEN.key, empty);
		edit.putString(Keys.TOKEN_EXPIRES_IN.key, empty);
		edit.putLong(Keys.LAST_SUCCESSFUL_SYNC.key, 0);
		edit.commit();

	}

	public void SaveSelectedValuesNotifications(HashMap<String, Boolean> check_states, HashMap<String, Integer> seek_states)
	{
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();

		for (String Id : check_states.keySet())
		{
			if (check_states.get(Id))
			{
				sb.append(Id);
				sb.append(';');
			}
		}

		for (String Id : seek_states.keySet())
		{
			sb1.append(Id);
			sb1.append('|');
			sb1.append(seek_states.get(Id));
			sb1.append(';');

		}

		Editor edit = sharedPreferences.edit();
		edit.putString(Keys.SELECTED_VALUES_NOTIFICATIONS.key, sb.toString());
		edit.putString(Keys.NOTIFICATION_SEEK_STATES.key, sb1.toString());
		edit.commit();

	}

	public ArrayList<String> GetSelectedValuesNotifications()
	{
		ArrayList<String> values = new ArrayList<String>();

		String tokenValues = sharedPreferences.getString(Keys.SELECTED_VALUES_NOTIFICATIONS.key, "");
		if (tokenValues.equalsIgnoreCase(""))
			return values;

		String[] splitValues = tokenValues.split(";");
		for (String s : splitValues)
			values.add(s);

		return values;

	}

	public HashMap<String, Integer> GetSeekValues()
	{
		HashMap<String, Integer> seek_states = new HashMap<String, Integer>();

		String seekValues = sharedPreferences.getString(Keys.NOTIFICATION_SEEK_STATES.key, "");
		String splitValues[] = seekValues.split(";");
		for (String splitValue : splitValues)
		{
			String[] idNum = splitValue.split("\\|");
			if (idNum.length == 2)
			{
				seek_states.put(idNum[0], Integer.parseInt(idNum[1]));
			}

		}

		return seek_states;

	}

	public String getMetadata(Keys key)
	{
		return sharedPreferences.getString(key.key, "");
	}

	public boolean getBoolMetadata(Keys key)
	{
		return sharedPreferences.getBoolean(key.key, true);
	}

	public void SaveSuccessfulSync()
	{
		Editor edit = sharedPreferences.edit();
		edit.putLong(Keys.LAST_SUCCESSFUL_SYNC.key, System.currentTimeMillis());
		edit.commit();

	}

	public long GetLastSuccessfulSync()
	{
		try
		{
			return sharedPreferences.getLong(Keys.LAST_SUCCESSFUL_SYNC.key, 0);
		}
		catch (ClassCastException e)
		{
			return 0;
		}

	}

}
