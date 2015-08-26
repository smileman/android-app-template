package org.kulabukhov.android.apptemplate.fragments;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;

import org.kulabukhov.android.apptemplate.components.ApplicationContext;
import org.kulabukhov.android.commons.helpers.SoftKeyboardHelper;

/**
 * Created by gkulabukhov on 25/06/15.
 */
public class BaseFragment extends Fragment {

	@Override
	public void onDestroy() {
		super.onDestroy();
		RefWatcher refWatcher = ApplicationContext.getRefWatcher();
		refWatcher.watch(this);
	}

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

	/**
	 * Fragment title to be shown in app bar. Override this method in subclasses.
	 *
	 * @return fragment title
	 */
	@Nullable
	public String fragmentTitle() {
		return null;
	}
}
