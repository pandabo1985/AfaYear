package android.support.v4.app;

import android.support.v4.app.BackStackRecord;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class BackStackEntryTrojan {

	public static Fragment getFragmentInBackStackRecord(final FragmentManager.BackStackEntry entry) {
		if (entry instanceof BackStackRecord) return ((BackStackRecord) entry).mHead.fragment;
		return null;
	}
}
