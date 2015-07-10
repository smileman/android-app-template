package org.kulabukhov.android.apptemplate.api;

import android.os.Handler;
import android.os.Looper;

import com.fasterxml.jackson.databind.JsonNode;

import org.jetbrains.annotations.Nullable;

/**
 * Created by gkulabukhov on 16/12/14.
 *
 * @param <T> type of data that requested with this {@link RequestCallback}
 */
public abstract class RequestCallback<T> {

	protected RequestCallback() {
	}

	public void postResult(@Nullable final T result, @Nullable final JsonNode rawResult,
						   @Nullable final Exception exception) {
		// post on UI thread
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			@Override
			public void run() {
				returnResult(result, rawResult, exception);
			}
		});
	}

	protected void returnResult(@Nullable T result, @Nullable JsonNode rawResult, @Nullable Exception exception) {
		done(result, rawResult, exception);
	}

	public void done(@Nullable T result, @Nullable JsonNode rawResult, @Nullable Exception exception) {
		done(result, exception);
	}

	public abstract void done(@Nullable T result, @Nullable Exception exception);

}
