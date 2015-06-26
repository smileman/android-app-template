package org.kulabukhov.android.apptemplate.api;

import com.fasterxml.jackson.databind.JsonNode;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by gkulabukhov on 22/12/14.
 */
public abstract class ReferenceRequestCallback<T, K> extends RequestCallback<T> {

	protected WeakReference<K> referent;

	public ReferenceRequestCallback(K referent)
	{
		this.referent = new WeakReference<K>(referent);
	}

	public void setReferent(K referent) {
		this.referent = new WeakReference<K>(referent);
	}

	public K getReferent() {
		return referent.get();
	}

	@Override
	protected void returnResult(@Nullable T result, JsonNode rawResult, @Nullable Exception exception) {
		if (referent != null && getReferent() != null) {
			done((T) result, rawResult, exception);
		}
	}
}
