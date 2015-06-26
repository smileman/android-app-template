package org.kulabukhov.android.apptemplate.components;

import android.os.Bundle;

import org.kulabukhov.android.apptemplate.R;

public class MainActivity extends BaseActivity {


	//region ==================== Lifecycle callbacks ====================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	//endregion

}
