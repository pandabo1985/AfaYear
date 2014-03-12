package com.afayear.android.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class ViewAccessor {
	public static void setBackground(final View view, final Drawable background) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackgroundDrawable(background);
		} else {
			ViewAccessorJB.setBackground(view, background);
		}
	}

	static class ViewAccessorJB {
		static void setBackground(final View view, final Drawable background) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
				return;
			view.setBackground(background);
		}
	}
}
