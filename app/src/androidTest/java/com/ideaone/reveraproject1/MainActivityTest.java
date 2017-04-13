package com.ideaone.reveraproject1;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction imageView = onView(
                allOf(withId(R.id.weather),
                        withParent(withId(R.id.mainActionSet)),
                        isDisplayed()));
        imageView.perform(click());

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.news),
                        withParent(withId(R.id.mainActionSet)),
                        isDisplayed()));
        imageView2.perform(click());

        ViewInteraction imageView3 = onView(
                allOf(withId(R.id.menu),
                        withParent(withId(R.id.mainActionSet)),
                        isDisplayed()));
        imageView3.perform(click());

        ViewInteraction imageView4 = onView(
                allOf(withId(R.id.recreation),
                        withParent(withId(R.id.mainActionSet)),
                        isDisplayed()));
        imageView4.perform(click());

        ViewInteraction imageView5 = onView(
                allOf(withId(R.id.promo),
                        withParent(withId(R.id.mainActionSet)),
                        isDisplayed()));
        imageView5.perform(click());

        ViewInteraction imageView6 = onView(
                allOf(withId(R.id.bulletin),
                        withParent(withId(R.id.mainActionSet)),
                        isDisplayed()));
        imageView6.perform(click());

        ViewInteraction imageView7 = onView(
                allOf(withId(R.id.gallery),
                        withParent(withId(R.id.mainActionSet)),
                        isDisplayed()));
        imageView7.perform(click());
    }
}
