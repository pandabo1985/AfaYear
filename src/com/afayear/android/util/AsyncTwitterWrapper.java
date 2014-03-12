package com.afayear.android.util;

import com.afayear.android.client.AfaApplication;
import com.afayear.android.client.R;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;

public class AsyncTwitterWrapper extends TwitterWrapper {
	
	private static AsyncTwitterWrapper sInstance;
	private Context mContext;
	private final SharedPreferences mPreferences;
	private final ContentResolver mResolver;
	private final boolean mLargeProfileImage;
	private final MessagesManager mMessagesManager;
	private final AsyncTaskManager mAsyncTaskManager;
	
	public AsyncTwitterWrapper(final Context context) {
		mContext = context;
		final AfaApplication app = AfaApplication.getInstance(context);
		mAsyncTaskManager = app.getAsyncTaskManager();
		mMessagesManager = app.getMessagesManager();
		mPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		mResolver = context.getContentResolver();
		mLargeProfileImage = context.getResources().getBoolean(R.bool.hires_profile_image);
	}
	
	public static AsyncTwitterWrapper getInstance(final Context context) {
		if (sInstance != null)
			return sInstance;
		return sInstance = new AsyncTwitterWrapper(context);
	}

}
