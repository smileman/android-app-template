package org.kulabukhov.android.apptemplate.models;

/**
 * Created by gkulabukhov on 27/08/2015.
 */
public class Question {

	private String title;

	//region ==================== Getters ====================

	public String getTitle() {
		return title;
	}

	//endregion


	@Override
	public String toString() {
		return title;
	}
}
