package com.afayear.android.client.activity;

import java.util.HashSet;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;

import com.afayear.android.client.Constants;
import com.afayear.android.util.AsyncTwitterWrapper;
import com.afayear.android.util.ThemeUtils;
import com.afayear.android.util.MessagesManager;
import com.afayear.android.client.AfaApplication;
import com.afayear.android.client.fragment.iface.IBasePullToRefreshFragment;
import com.afayear.android.client.fragment.iface.PullToRefreshAttacherActivity;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class BaseSupportActivity extends BaseSupportThemedActivity implements
		Constants, PullToRefreshAttacherActivity {

	private PullToRefreshAttacher mPullToRefreshAttacher;
	private boolean mInstanceStateSaved, mIsVisible, mIsOnTop;
	private final Set<String> mEnabledStates = new HashSet<String>();
	private final Set<String> mRefreshingStates = new HashSet<String>();

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

	@Override
	protected void onPause() {
		mIsOnTop = false;
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mInstanceStateSaved = false;
		mIsOnTop = true;
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		mInstanceStateSaved = true;
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mIsVisible = true;
		final MessagesManager croutons = getMessagesManager();
		if (croutons != null) {
			croutons.addMessageCallback(this);
		}
	}

	@Override
	protected void onStop() {
		mIsVisible = false;
		final MessagesManager croutons = getMessagesManager();
		if (croutons != null) {
			croutons.removeMessageCallback(this);
		}
		super.onStop();
	}

	protected IBasePullToRefreshFragment getCurrentPullToRefreshFragment() {
		return null;
	}

	protected boolean isStateSaved() {
		return mInstanceStateSaved;
	}

	@Override
	public void addRefreshingState(final IBasePullToRefreshFragment fragment) {
		final String tag = fragment.getPullToRefreshTag();
		if (tag == null)
			return;
		mEnabledStates.add(tag);
	}

	@Override
	public PullToRefreshAttacher getPullToRefreshAttacher() {
		return mPullToRefreshAttacher;
	}

	@Override
	public boolean isRefreshing(final IBasePullToRefreshFragment fragment) {
		if (fragment == null)
			return false;
		return mRefreshingStates.contains(fragment.getPullToRefreshTag());
	}

	@Override
	public void setPullToRefreshEnabled(
			final IBasePullToRefreshFragment fragment, final boolean enabled) {
		final String tag = fragment.getPullToRefreshTag();
		if (tag == null)
			return;
		if (enabled) {
			mEnabledStates.add(tag);
		} else {
			mEnabledStates.remove(tag);
		}
		final IBasePullToRefreshFragment curr = getCurrentPullToRefreshFragment();
		if (curr != null && tag.equals(curr.getPullToRefreshTag())) {
			mPullToRefreshAttacher.setEnabled(enabled);
		}
	}

	@Override
	public void setRefreshComplete(final IBasePullToRefreshFragment fragment) {
		final String tag = fragment.getPullToRefreshTag();
		if (tag == null)
			return;
		mRefreshingStates.remove(tag);
		final IBasePullToRefreshFragment curr = getCurrentPullToRefreshFragment();
		if (curr != null && tag.equals(curr.getPullToRefreshTag())) {
			mPullToRefreshAttacher.setRefreshComplete();
		}
	}

	@Override
	public void setRefreshing(final IBasePullToRefreshFragment fragment,
			final boolean refreshing) {
		final String tag = fragment.getPullToRefreshTag();
		if (tag == null)
			return;
		if (refreshing) {
			mRefreshingStates.add(tag);
		} else {
			mRefreshingStates.remove(tag);
		}
		final IBasePullToRefreshFragment curr = getCurrentPullToRefreshFragment();
		if (curr != null && tag.equals(curr.getPullToRefreshTag())) {
			mPullToRefreshAttacher.setRefreshing(refreshing);
		}
	}

	@Override
	public void startActivity(final Intent intent) {
		super.startActivity(intent);
	}

	@Override
	public void startActivityForResult(final Intent intent,
			final int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

	public MessagesManager getMessagesManager() {
		return getTwidereApplication() != null ? getTwidereApplication()
				.getMessagesManager() : null;
	}

	public AfaApplication getTwidereApplication() {
		return (AfaApplication) getApplication();
	}

	public boolean isOnTop() {
		return mIsOnTop;
	}

	public boolean isVisible() {
		return mIsVisible;
	}

	public void setRefreshing(final boolean refreshing) {
		mPullToRefreshAttacher.setRefreshing(refreshing);
	}

	public void updateRefreshingState() {
		setRefreshing(isRefreshing(getCurrentPullToRefreshFragment()));
	}


	public AsyncTwitterWrapper getTwitterWrapper() {
		return getTwidereApplication() != null ? getTwidereApplication()
				.getTwitterWrapper() : null;
	}

}
