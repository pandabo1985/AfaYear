package com.afayear.android.util;

import android.app.Activity;

public class Utils {
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
}
