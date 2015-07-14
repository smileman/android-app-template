package org.kulabukhov.android.apptemplate;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by gkulabukhov on 10/07/2015.
 */
public class TestMainActivityInputText extends BaseTestMainActivity {

	public static final String STRING_TO_BE_TYPED = "Espresso";

	@Test
	public void changeText_sameActivity() throws InterruptedException {
		// Type text and then press the button.
		onView(withId(R.id.edit_text))
				.perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

		Thread.sleep(500);
		onView(withId(R.id.btn_set_text)).perform(click());


		// Check that the text was changed.
		onView(withId(R.id.text_view)).check(matches(withText(STRING_TO_BE_TYPED)));
	}

}
