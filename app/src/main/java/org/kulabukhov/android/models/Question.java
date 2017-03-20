package org.kulabukhov.android.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by gkulabukhov on 27/08/2015.
 */
public class Question {

	@JsonProperty(value = "question_id")
	private String id;

	private String title;

	private String body;

	//region ==================== Getters ====================

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	//endregion

	@Override
	public String toString() {
		return title + "\n" + body;
	}
}
