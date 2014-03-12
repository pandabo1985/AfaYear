package com.afayear.android.client.view;

import com.afayear.android.client.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SlidingPaneView extends ViewGroup {
	/**
	 * Fade is disabled.
	 */
	public static final int FADE_NONE = 0;

	private final RightPaneLayout mRightPaneLayout;

	/**
	 * Value of actions container spacing to use.
	 */
	private int mLeftSpacing;
	/**
	 * Value of spacing to use.
	 */
	private int mRightSpacing;

	/**
	 * Value of shadow width.
	 */
	private int mShadowWidth;
	/**
	 * Fade type.
	 */
	private int mFadeType = FADE_NONE;
	/**
	 * Max fade value.
	 */
	private int mFadeMax;

	/**
	 * Indicates how long flinging will take time in milliseconds.
	 */
	private int mFlingDuration;

	public SlidingPaneView(final Context context) {
		this(context, null);
	}

	public SlidingPaneView(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingPaneView(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);

		final Resources res = getResources();

		setClipChildren(false);
		setClipToPadding(false);

		// reading attributes
		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SlidingPaneView);
		final int spacingLeftDefault = res
				.getDimensionPixelSize(R.dimen.default_slidepane_spacing_left);
		mLeftSpacing = a.getDimensionPixelSize(
				R.styleable.SlidingPaneView_spacingLeft, spacingLeftDefault);
		final int spacingRightDefault = res
				.getDimensionPixelSize(R.dimen.default_slidepane_spacing_right);
		mRightSpacing = a.getDimensionPixelSize(
				R.styleable.SlidingPaneView_spacingRight, spacingRightDefault);

		final int leftPaneLayout = a.getResourceId(
				R.styleable.SlidingPaneView_layoutLeft, 0);
		if (leftPaneLayout == 0)
			throw new IllegalArgumentException(
					"The layoutLeft attribute is required");

		final int rightPaneLayout = a.getResourceId(
				R.styleable.SlidingPaneView_layoutRight, 0);
		if (rightPaneLayout == leftPaneLayout || rightPaneLayout == 0)
			throw new IllegalArgumentException(
					"The layoutRight attribute is required");

		final boolean shadowSlidableDefault = res
				.getBoolean(R.bool.default_shadow_slidable);
		final boolean shadowSlidable = a.getBoolean(
				R.styleable.SlidingPaneView_shadowSlidable,
				shadowSlidableDefault);

		mShadowWidth = a.getDimensionPixelSize(
				R.styleable.SlidingPaneView_shadowWidth, 0);
		final int shadowDrawableRes = a.getResourceId(
				R.styleable.SlidingPaneView_shadowDrawable, 0);

		mFadeType = a.getInteger(R.styleable.SlidingPaneView_fadeType,
				FADE_NONE);
		final int fadeValueDefault = res
				.getInteger(R.integer.default_sliding_pane_fade_max);
		mFadeMax = a.getDimensionPixelSize(R.styleable.SlidingPaneView_fadeMax,
				fadeValueDefault);

		final int flingDurationDefault = res
				.getInteger(R.integer.default_sliding_pane_fling_duration);
		mFlingDuration = a
				.getInteger(R.styleable.SlidingPaneView_flingDuration,
						flingDurationDefault);

		a.recycle();
		
		mRightPaneLayout = new RightPaneLayout(this);
	}

	@Override
	protected void onLayout(final boolean changed, final int l, final int t,
			final int r, final int b) {
	}

	private static class RightPaneLayout extends ExtendedLinearLayout {

		private OnSwipeListener mOnSwipeListener;

		public RightPaneLayout(final SlidingPaneView parent) {
			super(parent.getContext());
			setOrientation(LinearLayout.HORIZONTAL);
		}

		public void setOnSwipeListener(final OnSwipeListener listener) {
			mOnSwipeListener = listener;
		}

		@Override
		protected void onScrollChanged(final int l, final int t,
				final int oldl, final int oldt) {
			super.onScrollChanged(l, t, oldl, oldt);
			if (mOnSwipeListener != null) {
				mOnSwipeListener.onSwipe(-getScrollX());
			}
		}

		public static interface OnSwipeListener {
			public void onSwipe(int scrollPosition);
		}

	}
}
