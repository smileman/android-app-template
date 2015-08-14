package org.kulabukhov.android.apptemplate.api;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by gkulabukhov on 22/12/14.
 *
 * @param <T> type of data that returned with this {@link RequestCallback}
 * @param <K> type of callback callback referent. Must be subclass of {@link Fragment}
 */
public abstract class FragmentRequestCallback<T, K extends Fragment> extends ReferenceRequestCallback<T, K> {

	public FragmentRequestCallback(K owner) {
		super(owner);
	}

	@Override
	protected void returnResult(@Nullable T result, JsonNode rawResult, @Nullable Exception exception) {
		if (referent != null && getReferent() != null && getReferent().isAdded() && getReferent().isResumed()) {
			done(result, rawResult, exception);
		}
	}

}
