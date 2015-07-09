package org.kulabukhov.android.apptemplate.api;

import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.Nullable;
import org.kulabukhov.android.apptemplate.BuildConfig;

import java.io.IOException;
import java.io.Reader;

import timber.log.Timber;

/**
 * Created by gkulabukhov on 26/06/15.
 */
public abstract class JacksonOkHttpCallback implements Callback {

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

	@Override
	public void onResponse(Response response) throws IOException {
		onResponse(response, parseResponse(response.body().charStream()));
	}

	public abstract void onResponse(Response response, JsonNode jsonResult) throws IOException;
}
