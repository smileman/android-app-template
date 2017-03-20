package org.kulabukhov.android.components;

import android.support.v4.app.FragmentManager;
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

	protected void popEntireFragmentBackStack() {
		FragmentManager fm = getSupportFragmentManager();
		popEntireFragmentBackStack(fm);
	}

	public static void popEntireFragmentBackStack(FragmentManager fragmentManager) {
		fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

}
