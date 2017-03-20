package org.kulabukhov.android.components;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.kulabukhov.android.apptemplate.R;

/**
 * Application main activity
 */
public class MainActivity extends BaseActivity {

	//region ==================== Lifecycle callbacks ====================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	//endregion

}
