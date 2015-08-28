package org.kulabukhov.android.apptemplate.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.HttpMethod;

import org.kulabukhov.android.apptemplate.BuildConfig;
import org.kulabukhov.android.apptemplate.models.Question;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
		if (BuildConfig.DEBUG) {
			httpClient.interceptors().add(new LoggingInterceptor());
		}

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
	private RequestBody getHttpBody(HashMap<String, Object> params) {
		if (params == null) {
			return null;
		}

		ObjectMapper objectMapper = getObjectMapper();
		try {
			String stringBody = objectMapper.writeValueAsString(params);

			/*Object json = objectMapper.readValue(stringBody, Object.class);

			if (BuildConfig.DEBUG) {
				Timber.w("Request body: %s",
						objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
			}*/

			if (stringBody != null) {
				return RequestBody.create(MediaType.parse(CONTENT_TYPE_JSON), stringBody);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


		return null;
	}

	@NonNull
	private HttpUrl getHttpUrl(@NonNull String httpMethod, @NonNull ApiRequest<?> apiRequest) {
		HttpUrl url = HttpUrl.parse(Configuration.SERVER_API_URL.concat(apiRequest.getMethod()));

		HashMap<String, Object> params = apiRequest.getParams();
		if (httpMethod.equals(METHOD_GET) && params != null) {
			HttpUrl.Builder builder = url.newBuilder();
			for (String key : params.keySet()) {
				builder.addQueryParameter(key, params.get(key).toString());
			}

			url = builder.build();
		}

		return url;
	}

	@Nullable
	protected JsonNode parseResponse(Reader rawJsonData) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(rawJsonData);
			if (BuildConfig.DEBUG) {
				Timber.w("Request response: %s", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
			}
			return jsonNode;
		} catch (IOException e) {
			e.printStackTrace();
			Crashlytics.getInstance().core.logException(e);
		}

		return null;
	}

	private <T> Observable<T> sendRequest(@NonNull final String httpMethod, @NonNull final ApiRequest<T> apiRequest,
								 @NonNull final ResultHandler<T> resultHandler) {
		return Observable.create(new Observable.OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> subscriber) {
				RequestBody requestBody = null;
				if (HttpMethod.requiresRequestBody(httpMethod)) {
					requestBody = getHttpBody(apiRequest.getParams());
				}

				HttpUrl url = getHttpUrl(httpMethod, apiRequest);
				Request request = new Request.Builder()
						.url(url)
						.method(httpMethod, requestBody)
						.build();

				try {
					Response response = httpClient.newCall(request).execute();
					JsonNode jsonNode = parseResponse(response.body().charStream());
					T result = resultHandler.handleResult(jsonNode, jsonNode);
					subscriber.onNext(result);
					subscriber.onCompleted();
				} catch (IOException e) {
					e.printStackTrace();
					subscriber.onError(e);
				}
			}
		}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
	}

	//endregion


	//region ==================== API requests ====================

	public Observable<List<Question>> getQuestions(@NonNull String searchQuery) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("order", "desc");
		params.put("sort", "activity");
		params.put("intitle", searchQuery);
		params.put("site", "stackoverflow");
		return sendRequest(METHOD_GET,
				new ApiRequest<List<Question>>("search",
						params), new ResultHandler<List<Question>>() {
					@Override
					public List<Question> handleResult(@Nullable JsonNode jsonResult, JsonNode rawResult) {
						if (jsonResult == null || jsonResult.isNull() || !jsonResult.isObject()) {
							return null;
						}
						ObjectMapper objectMapper = getObjectMapper();
						List<Question> questions = null;
						try {
							questions = objectMapper.readValue(jsonResult.path("items").traverse(),
									new TypeReference<List<Question>>() {
									});
						} catch (IOException e) {
							e.printStackTrace();
							Crashlytics.getInstance().core.logException(e);
						}
						return questions;
					}
				});
	}

	//endregion

}
