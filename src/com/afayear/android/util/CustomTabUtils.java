package com.afayear.android.util;

import java.io.File;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.afayear.android.client.Constants;
import com.afayear.android.client.R;
import com.afayear.android.util.Utils;

public class CustomTabUtils implements Constants {

	public static Drawable getTabIconDrawable(final Context context,
			final Object icon_obj) {
		if (context == null)
			return null;
		final Resources res = context.getResources();
		if (icon_obj instanceof Integer) {
			try {
				return res.getDrawable((Integer) icon_obj);
			} catch (final Resources.NotFoundException e) {
				// Ignore.
			}
		} else if (icon_obj instanceof Bitmap)
			return new BitmapDrawable(res, (Bitmap) icon_obj);
		else if (icon_obj instanceof Drawable)
			return (Drawable) icon_obj;
		else if (icon_obj instanceof File) {
			final Bitmap b = Utils.getTabIconFromFile((File) icon_obj, res);
			if (b != null)
				return new BitmapDrawable(res, b);
		}
		return res.getDrawable(R.drawable.ic_tab_list);
	}

}
