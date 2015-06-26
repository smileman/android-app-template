package org.kulabukhov.android.apptemplate.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kulabukhov.android.apptemplate.BuildConfig;
import org.kulabukhov.android.apptemplate.R;
import org.kulabukhov.android.apptemplate.components.ApplicationContext;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;

import timber.log.Timber;

/**
 * Created by gkulabukhov on 15/12/14.
 */
public class API {

	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	private static final int CONNECTION_TIMEOUT = 30 * 1000;


	@NotNull
	private static API instance = new API();

	private AsyncHttpClient httpClient;


	private API() {

		httpClient = new AsyncHttpClient();
		httpClient.setTimeout(CONNECTION_TIMEOUT);

		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(CustomSSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			httpClient.setSSLSocketFactory(sf);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@NotNull
	public static API getInstance() {
		return instance;
	}


	private class HttpResponseHandler extends JacksonHttpResponseHandler {

		protected Request request;

		private HttpResponseHandler(Request request) {
			super();
			this.request = request;
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, @Nullable JsonNode response) {
			if (response == null || statusCode == HttpStatus.SC_NO_CONTENT) {
				request.handleResult(null, null);
				return;
			}

			JsonNode requestResponse = response.path("result");
			request.handleResult(requestResponse.isMissingNode() ? null : requestResponse, response);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, @NotNull Throwable throwable, String rawJsonData,
							  @Nullable JsonNode errorResponse) {
			Timber.w(throwable.toString(), rawJsonData);

			Exception exception =
					getExceptionWithDescription(statusCode, throwable,
							errorResponse == null ? null : errorResponse.toString(),
							request);

			request.handleError(exception);
		}
	}

	@Nullable
	private Exception getExceptionWithDescription(int statusCode, @NotNull Throwable e, @Nullable String errorString,
												  Request request) {
		e.printStackTrace();

		String errorDescription = null;
		if (e instanceof HttpHostConnectException) {
			errorDescription = ApplicationContext.getInstance().getResources().getString(
					R.string.error_message_network_unavailable_try_again_later);
		} else if (e instanceof UnknownHostException) {
			errorDescription = ApplicationContext.getInstance().getResources()
					.getString(R.string.error_message_network_unavailable_check_internet_connection);
		} else if (e instanceof HttpResponseException) {
			errorDescription = ApplicationContext.getInstance().getResources().getString(
					R.string.error_message_internal_error_occurred_try_again_later);
		} else if (e instanceof SocketTimeoutException) {
			errorDescription = ApplicationContext.getInstance().getResources()
					.getString(R.string.error_message_network_unavailable_check_internet_connection);
		} else if (e instanceof IOException) {
			errorDescription = ApplicationContext.getInstance().getResources()
					.getString(R.string.error_message_network_unavailable_check_internet_connection);
		} else if (errorString != null) {
			errorDescription = errorString;
		} else {
			errorDescription = e.getLocalizedMessage();
		}

		return new Exception(errorDescription, e);
	}

	@Nullable
	private String getHttpBody(@NotNull Request<?> request) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			String stringBody = objectMapper.writeValueAsString(request.getParams());

			Object json = objectMapper.readValue(stringBody, Object.class);

			if (BuildConfig.DEBUG) {
				Timber.w("Request body: %s", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
			}

			if (stringBody != null) {
				return stringBody;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

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

	private void sendGetRequest(@NotNull Request<?> request) {
		httpClient.get(null, Configuration.SERVER_API_URL.concat(request.getMethod()), new HttpResponseHandler(request));
	}

	private <T> void sendRequest(@NotNull Request<T> request, ResultHandler<T> resultHandler,
								 RequestCallback<T> callback) {
		request.setResultHandler(resultHandler);
		request.setCallback(callback);
		sendGetRequest(request);
	}

}
