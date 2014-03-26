package com.afayear.android.client;

/**
 * 
 * @author afayear
 * 
 */
public interface Constants {

	public static final String SHARED_PREFERENCES_NAME = "preferences";
	public static final String PREFERENCE_KEY_THEME = "theme";
	public static final String PREFERENCE_KEY_SOLID_COLOR_BACKGROUND = "solid_color_background";

	public static final String INTENT_PACKAGE_PREFIX = "com.afayear.android.client.";
	public static final String BROADCAST_TASK_STATE_CHANGED = INTENT_PACKAGE_PREFIX
			+ "TASK_STATE_CHANGED";
	public static final String PREFERENCE_KEY_DUAL_PANE_IN_PORTRAIT = "dual_pane_in_portrait";
	public static final String PREFERENCE_KEY_DUAL_PANE_IN_LANDSCAPE = "dual_pane_in_landscape";

	public static final int PANE_LEFT = R.id.fragment_container_left;
	public static final int PANE_RIGHT = R.id.fragment_container_right;
	public static final String INTENT_ACTION_TWITTER_LOGIN = INTENT_PACKAGE_PREFIX
			+ "CLIENT_LOGIN";
	public static final String PREFERENCE_KEY_DEFAULT_ACCOUNT_ID = "default_account_id";
	public static final String PREFERENCE_KEY_SETTINGS_WIZARD_COMPLETED = "settings_wizard_completed";

	public static final String BROADCAST_HOME_ACTIVITY_ONCREATE = INTENT_PACKAGE_PREFIX
			+ "HOME_ACTIVITY_ONCREATE";
	public static final String PREFERENCE_KEY_REFRESH_ON_START = "refresh_on_start";

	public static final String EXTRA_ACCOUNT_ID = "account_id";
	public static final String EXTRA_QUERY = "query";
	public static final String SCHEME_AFAYEAR = "afayear";
	public static final String AUTHORITY_SEARCH = "search";
	public static final String QUERY_PARAM_ACCOUNT_ID = "account_id";
	public static final String QUERY_PARAM_QUERY = "query";
	public static final String EXTRA_IDS = "ids";
	public static final String EXTRA_INITIAL_TAB = "initial_tab";
	public static final String EXTRA_TAB_TYPE = "tab_type";
	public static final String EXTRA_EXTRA_INTENT = "extra_intent";
}
