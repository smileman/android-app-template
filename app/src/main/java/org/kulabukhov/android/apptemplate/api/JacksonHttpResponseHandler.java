package org.kulabukhov.android.apptemplate.api;

import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.jetbrains.annotations.Nullable;
import org.kulabukhov.android.apptemplate.BuildConfig;

import java.io.IOException;

import timber.log.Timber;

/**
* Created by gkulabukhov on 17/12/14.
*/
abstract class JacksonHttpResponseHandler extends BaseJsonHttpResponseHandler<JsonNode> {

	@Nullable
	@Override
	protected JsonNode parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
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
}
