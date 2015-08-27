package org.kulabukhov.android.apptemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.kulabukhov.android.apptemplate.R;
import org.kulabukhov.android.apptemplate.api.API;
import org.kulabukhov.android.apptemplate.models.Question;

import java.util.List;

import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by gkulabukhov on 01/07/15.
 */
public class StubFragment extends BaseFragment {


	//region ==================== Lifecycle callbacks ====================

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_stub, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getQuestions();
	}

	//endregion

	//region ==================== API request ====================

	private void getQuestions() {
		final long start = System.currentTimeMillis();
		Timber.e("loading started = %d", start);
		subscriptions.add(
				API.getInstance().getQuestions("").subscribe(new Action1<List<Question>>() {
			@Override
			public void call(List<Question> questions) {
				//Timber.d("questions count = %d", questions == null ? 0 : questions.size());
				long end = System.currentTimeMillis();
				Timber.e("loading completed = %d\ntime = %d", end, end - start);

			}
		}, new Action1<Throwable>() {
			@Override
			public void call(Throwable throwable) {
				Timber.e("error = %s", throwable.getLocalizedMessage());
			}
		}));
	}

	//endregion

}
