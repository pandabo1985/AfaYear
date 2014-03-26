package com.afayear.android.client.fragment.iface;

import android.support.v4.app.Fragment;

public interface SupportFragmentCallback {

	public Fragment getCurrentVisibleFragment();

	public void onDetachFragment(Fragment fragment);

	public void onSetUserVisibleHint(Fragment fragment, boolean isVisibleToUser);

	public boolean triggerRefresh(int position);

}
