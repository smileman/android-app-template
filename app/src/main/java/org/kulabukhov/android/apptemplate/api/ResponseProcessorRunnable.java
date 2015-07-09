package org.kulabukhov.android.apptemplate.api;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by gkulabukhov on 16/12/14.
 */
public class ResponseProcessorRunnable implements Runnable {

	private final AsyncResultHandler resultHandler;
	private final JsonNode unhandledResponse;
	private final JsonNode rawResponse;

	public ResponseProcessorRunnable(AsyncResultHandler resultHandler, JsonNode unhandledResponse,
									 JsonNode rawResponse) {
		this.resultHandler = resultHandler;
		this.unhandledResponse = unhandledResponse;
		this.rawResponse = rawResponse;
	}

	@Override
	public void run() {
		Object processedResponse = resultHandler.parseResponse(unhandledResponse);
		resultHandler.handleProcessedResult(processedResponse, rawResponse);
	}
}
