package com.afayear.android.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.afayear.android.client.Constants;

public final class MessagesManager implements Constants {

	private final Context mContext;
	private final SharedPreferences mPreferences;
	private final Set<Activity> mMessageCallbacks = Collections
			.synchronizedSet(new HashSet<Activity>());

	public MessagesManager(final Context context) {
		mContext = context;
		mPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public boolean addMessageCallback(final Activity activity) {
		if (activity == null)
			return false;
		return mMessageCallbacks.add(activity);
	}

	public boolean removeMessageCallback(final Activity activity) {
		if (activity == null)
			return false;
		return mMessageCallbacks.remove(activity);
	}

}
