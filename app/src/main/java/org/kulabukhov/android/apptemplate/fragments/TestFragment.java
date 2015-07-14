package org.kulabukhov.android.apptemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.kulabukhov.android.apptemplate.R;

/**
 * Created by gkulabukhov on 10/07/2015.
 */
public class TestFragment extends BaseFragment {


	//region ==================== Lifecycle callbacks ====================

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_test, container, false);

		view.findViewById(R.id.btn_set_text).setOnClickListener(btnSetTextClickListener);

		return view;
	}

	//endregion

	//region ==================== UI handlers ====================

	private View.OnClickListener btnSetTextClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			String text = ((EditText) getView().findViewById(R.id.edit_text)).getText().toString();
			((TextView) getView().findViewById(R.id.text_view)).setText(text);
		}
	};

	//endregion

}
