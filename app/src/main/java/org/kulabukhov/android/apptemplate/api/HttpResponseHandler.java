package org.kulabukhov.android.apptemplate.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.HttpHostConnectException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kulabukhov.android.apptemplate.R;
import org.kulabukhov.android.apptemplate.components.ApplicationContext;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import timber.log.Timber;

/**
 * Created by gkulabukhov on 26/06/15.
 */
class HttpResponseHandler extends JacksonOkHttpCallback {

	protected ApiRequest apiRequest;

	HttpResponseHandler(ApiRequest apiRequest) {
		super();
		this.apiRequest = apiRequest;
	}

	@Override
	public void onResponse(Response response, JsonNode jsonResult) throws IOException {
		if (response == null) {
			apiRequest.handleResult(null, null);
			return;
		}


		apiRequest.handleResult(jsonResult.isMissingNode() ? null : jsonResult, jsonResult);
	}

	@Override
	public void onFailure(Request request, IOException e) {
		Timber.w(e.toString(), request.toString());

		Exception exception = getExceptionWithDescription(e);

		this.apiRequest.handleError(exception);
	}

	@Nullable
	private Exception getExceptionWithDescription(@NotNull Throwable e) {
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
		} else {
			errorDescription = e.getLocalizedMessage();
		}

		return new Exception(errorDescription, e);
	}
}
