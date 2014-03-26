package com.afayear.android.util;

import com.afayear.android.client.AfaApplication;
import com.afayear.android.client.activity.BaseSupportActivity;

public class MultiSelectEventHandler {

	private final BaseSupportActivity mActivity;
	private AfaApplication mApplication;
	private AsyncTwitterWrapper mTwitterWrapper;
	private MultiSelectManager mMultiSelectManager;

	public MultiSelectEventHandler(final BaseSupportActivity activity) {
		mActivity = activity;
	}

	/**
	 * Call before super.onCreate
	 */
	public void dispatchOnCreate() {
		mApplication = mActivity.getTwidereApplication();
		mTwitterWrapper = mApplication.getTwitterWrapper();
		mMultiSelectManager = mApplication.getMultiSelectManager();
	}

}
