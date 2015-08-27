package org.kulabukhov.android.apptemplate.api;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import timber.log.Timber;

/**
 * Created by gkulabukhov on 27/08/2015.
 */
class LoggingInterceptor implements Interceptor {
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();

		long t1 = System.nanoTime();
		Timber.i("Sending request %s on %s%n%s with body:%n%s%n",
				request.url(), chain.connection(), request.headers(), request.body());

		Response response = chain.proceed(request);

		long t2 = System.nanoTime();
		Timber.i("Received response for %s in %.1fms%n%s",
				response.request().url(), (t2 - t1) / 1e6d, response.headers());

		return response;
	}
}