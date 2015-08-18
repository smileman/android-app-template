package org.kulabukhov.android.apptemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.kulabukhov.android.apptemplate.R;


/**
 * Created by gkulabukhov on 17/08/2015.
 */
public class SideMenuFragment extends BaseFragment {


	private NavigationView navigationView;
	private View menuHeaderView;

	//region ==================== Lifecycle callbacks ====================

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_side_menu, container, false);

		navigationView = (NavigationView) view.findViewById(R.id.navigation_view);

		menuHeaderView = navigationView.inflateHeaderView(R.layout.fragment_side_menu_header);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);


		navigationView.inflateMenu(R.menu.fragment_side_menu);
		navigationView.setNavigationItemSelectedListener(onMenuItemSelectedListener);


	}

	//endregion

	//region ==================== UI handlers ====================

	private NavigationView.OnNavigationItemSelectedListener onMenuItemSelectedListener =
			new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem) {
					menuItem.setChecked(true);
					return true;
				}
			};

	//endregion

}
