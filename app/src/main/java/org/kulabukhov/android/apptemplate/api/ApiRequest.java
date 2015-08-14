package org.kulabukhov.android.apptemplate.api;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

/**
 * Created by gkulabukhov on 15/12/14.
 *
 * @param <T> type of data that requested with this {@link ApiRequest}
 */
public class ApiRequest<T> {

	private String method;
	private HashMap<String, Object> params;

	@Nullable
	private RequestCallback<T> callback;

	private ResultHandler<T> resultHandler;


	public ApiRequest(String method, HashMap<String, Object> params) {
		this.method = method;
		this.params = params;
	}

	public String getMethod() {
		return method;
	}

	public HashMap<String, Object> getParams() {
		return params;
	}

	public void setCallback(@Nullable RequestCallback<T> callback) {
		this.callback = callback;
	}

	public void setResultHandler(ResultHandler<T> resultHandler) {
		this.resultHandler = resultHandler;
	}

	@Nullable
	protected RequestCallback<T> getCallback() {
		return callback;
	}

	protected ResultHandler<T> getResultHandler() {
		return resultHandler;
	}

	public void handleResult(JsonNode result, JsonNode rawResult) {
		resultHandler.handleResult(result, rawResult, callback);
	}

	public void handleError(Exception exception) {
		if (callback != null) {
			callback.postResult(null, null, exception);
		}
	}

}
