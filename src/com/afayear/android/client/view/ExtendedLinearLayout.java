package com.afayear.android.client.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.afayear.android.client.view.iface.IExtendedView.OnSizeChangedListener;
import com.afayear.android.client.view.iface.IExtendedViewGroup;
import com.afayear.android.client.view.iface.IExtendedViewGroup.TouchInterceptor;

public class ExtendedLinearLayout extends LinearLayout implements
		IExtendedViewGroup {

	private TouchInterceptor mTouchInterceptor;
	private OnSizeChangedListener mOnSizeChangedListener;

	public ExtendedLinearLayout(final Context context) {
		super(context);
	}

	public ExtendedLinearLayout(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public ExtendedLinearLayout(final Context context,
			final AttributeSet attrs, final int defStyle) {
		// Workaround for pre-Honeycomb devices.
		super(context, attrs);
	}

	@Override
	public final boolean onInterceptTouchEvent(final MotionEvent event) {
		if (mTouchInterceptor != null) {
			final boolean ret = mTouchInterceptor.onInterceptTouchEvent(this,
					event);
			if (ret)
				return true;
		}
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public final boolean onTouchEvent(final MotionEvent event) {
		if (mTouchInterceptor != null) {
			final boolean ret = mTouchInterceptor.onTouchEvent(this, event);
			if (ret)
				return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected final void onSizeChanged(final int w, final int h,
			final int oldw, final int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mOnSizeChangedListener != null) {
			mOnSizeChangedListener.onSizeChanged(this, w, h, oldw, oldh);
		}
	}

	@Override
	public final void setOnSizeChangedListener(
			final OnSizeChangedListener listener) {
		mOnSizeChangedListener = listener;
	}

	@Override
	public final void setTouchInterceptor(final TouchInterceptor listener) {
		mTouchInterceptor = listener;
	}

}
