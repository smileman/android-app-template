package org.kulabukhov.android.apptemplate.api;

import com.fasterxml.jackson.databind.JsonNode;

import org.jetbrains.annotations.Nullable;

/**
 * Created by gkulabukhov on 16/12/14.
 */
public abstract class AsyncResultHandler<T> extends ResultHandler<T> {

	@Nullable
	private RequestCallback<T> callback;

	private T handledResult;
	private JsonNode rawResponse;

	@Override
	public void handleResult(@Nullable JsonNode unhandledResult, @Nullable JsonNode rawResult,
							 @Nullable RequestCallback<T> callback) {
		this.callback = callback;

		BackgroundResponseProcessor.getInstance().processRequestResult(this, unhandledResult, rawResult);
	}

	@Nullable
	public abstract T parseResponse(@Nullable JsonNode jsonResult);

	public void handleProcessedResult(T processedResult, JsonNode rawResponse) {
		this.handledResult = processedResult;
		this.rawResponse = rawResponse;
		postProcessedResult();
	}

	public void postProcessedResult() {
		if (callback != null) {
			callback.postResult(handledResult, rawResponse, null);
		}
	}

}
