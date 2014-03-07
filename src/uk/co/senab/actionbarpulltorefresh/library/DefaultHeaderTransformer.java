package uk.co.senab.actionbarpulltorefresh.library;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afayear.android.client.R;
import com.afayear.android.util.ThemeUtils;

public class DefaultHeaderTransformer extends
		PullToRefreshAttacher.HeaderTransformer {

	private ProgressBar mHeaderProgressBar;
	private final Interpolator mInterpolator = new AccelerateInterpolator();
	private ViewGroup mContentLayout;
	private TextView mHeaderTextView;
	private CharSequence mPullRefreshLabel, mRefreshingLabel, mReleaseLabel;
	private boolean mUseCustomProgressColor = false;
	private int mProgressDrawableColor;

	protected DefaultHeaderTransformer() {
		final int min = getMinimumApiLevel();
		if (Build.VERSION.SDK_INT < min)
			throw new IllegalStateException(
					"This HeaderTransformer is designed to run on SDK "
							+ min
							+ "+. If using ActionBarSherlock or ActionBarCompat you should use the appropriate provided extra.");
	}

	@Override
	public void onPulled(final float percentagePulled) {
		if (mHeaderProgressBar != null) {
			mHeaderProgressBar.setVisibility(View.VISIBLE);
			final float progress = mInterpolator
					.getInterpolation(percentagePulled);
			mHeaderProgressBar.setProgress(Math.round(mHeaderProgressBar
					.getMax() * progress));
		}
	}

	@Override
	public void onRefreshMinimized() {
		// Here we fade out most of the header, leaving just the progress bar
		if (mContentLayout != null) {
			mContentLayout.startAnimation(AnimationUtils.loadAnimation(
					mContentLayout.getContext(), R.anim.pull_refresh_fade_out));
			mContentLayout.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onRefreshStarted() {
		if (mHeaderTextView != null) {
			mHeaderTextView.setText(mRefreshingLabel);
		}
		if (mHeaderProgressBar != null) {
			mHeaderProgressBar.setVisibility(View.VISIBLE);
			mHeaderProgressBar.setIndeterminate(true);
		}
	}

	@Override
	public void onReset() {
		// Reset Progress Bar
		if (mHeaderProgressBar != null) {
			mHeaderProgressBar.setVisibility(View.GONE);
			mHeaderProgressBar.setProgress(0);
			mHeaderProgressBar.setIndeterminate(false);
		}

		// Reset Text View
		if (mHeaderTextView != null) {
			mHeaderTextView.setVisibility(View.VISIBLE);
			mHeaderTextView.setText(mPullRefreshLabel);
		}

		// Reset the Content Layout
		if (mContentLayout != null) {
			mContentLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onViewCreated(final Activity activity, final View headerView) {
		// Get ProgressBar and TextView. Also set initial text on TextView
		mHeaderProgressBar = (ProgressBar) headerView
				.findViewById(R.id.ptr_progress);
		mHeaderTextView = (TextView) headerView.findViewById(R.id.ptr_text);

		// Apply any custom ProgressBar colors
		applyProgressBarColor();

		// Labels to display
		mPullRefreshLabel = activity
				.getString(R.string.pull_to_refresh_pull_label);
		mRefreshingLabel = activity
				.getString(R.string.pull_to_refresh_refreshing_label);
		mReleaseLabel = activity
				.getString(R.string.pull_to_refresh_release_label);

		// Retrieve the Action Bar size from the Activity's theme
		mContentLayout = (ViewGroup) headerView.findViewById(R.id.ptr_content);
		if (mContentLayout != null) {
			mContentLayout.getLayoutParams().height = getActionBarSize(activity);
			mContentLayout.requestLayout();
		}

		// Retrieve the Action Bar background from the Activity's theme (see
		// #93).
		final Drawable abBg = getActionBarBackground(activity);
		if (abBg != null) {
			// If we do not have a opaque background we just display a solid
			// solid behind it
			if (abBg.getOpacity() != PixelFormat.OPAQUE) {
				final View view = headerView
						.findViewById(R.id.ptr_text_opaque_bg);
				if (view != null) {
					view.setVisibility(View.VISIBLE);
				}
			}
			if (mHeaderTextView != null) {
				ViewAccessor.setBackground(mHeaderTextView, abBg);
			}
		}

		// Retrieve the Action Bar Title Style from the Action Bar's theme
		final Context abContext = headerView.getContext();
		final int titleTextStyle = getActionBarTitleStyle(abContext);
		if (titleTextStyle != 0 && mHeaderTextView != null) {
			mHeaderTextView.setTextAppearance(abContext, titleTextStyle);
		}

		// Call onReset to make sure that the View is consistent
		onReset();
	}

	protected int getMinimumApiLevel() {
		return Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	private void applyProgressBarColor() {
		if (mHeaderProgressBar != null) {
			if (mUseCustomProgressColor) {
				mHeaderProgressBar.getProgressDrawable().setColorFilter(
						mProgressDrawableColor, Mode.SRC_ATOP);
				mHeaderProgressBar.getIndeterminateDrawable().setColorFilter(
						mProgressDrawableColor, Mode.SRC_ATOP);
			} else {
				mHeaderProgressBar.getProgressDrawable().clearColorFilter();
				mHeaderProgressBar.getIndeterminateDrawable()
						.clearColorFilter();
			}
		}
	}

	protected int getActionBarSize(final Context context) {
		final int[] attrs = { android.R.attr.actionBarSize };
		final TypedArray values = context.getTheme().obtainStyledAttributes(
				attrs);
		try {
			return values.getDimensionPixelSize(0, 0);
		} finally {
			values.recycle();
		}
	}

	protected Drawable getActionBarBackground(final Context context) {
		return ThemeUtils.getActionBarBackground(context);
	}
}
