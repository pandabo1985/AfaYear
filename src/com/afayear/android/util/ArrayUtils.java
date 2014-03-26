package com.afayear.android.util;

public final class ArrayUtils {

	private ArrayUtils() {
		throw new AssertionError(
				"You are trying to create an instance for this utility class!");
	}

	public static boolean contains(final long[] array, final long value) {
		if (array == null)
			return false;
		for (final long item : array) {
			if (item == value)
				return true;
		}
		return false;
	}

}
