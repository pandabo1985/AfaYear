package uk.co.senab.actionbarpulltorefresh.library;

import java.util.WeakHashMap;

import com.afayear.android.client.R;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PullToRefreshAttacher {
	private static final String LOG_TAG = "PullToRefreshAttacher";
	/* Default configuration values */
	private static final int DEFAULT_HEADER_LAYOUT = R.layout.pull_refresh_default_header;
	private static final int DEFAULT_ANIM_HEADER_IN = R.anim.pull_refresh_fade_in;
	private static final int DEFAULT_ANIM_HEADER_OUT = R.anim.pull_refresh_fade_out;
	private static final float DEFAULT_REFRESH_SCROLL_DISTANCE = 0.3f;
	private static final boolean DEFAULT_REFRESH_ON_UP = false;
	private static final int DEFAULT_REFRESH_MINIMIZED_DELAY = 3 * 1000;
	private static final boolean DEFAULT_REFRESH_MINIMIZE = true;

	/* Member Variables */
	private final WeakHashMap<View, ViewParams> mRefreshableViews;
	private final WeakHashMap<View, OnTouchListener> mOnTouchListeners;
	private final float mRefreshScrollDistance;
	private final boolean mRefreshOnUp;
	private final int mRefreshMinimizeDelay;
	private final boolean mRefreshMinimize;
	private final EnvironmentDelegate mEnvironmentDelegate;
	private final HeaderTransformer mHeaderTransformer;
	private final Animation mHeaderInAnimation, mHeaderOutAnimation;


	protected PullToRefreshAttacher(final Activity activity, Options options) {
		if (options == null) {
			Log.i(LOG_TAG, "Given null options so using default options.");
			options = new Options();
		}

		mRefreshableViews = new WeakHashMap<View, ViewParams>();
		mOnTouchListeners = new WeakHashMap<View, View.OnTouchListener>();

		// Copy necessary values from options
		mRefreshScrollDistance = options.refreshScrollDistance;
		mRefreshOnUp = options.refreshOnUp;
		mRefreshMinimizeDelay = options.refreshMinimizeDelay;
		mRefreshMinimize = options.refreshMinimize;

		// EnvironmentDelegate
		mEnvironmentDelegate = options.environmentDelegate != null ? options.environmentDelegate
				: createDefaultEnvironmentDelegate();

		// Header Transformer
		mHeaderTransformer = options.headerTransformer != null ? options.headerTransformer
				: createDefaultHeaderTransformer();

		// Create animations for use later
		mHeaderInAnimation = AnimationUtils.loadAnimation(activity,
				options.headerInAnimation);
		mHeaderOutAnimation = AnimationUtils.loadAnimation(activity,
				options.headerOutAnimation);
		if (mHeaderOutAnimation != null || mHeaderInAnimation != null) {
			final AnimationCallback callback = new AnimationCallback();
			if (mHeaderInAnimation != null) {
				mHeaderInAnimation.setAnimationListener(callback);
			}
			if (mHeaderOutAnimation != null) {
				mHeaderOutAnimation.setAnimationListener(callback);
			}
		}

		// Get touch slop for use later
		mTouchSlop = ViewConfiguration.get(activity).getScaledTouchSlop();

		// Get Window Decor View
		final ViewGroup decorView = (ViewGroup) activity.getWindow()
				.getDecorView();

		// Check to see if there is already a Attacher view installed
		if (decorView.getChildCount() == 1
				&& decorView.getChildAt(0) instanceof DecorChildLayout)
			throw new IllegalStateException(
					"View already installed to DecorView. This shouldn't happen.");

		// Create Header view and then add to Decor View
		mHeaderView = LayoutInflater.from(
				mEnvironmentDelegate.getContextForInflater(activity)).inflate(
				options.headerLayout, decorView, false);
		if (mHeaderView == null)
			throw new IllegalArgumentException(
					"Must supply valid layout id for header.");
		mHeaderView.setVisibility(View.GONE);

		// Create DecorChildLayout which will move all of the system's decor
		// view's children + the
		// Header View to itself. See DecorChildLayout for more info.
		final DecorChildLayout decorContents = new DecorChildLayout(activity,
				decorView, mHeaderView);

		// Now add the DecorChildLayout to the decor view
		decorView.addView(decorContents, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		// Notify transformer
		mHeaderTransformer.onViewCreated(activity, mHeaderView);
		// TODO Remove the follow deprecated method call before v1.0
		mHeaderTransformer.onViewCreated(mHeaderView);
	}

	/**
	 * Get a PullToRefreshAttacher for this Activity. If there is already a
	 * PullToRefreshAttacher attached to the Activity, the existing one is
	 * returned, otherwise a new instance is created. This version of the method
	 * will use default configuration options for everything.
	 * 
	 * @param activity
	 *            Activity to attach to.
	 * @return PullToRefresh attached to the Activity.
	 */
	public static PullToRefreshAttacher get(final Activity activity) {
		return get(activity, new Options());
	}

	/**
	 * Get a PullToRefreshAttacher for this Activity. If there is already a
	 * PullToRefreshAttacher attached to the Activity, the existing one is
	 * returned, otherwise a new instance is created.
	 * 
	 * @param activity
	 *            Activity to attach to.
	 * @param options
	 *            Options used when creating the PullToRefreshAttacher.
	 * @return PullToRefresh attached to the Activity.
	 */
	public static PullToRefreshAttacher get(final Activity activity,
			final Options options) {
		return new PullToRefreshAttacher(activity, options);
	}

	public static class Options {

		/**
		 * EnvironmentDelegate instance which will be used. If null, we will
		 * create an instance of the default class.
		 */
		public EnvironmentDelegate environmentDelegate = null;

		/**
		 * The layout resource ID which should be inflated to be displayed above
		 * the Action Bar
		 */
		public int headerLayout = DEFAULT_HEADER_LAYOUT;

		/**
		 * The header transformer to be used to transfer the header view. If
		 * null, an instance of {@link DefaultHeaderTransformer} will be used.
		 */
		public HeaderTransformer headerTransformer = null;

		/**
		 * The anim resource ID which should be started when the header is being
		 * hidden.
		 */
		public int headerOutAnimation = DEFAULT_ANIM_HEADER_OUT;

		/**
		 * The anim resource ID which should be started when the header is being
		 * shown.
		 */
		public int headerInAnimation = DEFAULT_ANIM_HEADER_IN;

		/**
		 * The percentage of the refreshable view that needs to be scrolled
		 * before a refresh is initiated.
		 */
		public float refreshScrollDistance = DEFAULT_REFRESH_SCROLL_DISTANCE;

		/**
		 * Whether a refresh should only be initiated when the user has finished
		 * the touch event.
		 */
		public boolean refreshOnUp = DEFAULT_REFRESH_ON_UP;

		/**
		 * The delay after a refresh is started in which the header should be
		 * 'minimized'. By default, most of the header is faded out, leaving
		 * only the progress bar signifying that a refresh is taking place.
		 */
		public int refreshMinimizeDelay = DEFAULT_REFRESH_MINIMIZED_DELAY;

		/**
		 * Enable or disable the header 'minimization', which by default means
		 * that the majority of the header is hidden, leaving only the progress
		 * bar still showing.
		 * <p/>
		 * If set to true, the header will be minimized after the delay set in
		 * {@link #refreshMinimizeDelay}. If set to false then the whole header
		 * will be displayed until the refresh is finished.
		 */
		public boolean refreshMinimize = DEFAULT_REFRESH_MINIMIZE;
	}

	public static abstract class HeaderTransformer {

		/**
		 * Called the user has pulled on the scrollable view.
		 * 
		 * @param percentagePulled
		 *            - value between 0.0f and 1.0f depending on how far the
		 *            user has pulled.
		 */
		public void onPulled(final float percentagePulled) {
		}

		/**
		 * Called when the current refresh has taken longer than the time
		 * specified in {@link Options#refreshMinimizeDelay}.
		 */
		public void onRefreshMinimized() {
		}

		/**
		 * Called when a refresh has begun. Theoretically this call is similar
		 * to that provided from {@link OnRefreshListener} but is more suitable
		 * for header view updates.
		 */
		public void onRefreshStarted() {
		}

		/**
		 * Called when a refresh can be initiated when the user ends the touch
		 * event. This is only called when {@link Options#refreshOnUp} is set to
		 * true.
		 */
		public void onReleaseToRefresh() {
		}

		/**
		 * Called when the header should be reset. You should update any child
		 * views to reflect this.
		 * <p/>
		 * You should <strong>not</strong> change the visibility of the header
		 * view.
		 */
		public void onReset() {
		}

		/**
		 * Called whether the header view has been inflated from the resources
		 * defined in {@link Options#headerLayout}.
		 * 
		 * @param activity
		 *            The {@link Activity} that the header view is attached to.
		 * @param headerView
		 *            The inflated header view.
		 */
		public void onViewCreated(final Activity activity, final View headerView) {
		}

		/**
		 * @deprecated This will be removed before v1.0. Override
		 *             {@link #onViewCreated(android.app.Activity, android.view.View)}
		 *             instead.
		 */
		@Deprecated
		public void onViewCreated(final View headerView) {
		}
	}

	/**
	 * FIXME
	 */
	public static class EnvironmentDelegate {

		/**
		 * @return Context which should be used for inflating the header layout
		 */
		public Context getContextForInflater(final Activity activity) {
			if (Build.VERSION.SDK_INT >= 14)
				return activity.getActionBar().getThemedContext();
			else
				return activity;
		}
	}

	private static final class ViewParams {
		final OnRefreshListener onRefreshListener;
		final ViewDelegate viewDelegate;

		ViewParams(final ViewDelegate _viewDelegate,
				final OnRefreshListener _onRefreshListener) {
			onRefreshListener = _onRefreshListener;
			viewDelegate = _viewDelegate;
		}
	}

	/**
	 * Simple Listener to listen for any callbacks to Refresh.
	 */
	public interface OnRefreshListener {
		/**
		 * Called when the user has initiated a refresh by pulling.
		 * 
		 * @param view
		 *            - View which the user has started the refresh from.
		 */
		public void onRefreshStarted(View view);
	}

	/**
	 * FIXME
	 */
	public static abstract class ViewDelegate {

		/**
		 * Allows you to provide support for View which do not have built-in
		 * support. In this method you should cast <code>view</code> to it's
		 * native class, and check if it is scrolled to the top.
		 * 
		 * @param view
		 *            The view which has should be checked against.
		 * @return true if <code>view</code> is scrolled to the top.
		 */
		public abstract boolean isScrolledToTop(View view);
	}

	protected EnvironmentDelegate createDefaultEnvironmentDelegate() {
		return new EnvironmentDelegate();
	}
	protected HeaderTransformer createDefaultHeaderTransformer() {
		return new DefaultHeaderTransformer();
	}
}
