package org.kulabukhov.android.api;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * Created by gkulabukhov on 16/12/14.
 *
 * @param <T> type of data that handled with this {@link ResultHandler}
 */
public abstract class ResultHandler<T> {

	public abstract T handleResult(@Nullable JsonNode jsonResult, JsonNode rawResult);

}