package org.kulabukhov.android.apptemplate.api;

import com.fasterxml.jackson.databind.JsonNode;

import org.jetbrains.annotations.Nullable;

/**
 * Created by gkulabukhov on 16/12/14.
 *
 * @param <T> type of data that handled with this {@link ResultHandler}
 */
public abstract class ResultHandler<T> {

	public abstract void handleResult(@Nullable JsonNode jsonResult, JsonNode rawResult,
									  @Nullable RequestCallback<T> callback);

}
