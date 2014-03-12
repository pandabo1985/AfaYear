package com.afayear.log;

import android.content.Context;

public class PandaLog {
	private static boolean isTest = true;

	public static void log(String tag, String info) {
		StackTraceElement[] ste = new Throwable().getStackTrace();
		int i = 1;

		if (isTest) {
			StackTraceElement s = ste[i];
			android.util.Log.e(tag, String.format("======[%s][%s][%s]=====%s",
					s.getClassName(), s.getLineNumber(), s.getMethodName(),
					info));
		}
	}
	public static void log(Context tag, String info) {
//		tag.getResources().getIdentifier();
		StackTraceElement[] ste = new Throwable().getStackTrace();
		int i = 1;
		
		if (isTest) {
			StackTraceElement s = ste[i];
			android.util.Log.e(tag.getClass().getSimpleName(), String.format("======[%s][%s][%s]=====%s",
					s.getClassName(), s.getLineNumber(), s.getMethodName(),
					info));
		}
	}
}
