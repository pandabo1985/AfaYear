package com.afayear.android.util;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import android.os.Handler;

public class AsyncTaskManager {

	private static AsyncTaskManager sInstance;
	private final Handler mHandler;
	private final ArrayList<ManagedAsyncTask<?, ?, ?>> mTasks = new ArrayList<ManagedAsyncTask<?, ?, ?>>();

	AsyncTaskManager() {
		this(new Handler());
	}

	AsyncTaskManager(final Handler handler) {
		mHandler = handler;
	}

	/**
	 * 
	 * @param task
	 * @param exec
	 *            true 为执行
	 * @param params
	 * @return
	 */
	public <T> int add(final ManagedAsyncTask<T, ?, ?> task,
			final boolean exec, final T... params) {
		final int hashCode = task.hashCode();
		mTasks.add(task);
		if (exec) {
			execute(hashCode);
		}
		return hashCode;
	}

	public boolean cancel(final int hashCode) {
		return cancel(hashCode, true);
	}

	public boolean cancel(final int hashCode,
			final boolean mayInterruptIfRunning) {
		final ManagedAsyncTask<?, ?, ?> task = findTask(hashCode);
		if (task != null) {
			task.cancel(mayInterruptIfRunning);
			mTasks.remove(task);
			return true;
		}
		return false;
	}

	public void cancelAll() {
		for (final ManagedAsyncTask<?, ?, ?> task : getTaskSpecList()) {
			task.cancel(true);
		}
		mTasks.clear();
	}

	public <T> boolean execute(final int hashCode, final T... params) {
		final ManagedAsyncTask<T, ?, ?> task = (ManagedAsyncTask<T, ?, ?>) findTask(hashCode);
		if (task != null) {
			task.execute(params == null || params.length == 0 ? null : params);
			return true;
		}
		return false;
	}

	public Handler getHandler() {
		return mHandler;
	}

	private <T> ManagedAsyncTask<?, ?, ?> findTask(final int hashCode) {
		for (final ManagedAsyncTask<?, ?, ?> task : getTaskSpecList()) {
			if (hashCode == task.hashCode())
				return task;
		}
		return null;
	}

	public ArrayList<ManagedAsyncTask<?, ?, ?>> getTaskSpecList() {
		return new ArrayList<ManagedAsyncTask<?, ?, ?>>(mTasks);
	}

	public boolean hasRunningTask() {
		for (final ManagedAsyncTask<?, ?, ?> task : getTaskSpecList()) {
			if (task.getStatus() == ManagedAsyncTask.Status.RUNNING)
				return true;
		}
		return false;
	}

	public boolean hasRunningTasksForTag(final String tag) {
		if (tag == null)
			return false;
		for (final ManagedAsyncTask<?, ?, ?> task : getTaskSpecList()) {
			if (task.getStatus() == ManagedAsyncTask.Status.RUNNING
					&& tag.equals(task.getTag()))
				return true;
		}
		return false;
	}

	public boolean isExcuting(final int hashCode) {
		final ManagedAsyncTask<?, ?, ?> task = findTask(hashCode);
		if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
			return true;
		return false;
	}

	public void remove(final int hashCode) {
		try {
			mTasks.remove(findTask(hashCode));
		} catch (final ConcurrentModificationException e) {
			// Ignore.
		}
	}

	public static AsyncTaskManager getInstance() {
		if (sInstance == null) {
			sInstance = new AsyncTaskManager();
		}
		return sInstance;
	}
}
