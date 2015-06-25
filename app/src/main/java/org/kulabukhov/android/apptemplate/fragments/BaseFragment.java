package org.kulabukhov.android.apptemplate.fragments;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;
import org.kulabukhov.android.commons.helpers.SoftKeyboardHelper;

/**
 * Created by gkulabukhov on 25/06/15.
 */
public class BaseFragment extends Fragment {

	protected void showToastWithMessage(@Nullable String message) {
		if (getActivity() != null && message != null && message.length() > 0) {
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		}
	}

	protected void showToastWithMessage(@StringRes int resourceId) {
		if (getActivity() != null) {
			Toast.makeText(getActivity(), resourceId, Toast.LENGTH_LONG).show();
		}
	}

	protected void hideSoftKeyboard() {
		SoftKeyboardHelper.hideSoftKeyboard(getActivity());
	}

	protected void showSoftKeyboard(View view) {
		SoftKeyboardHelper.showSoftKeyboard(getActivity(), view);
	}

}
