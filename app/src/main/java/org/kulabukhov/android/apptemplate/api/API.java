package org.kulabukhov.android.apptemplate.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.internal.http.HttpMethod;

import org.kulabukhov.android.apptemplate.BuildConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import timber.log.Timber;

/**
 * Created by gkulabukhov on 15/12/14.
 */
public final class API {

	private static final int CONNECTION_TIMEOUT = 30;
	private static final String CONTENT_TYPE_JSON = "application/json";

	private static final String METHOD_GET = "GET";
	private static final String METHOD_POST = "POST";
	private static final String METHOD_PUT = "PUT";
	private static final String METHOD_PATCH = "PATCH";
	private static final String METHOD_DELETE = "DELETE";

	@NonNull
	private static API instance = new API();

	private final OkHttpClient httpClient;

	private API() {

		httpClient = new OkHttpClient();
		httpClient.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);

		//region ==================== SSL ====================

		try {
			SSLSocketFactory sf = new UnsecureSSLSocketFactory();
			httpClient.setSslSocketFactory(sf);
			httpClient.setHostnameVerifier(null);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		//endregion

	}

	@NonNull
	public static API getInstance() {
		return instance;
	}

	//region ==================== Internal request logic ====================

	private ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setVisibilityChecker(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.DEFAULT)
				.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		return objectMapper;
	}

	@Nullable
	private RequestBody getHttpBody(@NonNull ApiRequest<?> apiRequest) {
		if (apiRequest.getParams() == null) {
			return null;
		}

		ObjectMapper objectMapper = getObjectMapper();
		try {
			String stringBody = objectMapper.writeValueAsString(apiRequest.getParams());

			Object json = objectMapper.readValue(stringBody, Object.class);

			if (BuildConfig.DEBUG) {
				Timber.w("Request body: %s",
						objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
			}

			if (stringBody != null) {
				return RequestBody.create(MediaType.parse(CONTENT_TYPE_JSON), stringBody);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


		return null;
	}

	@NonNull
	private HttpUrl getHttpUrl(@NonNull String method, @NonNull ApiRequest<?> apiRequest) {
		HttpUrl url = HttpUrl.parse(Configuration.SERVER_API_URL.concat(apiRequest.getMethod()));

		HashMap<String, Object> params = apiRequest.getParams();
		if (method.equals(METHOD_GET) && params != null) {
			HttpUrl.Builder builder = url.newBuilder();
			for (String key : params.keySet()) {
				builder.addQueryParameter(key, params.get(key).toString());
			}

			url = builder.build();
		}

		return url;
	}

	private <T> void sendRequest(@NonNull String method, @NonNull ApiRequest<T> apiRequest,
								 @NonNull ResultHandler<T> resultHandler,
								 @Nullable RequestCallback<T> callback) {
		apiRequest.setResultHandler(resultHandler);
		apiRequest.setCallback(callback);

		RequestBody requestBody = null;
		if (HttpMethod.requiresRequestBody(method)) {
			requestBody = getHttpBody(apiRequest);
		}

		HttpUrl url = getHttpUrl(method, apiRequest);
		Request request = new Request.Builder()
				.url(url)
				.method(method, requestBody)
				.build();

		httpClient.newCall(request).enqueue(new HttpResponseHandler(apiRequest));
	}

	//endregion


	//region ==================== API requests ====================


	//endregion

}
