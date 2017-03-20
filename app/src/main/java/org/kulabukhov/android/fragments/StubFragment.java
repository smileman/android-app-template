package org.kulabukhov.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.kulabukhov.android.apptemplate.R;

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
	}

	//endregion

}