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

}
