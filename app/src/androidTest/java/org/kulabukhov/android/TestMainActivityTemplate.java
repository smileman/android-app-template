package org.kulabukhov.android;

import android.support.test.espresso.matcher.ViewMatchers;

import org.junit.Test;
import org.kulabukhov.android.apptemplate.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by gkulabukhov on 10/07/2015.
 */
public class TestMainActivityTemplate extends BaseTestMainActivity {

	@Test
	public void testTemplateMethod() {
		// TODO add espresso testing code
	}

	@Test
	public void testStubFragmentShown() {
		onView(ViewMatchers.withId(R.id.tv_stub))
				.check(matches(isDisplayed()));
	}

}
