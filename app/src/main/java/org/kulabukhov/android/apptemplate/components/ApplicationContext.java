package org.kulabukhov.android.apptemplate.components;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

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

	}

	public static ApplicationContext getInstance() {
		return instance;
	}
}
