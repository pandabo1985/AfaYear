package com.afayear.android.util;

import java.io.File;

import com.afayear.android.client.Constants;
import com.afayear.android.client.activity.DualPaneActivity;
import com.afayear.android.client.fragment.SearchFragment;
import com.afayear.android.client.provider.AfayearStore.Accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

public final class Utils implements Constants {
	public static void restartActivity(final Activity activity) {
		if (activity == null)
			return;
		final int enter_anim = android.R.anim.fade_in;
		final int exit_anim = android.R.anim.fade_out;
		activity.overridePendingTransition(enter_anim, exit_anim);
		activity.finish();
		activity.overridePendingTransition(enter_anim, exit_anim);
		activity.startActivity(activity.getIntent());
	}

	public static long[] getAccountIds(final Context context) {
		long[] accounts = new long[0];
		if (context == null)
			return accounts;
		final Cursor cur = context.getContentResolver().query(
				Accounts.CONTENT_URI, new String[] { Accounts.ACCOUNT_ID },
				null, null, null);
		if (cur != null) {
			final int idx = cur.getColumnIndexOrThrow(Accounts.ACCOUNT_ID);
			cur.moveToFirst();
			accounts = new long[cur.getCount()];
			int i = 0;
			while (!cur.isAfterLast()) {
				accounts[i] = cur.getLong(idx);
				i++;
				cur.moveToNext();
			}
			cur.close();
		}
		return accounts;
	}

	public static void openSearch(final Activity activity,
			final long account_id, final String query) {
		if (activity == null)
			return;
		if (activity instanceof DualPaneActivity
				&& ((DualPaneActivity) activity).isDualPaneMode()) {
			final DualPaneActivity dual_pane_activity = (DualPaneActivity) activity;
			final Fragment fragment = new SearchFragment();
			final Bundle args = new Bundle();
			args.putLong(EXTRA_ACCOUNT_ID, account_id);
			args.putString(EXTRA_QUERY, query);
			fragment.setArguments(args);
			dual_pane_activity.showAtPane(DualPaneActivity.PANE_LEFT, fragment,
					true);
		} else {
			final Uri.Builder builder = new Uri.Builder();
			builder.scheme(SCHEME_AFAYEAR);
			builder.authority(AUTHORITY_SEARCH);
			builder.appendQueryParameter(QUERY_PARAM_ACCOUNT_ID,
					String.valueOf(account_id));
			builder.appendQueryParameter(QUERY_PARAM_QUERY, query);
			activity.startActivity(new Intent(Intent.ACTION_VIEW, builder
					.build()));
		}
	}

	public static Bitmap getTabIconFromFile(final File file, final Resources res) {
		if (file == null || !file.exists()) return null;
		final String path = file.getPath();
		final BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, o);
		if (o.outHeight <= 0 || o.outWidth <= 0) return null;
		o.inSampleSize = (int) (Math.max(o.outWidth, o.outHeight) / (48 * res.getDisplayMetrics().density));
		o.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, o);
	}
	
	public static void announceForAccessibilityCompat(final Context context, final View view, final CharSequence text,
			final Class<?> cls) {
		final AccessibilityManager accessibilityManager = (AccessibilityManager) context
				.getSystemService(Context.ACCESSIBILITY_SERVICE);
		if (!accessibilityManager.isEnabled()) return;
		// Prior to SDK 16, announcements could only be made through FOCUSED
		// events. Jelly Bean (SDK 16) added support for speaking text verbatim
		// using the ANNOUNCEMENT event type.
		final int eventType;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			eventType = AccessibilityEvent.TYPE_VIEW_FOCUSED;
		} else {
			eventType = AccessibilityEventCompat.TYPE_ANNOUNCEMENT;
		}

		// Construct an accessibility event with the minimum recommended
		// attributes. An event without a class name or package may be dropped.
		final AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
		event.getText().add(text);
		event.setClassName(cls.getName());
		event.setPackageName(context.getPackageName());
		event.setSource(view);

		// Sends the event directly through the accessibility manager. If your
		// application only targets SDK 14+, you should just call
		// getParent().requestSendAccessibilityEvent(this, event);
		accessibilityManager.sendAccessibilityEvent(event);
	}

}
