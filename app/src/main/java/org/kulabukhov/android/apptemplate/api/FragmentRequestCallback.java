package org.kulabukhov.android.apptemplate.api;

import android.support.v4.app.Fragment;

import com.fasterxml.jackson.databind.JsonNode;

import org.jetbrains.annotations.Nullable;

/**
 * Created by gkulabukhov on 22/12/14.
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
