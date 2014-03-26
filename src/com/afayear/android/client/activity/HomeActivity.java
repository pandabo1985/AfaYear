package com.afayear.android.client.activity;


import static com.afayear.android.util.Utils.getAccountIds;
import static com.afayear.android.util.Utils.openSearch;
import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.afayear.android.client.R;
import com.afayear.android.client.adapter.SupportTabsAdapter;
import com.afayear.android.client.provider.RecentSearchProvider;
import com.afayear.android.client.view.ExtendedViewPager;
import com.afayear.android.client.view.TabPageIndicator;
import com.afayear.android.util.ArrayUtils;
import com.afayear.android.util.AsyncTwitterWrapper;
import com.afayear.android.util.MultiSelectEventHandler;
import com.afayear.android.util.ThemeUtils;


public class HomeActivity extends DualPaneActivity {

	private SharedPreferences mPreferences;
	private AsyncTwitterWrapper mTwitterWrapper;
	private NotificationManager mNotificationManager;
	private MultiSelectEventHandler mMultiSelectHandler;
	private boolean mDisplayAppIcon;
	private ActionBar mActionBar;
	private TabPageIndicator mIndicator;
	private SupportTabsAdapter mPagerAdapter;
	private ExtendedViewPager mViewPager;
	private DrawerLayout mDrawerLayout;
	private View mActionsActionView, mActionsButtonLayout;
	private View mLeftDrawerContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mTwitterWrapper = getTwitterWrapper();
		mPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mMultiSelectHandler = new MultiSelectEventHandler(this);
		mMultiSelectHandler.dispatchOnCreate();
		final Resources res = getResources();
		mDisplayAppIcon = res.getBoolean(R.bool.home_display_icon);
		super.onCreate(savedInstanceState);
		final long[] account_ids = getAccountIds(this);
//		if (account_ids.length == 0) {
//			final Intent sign_in_intent = new Intent(
//					INTENT_ACTION_TWITTER_LOGIN);
//			sign_in_intent.setClass(this, SignInActivity.class);
//			startActivity(sign_in_intent);
//			finish();
//			return;
//		} else {
//			notifyAccountsChanged();
//		}

		final Intent intent = getIntent();
//		if (openSettingsWizard()) {
//			finish();
//			return;
//		}
		sendBroadcast(new Intent(BROADCAST_HOME_ACTIVITY_ONCREATE));
		final boolean refresh_on_start = mPreferences.getBoolean(
				PREFERENCE_KEY_REFRESH_ON_START, false);
		final int initial_tab = handleIntent(intent, savedInstanceState == null);
		
		mActionBar = getActionBar();
		mActionBar.setCustomView(R.layout.base_tabs);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(mDisplayAppIcon);
		if (mDisplayAppIcon) {
			mActionBar.setHomeButtonEnabled(true);
		}
		final View view = mActionBar.getCustomView();
		
		mIndicator = (TabPageIndicator) view.findViewById(android.R.id.tabs);
		ThemeUtils.applyBackground(mIndicator);
		
		mPagerAdapter = new SupportTabsAdapter(this, getSupportFragmentManager(), mIndicator);

	}

	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mViewPager = (ExtendedViewPager) findViewById(R.id.main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mActionsButtonLayout = findViewById(R.id.actions_button);
		mLeftDrawerContainer = findViewById(R.id.left_drawer_container);
	}
	@Override
	protected int getDualPaneLayoutRes() {
		return R.layout.home_dual_pane;
	}

	@Override
	protected int getNormalLayoutRes() {
		return R.layout.home;
	}
	
	public void notifyAccountsChanged() {
		if (mPreferences == null)
			return;
		final long[] account_ids = getAccountIds(this);
		final long default_id = mPreferences.getLong(
				PREFERENCE_KEY_DEFAULT_ACCOUNT_ID, -1);
		if (account_ids == null || account_ids.length == 0) {
			finish();
		} else if (account_ids.length > 0
				&& !ArrayUtils.contains(account_ids, default_id)) {
			mPreferences.edit()
					.putLong(PREFERENCE_KEY_DEFAULT_ACCOUNT_ID, account_ids[0])
					.commit();
		}
	}

	private boolean openSettingsWizard() {
		if (mPreferences == null
				|| mPreferences.getBoolean(
						PREFERENCE_KEY_SETTINGS_WIZARD_COMPLETED, false))
			return false;
		startActivity(new Intent(this, SettingsWizardActivity.class));
		return true;
	}

	private int handleIntent(final Intent intent, final boolean first_create) {
		// Reset intent
		setIntent(new Intent(this, HomeActivity.class));
		final String action = intent.getAction();
		if (Intent.ACTION_SEARCH.equals(action)) {
			final String query = intent.getStringExtra(SearchManager.QUERY);
			if (first_create) {
				final SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
						this, RecentSearchProvider.AUTHORITY,
						RecentSearchProvider.MODE);
				suggestions.saveRecentQuery(query, null);
			}
			final long account_id = getDefaultAccountId(this);
			openSearch(this, account_id, query);
			return -1;
		}
		final Bundle extras = intent.getExtras();
		final boolean refresh_on_start = mPreferences.getBoolean(
				PREFERENCE_KEY_REFRESH_ON_START, false);
		final long[] refreshed_ids = extras != null ? extras
				.getLongArray(EXTRA_IDS) : null;
//		if (refreshed_ids != null) {
//			mTwitterWrapper.refreshAll(refreshed_ids);
//		} else if (first_create && refresh_on_start) {
//			mTwitterWrapper.refreshAll();
//		}
//
		final int initial_tab;
//		if (extras != null) {
//			final int tab = extras.getInt(EXTRA_INITIAL_TAB, -1);
//			initial_tab = tab != -1 ? tab : getAddedTabPosition(this,
//					extras.getString(EXTRA_TAB_TYPE));
//			if (initial_tab != -1 && mViewPager != null) {
//				clearNotification(initial_tab);
//			}
//			final Intent extra_intent = extras
//					.getParcelable(EXTRA_EXTRA_INTENT);
//			if (extra_intent != null) {
//				if (isTwidereLink(extra_intent.getData()) && isDualPaneMode()) {
//					showFragment(createFragmentForIntent(this, extra_intent),
//							true);
//				} else {
//					startActivity(extra_intent);
//				}
//			}
//		} else {
			initial_tab = -1;
//		}
		return initial_tab;
	}

	public static long getDefaultAccountId(final Context context) {
		if (context == null)
			return -1;
		final SharedPreferences prefs = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		return prefs.getLong(PREFERENCE_KEY_DEFAULT_ACCOUNT_ID, -1);
	}

}
