package com.afayear.android.client;

import android.app.Application;
import android.content.Context;

import com.afayear.android.util.AsyncTaskManager;
import com.afayear.android.util.AsyncTwitterWrapper;
import com.afayear.android.util.MessagesManager;
import com.afayear.android.util.MultiSelectManager;

public class AfaApplication extends Application {
	private MessagesManager mCroutonsManager;
	private AsyncTwitterWrapper mTwitterWrapper;
	private AsyncTaskManager mAsyncTaskManager;
	private MultiSelectManager mMultiSelectManager;

	public static AfaApplication getInstance(final Context context) {
		if (context == null)
			return null;
		final Context app = context.getApplicationContext();
		return app instanceof AfaApplication ? (AfaApplication) app : null;
	}

	public MessagesManager getMessagesManager() {
		if (mCroutonsManager != null)
			return mCroutonsManager;
		return mCroutonsManager = new MessagesManager(this);
	}

	public AsyncTwitterWrapper getTwitterWrapper() {
		if (mTwitterWrapper != null)
			return mTwitterWrapper;
		return mTwitterWrapper = AsyncTwitterWrapper.getInstance(this);
	}

	public AsyncTaskManager getAsyncTaskManager() {
		if (mAsyncTaskManager != null)
			return mAsyncTaskManager;
		return mAsyncTaskManager = AsyncTaskManager.getInstance();
	}

	public MultiSelectManager getMultiSelectManager() {

		if (mMultiSelectManager != null)
			return mMultiSelectManager;
		return mMultiSelectManager = new MultiSelectManager();
	}

}
