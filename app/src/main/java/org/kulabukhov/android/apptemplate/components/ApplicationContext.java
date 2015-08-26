package org.kulabukhov.android.apptemplate.components;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.kulabukhov.android.apptemplate.BuildConfig;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by gkulabukhov on 15/12/14.
 */
public class ApplicationContext extends Application {

	private static ApplicationContext instance;

	private RefWatcher refWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		Fabric.with(this, new Crashlytics());

		// configure Timber and LeakCanary
		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
		}

		refWatcher = LeakCanary.install(this);

	}

	public static ApplicationContext getInstance() {
		return instance;
	}

	public static RefWatcher getRefWatcher() {
		return ApplicationContext.getInstance().refWatcher;
	}
}
