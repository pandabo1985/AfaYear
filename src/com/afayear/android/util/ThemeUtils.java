package com.afayear.android.util;

import java.util.HashMap;

import com.afayear.android.client.Constants;
import com.afayear.android.client.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

public class ThemeUtils implements Constants {

	private static final String THEME_NAME_TWIDERE = "twidere";
	private static final HashMap<String, Integer> THEMES_SOLIDBG = new HashMap<String, Integer>();
	private static final HashMap<String, Integer> THEMES = new HashMap<String, Integer>();

	/**
	 * @param context
	 * @return 获得drawable,在drawable文件下定义color_layer
	 */
	public static Drawable getActionBarBackground(final Context context) {
		final TypedArray a = context.obtainStyledAttributes(null,
				new int[] { android.R.attr.background },
				android.R.attr.actionBarStyle, 0);
		final int color = getThemeColor(context);
		final Drawable d = a.getDrawable(0);
		a.recycle();
		if (!(d instanceof LayerDrawable))
			return d;
		final LayerDrawable ld = (LayerDrawable) d.mutate();
		final Drawable color_layer = ld.findDrawableByLayerId(R.id.color_layer);
		if (color_layer != null) {
			color_layer.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
		}
		return ld;
	}

	public static int getThemeColor(final Context context) {
		if (context == null)
			return Color.TRANSPARENT;
		final int def = context.getResources().getColor(
				android.R.color.holo_blue_light);
		try {
			final TypedArray a = context
					.obtainStyledAttributes(new int[] { android.R.attr.colorActivatedHighlight });
			final int color = a.getColor(0, def);
			a.recycle();
			return color;
		} catch (final Exception e) {
			return def;
		}
	}

	public static int getThemeResource(final Context context) {
		return getThemeResource(getThemeName(context),
				isSolidBackground(context));
	}

	public static int getThemeResource(final String name,
			final boolean solid_background) {
		final Integer res = (solid_background ? THEMES_SOLIDBG : THEMES)
				.get(name);
		return res != null ? res : R.style.Theme_Twidere;
	}

	public static String getThemeName(final Context context) {
		if (context == null)
			return THEME_NAME_TWIDERE;
		final SharedPreferences pref = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		return pref != null ? pref.getString(PREFERENCE_KEY_THEME,
				THEME_NAME_TWIDERE) : THEME_NAME_TWIDERE;
	}

	public static boolean isSolidBackground(final Context context) {
		if (context == null)
			return false;
		final SharedPreferences pref = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		return pref != null ? pref.getBoolean(
				PREFERENCE_KEY_SOLID_COLOR_BACKGROUND, false) : false;
	}

}
