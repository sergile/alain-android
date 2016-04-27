package net.bradbowie.alain.generator;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import net.bradbowie.alain.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by bradbowie on 4/26/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class GeneratorActivityTest {

    @Rule
    public ActivityTestRule<GeneratorActivity> activityRule = new ActivityTestRule<>(GeneratorActivity.class);

    @Test
    public void initializeEmpty() {
        onView(withId(R.id.generator_idea_list))
                .check(matches(isDisplayed()));

        onView(withId(R.id.generator_play_button))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));

        onView(withId(R.id.generator_pause_button))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void togglePlayPause() {
        onView(withId(R.id.generator_play_button))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.generator_pause_button))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));

        onView(withId(R.id.generator_play_button))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}