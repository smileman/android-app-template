package org.kulabukhov.android.apptemplate.components;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import org.kulabukhov.android.apptemplate.R;
import org.kulabukhov.android.apptemplate.fragments.BaseFragment;
import org.kulabukhov.android.apptemplate.fragments.SideMenuFragment;
import org.kulabukhov.android.apptemplate.fragments.StubFragment;

import timber.log.Timber;

/**
 * Application main activity
 */
public class MainActivity extends BaseActivity implements SideMenuFragment.SideMenuFragmentListener {

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;

	private SideMenuFragment sideMenuFragment;
	private BaseFragment currentContentFragment;

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

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		configureNavigationDrawer(toolbar, toolbarBackButtonPressed);

		sideMenuFragment = (SideMenuFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_side_menu);

		if (savedInstanceState == null) {
			showContentFragment(new StubFragment(), false, true);
		}
		updateActionBarTitle();

		getSupportFragmentManager().addOnBackStackChangedListener(onBackStackListener);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
			drawerLayout.closeDrawer(Gravity.LEFT);
			return;
		}

		updateActionBarTitle();
		super.onBackPressed();
	}

	//endregion

	//region ==================== UI configuration ====================

	private void configureNavigationDrawer(Toolbar toolbar, View.OnClickListener toolbarNavigationClickListener) {
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
				R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				supportInvalidateOptionsMenu();
			}
		};
		drawerToggle.setToolbarNavigationClickListener(toolbarNavigationClickListener);
		drawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.syncState();
	}

	//endregion

	//region ==================== UI and content updates ====================

	private void updateActionBarTitle() {
		if (getSupportActionBar() != null && currentContentFragment != null) {
			getSupportActionBar().setTitle(currentContentFragment.fragmentTitle());
		}

	}

	private void showContentFragment(@NonNull BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) {
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

	private void showContentFragment(@NonNull BaseFragment fragment) {
		showContentFragment(fragment, true, false);
	}

	//endregion

	//region ==================== UI handlers and callbacks ====================

	private FragmentManager.OnBackStackChangedListener onBackStackListener =
			new FragmentManager.OnBackStackChangedListener() {
				@Override
				public void onBackStackChanged() {
					updateActionBarTitle();
				}
			};

	private View.OnClickListener toolbarBackButtonPressed = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};

	//endregion

	//region ==================== SideMenuFragment.SideMenuFragmentListener ====================

	@Override
	public void onSideMenuItemSelected() {
		Timber.d("Side menu item clicked");
	}

	//endregion

}
