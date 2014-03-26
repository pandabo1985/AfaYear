/*
 *				Twidere - Twitter client for Android
 * 
 * Copyright (C) 2012 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.afayear.android.client.adapter;


import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.afayear.android.client.Constants;
import com.afayear.android.client.fragment.iface.RefreshScrollTopInterface;
import com.afayear.android.client.fragment.iface.SupportFragmentCallback;
import com.afayear.android.client.model.SupportTabSpec;
import com.afayear.android.client.view.TabPageIndicator;
import com.afayear.android.client.view.TabPageIndicator.TabListener;
import com.afayear.android.client.view.TabPageIndicator.TabProvider;
import static com.afayear.android.util.CustomTabUtils.getTabIconDrawable;
import static com.afayear.android.util.Utils.announceForAccessibilityCompat;

public class SupportTabsAdapter extends FragmentStatePagerAdapter implements TabProvider, TabListener, Constants {

	private final ArrayList<SupportTabSpec> mTabs = new ArrayList<SupportTabSpec>();

	private final Context mContext;
	private final TabPageIndicator mIndicator;

	public SupportTabsAdapter(final Context context, final FragmentManager fm, final TabPageIndicator indicator) {
		super(fm);
		mContext = context;
		mIndicator = indicator;
		clear();
	}

	public void addTab(final Class<? extends Fragment> cls, final Bundle args, final String name, final Integer icon,
			final int position) {
		addTab(new SupportTabSpec(name, icon, cls, args, position));
	}

	public void addTab(final SupportTabSpec spec) {
		mTabs.add(spec);
		notifyDataSetChanged();
	}

	public void addTabs(final Collection<? extends SupportTabSpec> specs) {
		mTabs.addAll(specs);
		notifyDataSetChanged();
	}

	public void clear() {
		mTabs.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public Fragment getItem(final int position) {
		final Fragment fragment = Fragment.instantiate(mContext, mTabs.get(position).cls.getName());
		fragment.setArguments(mTabs.get(position).args);
		return fragment;
	}

	@Override
	public Drawable getPageIcon(final int position) {
		return getTabIconDrawable(mContext, mTabs.get(position).icon);
	}

	@Override
	public CharSequence getPageTitle(final int position) {
		return mTabs.get(position).name;
	}

	public SupportTabSpec getTab(final int position) {
		return position >= 0 && position < mTabs.size() ? mTabs.get(position) : null;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (mIndicator != null) {
			mIndicator.notifyDataSetChanged();
		}
	}

	@Override
	public void onPageReselected(final int position) {
		if (!(mContext instanceof SupportFragmentCallback)) return;
		final Fragment f = ((SupportFragmentCallback) mContext).getCurrentVisibleFragment();
		if (f instanceof RefreshScrollTopInterface) {
			((RefreshScrollTopInterface) f).scrollToStart();
		}
	}

	@Override
	public void onPageSelected(final int position) {
		if (mIndicator == null) return;
		announceForAccessibilityCompat(mContext, mIndicator, getPageTitle(position), getClass());
	}

	@Override
	public boolean onTabLongClick(final int position) {
		if (!(mContext instanceof SupportFragmentCallback)) return false;
		if (((SupportFragmentCallback) mContext).triggerRefresh(position)) return true;
		final Fragment f = ((SupportFragmentCallback) mContext).getCurrentVisibleFragment();
		if (f instanceof RefreshScrollTopInterface) return ((RefreshScrollTopInterface) f).triggerRefresh();
		return false;
	}

	// @Override
	// public float getPageWidth(int position) {
	// return 0.5f;
	// }
}
