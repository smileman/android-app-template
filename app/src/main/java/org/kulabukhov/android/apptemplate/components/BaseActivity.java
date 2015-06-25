package org.kulabukhov.android.apptemplate.components;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.kulabukhov.android.commons.helpers.SoftKeyboardHelper;

/**
 * Created by gkulabukhov on 25/06/15.
 */
public class BaseActivity extends AppCompatActivity {

	protected void hideSoftKeyboard() {
		SoftKeyboardHelper.hideSoftKeyboard(this);
	}

	protected void showSoftKeyboard(View view) {
		SoftKeyboardHelper.showSoftKeyboard(this, view);
	}

}
