package org.kulabukhov.android.apptemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import org.kulabukhov.android.apptemplate.R;
import org.kulabukhov.android.apptemplate.api.API;
import org.kulabukhov.android.apptemplate.models.Question;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by gkulabukhov on 27/08/2015.
 */
public class QuestionsFragment extends BaseFragment {

	private SearchView searchView;
	private ListView listView;

	private List<Question> questions;

	//region ==================== Lifecycle callbacks ====================

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_questions, container, false);

		searchView = (SearchView) view.findViewById(R.id.search_view);
		listView = (ListView) view.findViewById(android.R.id.list);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Observable<CharSequence> textChangeObservable = RxSearchView.queryTextChanges(searchView);
		Subscription subscription = textChangeObservable//
						.filter(charSequence -> charSequence.length() > 3)
						.debounce(400, TimeUnit.MILLISECONDS)// default Scheduler is Computation
						.observeOn(AndroidSchedulers.mainThread())
				.subscribe(charSequence -> {
					getQuestions(charSequence.toString());
				});
		subscriptions.add(subscription);

	}

	//endregion

	//region ==================== Setters ====================

	private void setQuestions(List<Question> questions) {
		this.questions = questions;

		listView.setAdapter(new ArrayAdapter<Question>(getActivity(), android.R.layout.simple_list_item_1, questions));
	}

	//endregion

	//region ==================== API request ====================

	private void getQuestions(String searchQuery) {
		subscriptions.add(
				API.getInstance().searchQuestions(searchQuery)
						.flatMap(questions -> Observable.from(questions))
						.map(question -> question.getId())
						.toList()
						.map(ids -> TextUtils.join(";", ids))
						.flatMap(ids -> API.getInstance().getQuestions(ids))
						.subscribe(questions -> {
							setQuestions(questions);
						}, throwable -> {
						}));
	}

	//endregion

}