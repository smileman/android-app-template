package org.kulabukhov.android.fragments;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import org.kulabukhov.android.components.ApplicationContext;
import org.kulabukhov.android.helpers.RxUtils;
import org.kulabukhov.android.commons.helpers.SoftKeyboardHelper;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by gkulabukhov on 25/06/15.
 */
public class BaseFragment extends Fragment {

	protected CompositeSubscription subscriptions = new CompositeSubscription();

	@Override
	public void onResume() {
		super.onResume();
		subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(subscriptions);
	}

	@Override
	public void onPause() {
		super.onPause();

		RxUtils.unsubscribeIfNotNull(subscriptions);
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
