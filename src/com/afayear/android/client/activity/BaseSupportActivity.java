package com.afayear.android.client.activity;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import android.os.Bundle;

import com.afayear.android.client.Constants;
import com.afayear.android.util.ThemeUtils;

public class BaseSupportActivity extends BaseSupportThemedActivity implements
		Constants {

	private PullToRefreshAttacher mPullToRefreshAttacher;

	@Override
	protected int getThemeResource() {
		return ThemeUtils.getThemeResource(this);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Here we create a PullToRefreshAttacher manually without an Options
		 * instance. PullToRefreshAttacher will manually create one using
		 * default values.
		 */
		mPullToRefreshAttacher = PullToRefreshAttacher.get(this);
	}
}
