package org.kulabukhov.android.apptemplate.components;

import android.os.Bundle;

import org.kulabukhov.android.apptemplate.R;
import org.kulabukhov.android.apptemplate.fragments.StubFragment;

public class MainActivity extends BaseActivity {


	//region ==================== Lifecycle callbacks ====================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new StubFragment()).commit();
		}

	}

	//endregion

}
