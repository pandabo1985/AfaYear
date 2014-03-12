package com.afayear.android.client.activity;

import com.afayear.android.client.R;
import com.afayear.android.client.view.SlidingPaneView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

public class DualPaneActivity extends BaseSupportActivity {

	private SharedPreferences mPreferences;
	private boolean mDualPaneInPortrait, mDualPaneInLandscape;
	private SlidingPaneView mSlidingPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		super.onCreate(savedInstanceState);
		final Resources res = getResources();
		final int orientation = res.getConfiguration().orientation;
		final int layout;
		final boolean is_large_screen = res.getBoolean(R.bool.is_large_screen);
		mDualPaneInPortrait = mPreferences.getBoolean(
				PREFERENCE_KEY_DUAL_PANE_IN_PORTRAIT, is_large_screen);
		mDualPaneInLandscape = mPreferences.getBoolean(
				PREFERENCE_KEY_DUAL_PANE_IN_LANDSCAPE, is_large_screen);
		switch (orientation) {
		case Configuration.ORIENTATION_LANDSCAPE:
			layout = mDualPaneInLandscape || shouldForceEnableDualPaneMode() ? getDualPaneLayoutRes()
					: getNormalLayoutRes();
			break;
		case Configuration.ORIENTATION_PORTRAIT:
			layout = mDualPaneInPortrait || shouldForceEnableDualPaneMode() ? getDualPaneLayoutRes()
					: getNormalLayoutRes();
			break;
		default:
			layout = getNormalLayoutRes();
			break;
		}
		setContentView(layout);
		if (mSlidingPane != null) {
			mSlidingPane.setRightPaneBackground(getPaneBackground());
		}
		final FragmentManager fm = getSupportFragmentManager();
		fm.addOnBackStackChangedListener(this);
		if (savedInstanceState != null) {
			final Fragment left_pane_fragment = fm.findFragmentById(PANE_LEFT);
			final View main_view = findViewById(R.id.main);
			final boolean left_pane_used = left_pane_fragment != null
					&& left_pane_fragment.isAdded();
			if (main_view != null) {
				final int visibility = left_pane_used ? View.GONE
						: View.VISIBLE;
				main_view.setVisibility(visibility);
			}
		}
	}

	protected boolean shouldForceEnableDualPaneMode() {
		return false;
	}

	protected int getDualPaneLayoutRes() {

		return R.layout.base_dual_pane;
	}

	protected int getNormalLayoutRes() {

		return R.layout.base;
	}

	protected int getPaneBackground() {
		final TypedArray a = obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
		final int background = a.getResourceId(0, 0);
		a.recycle();
		return background;
	}

}
