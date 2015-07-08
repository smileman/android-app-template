package org.kulabukhov.android.apptemplate.components;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import org.jetbrains.annotations.NotNull;
import org.kulabukhov.android.apptemplate.R;
import org.kulabukhov.android.apptemplate.fragments.BaseFragment;
import org.kulabukhov.android.apptemplate.fragments.StubFragment;

public class MainActivity extends BaseActivity {


	public BaseFragment currentContentFragment;

	//region ==================== Lifecycle callbacks ====================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState == null) {
			showContentFragment(new StubFragment(), false, true);
		}
		updateActionBarTitle();

		getSupportFragmentManager().addOnBackStackChangedListener(onBackStackListener);

	}

	//endregion

	//region ==================== UI and content updates ====================

	private void updateActionBarTitle() {
		if (getSupportActionBar() != null && currentContentFragment != null) {
			getSupportActionBar().setTitle(currentContentFragment.fragmentTitle());
		}

	}

	private void showContentFragment(@NotNull BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) {
		if (clearBackStack) {
			popEntireFragmentBackStack();
		}

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, fragment);
		if (addToBackStack) {
			ft.addToBackStack(null);
		}
		ft.commit();
		currentContentFragment = fragment;

		updateActionBarTitle();
	}

	private void showContentFragment(@NotNull BaseFragment fragment) {
		showContentFragment(fragment, true, false);
	}

	//endregion

	//region ==================== UI handlers and callbacks ====================

	private FragmentManager.OnBackStackChangedListener onBackStackListener = new FragmentManager.OnBackStackChangedListener() {
		@Override
		public void onBackStackChanged() {
			updateActionBarTitle();
		}
	};

	//endregion

}
