package com.afayear.android.client.activity;

import static com.afayear.android.util.Utils.restartActivity;
import com.afayear.android.util.ThemeUtils;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseSupportThemedActivity extends FragmentActivity {
	private int mCurrentThemeResource;

	protected final int getCurrentThemeResource() {
		return mCurrentThemeResource;
	}

	protected abstract int getThemeResource();

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		setTheme();
		super.onCreate(savedInstanceState);
		setActionBarBackground();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isThemeChanged()) {
			restart();
		}
	}
	
	protected final void restart() {
		restartActivity(this);
	}
	private final boolean isThemeChanged() {
		return getThemeResource() != mCurrentThemeResource;
	}
	
	private final void setActionBarBackground() {
		final ActionBar ab = getActionBar();
		if (ab == null) return;
		ab.setBackgroundDrawable(ThemeUtils.getActionBarBackground(this));
	}

	private final void setTheme() {
		mCurrentThemeResource = getThemeResource();
		setTheme(mCurrentThemeResource);
	}
}
