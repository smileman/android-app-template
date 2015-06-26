package org.kulabukhov.android.apptemplate.components;

import android.app.Application;
import com.crashlytics.android.Crashlytics;

import org.kulabukhov.android.apptemplate.BuildConfig;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by gkulabukhov on 15/12/14.
 */
public class ApplicationContext extends Application {

	private static ApplicationContext instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		Fabric.with(this, new Crashlytics());

		// configure Timber
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		}

	}

	public static ApplicationContext getInstance() {
		return instance;
	}
}
