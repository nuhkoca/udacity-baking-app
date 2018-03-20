package com.nuhkoca.udacitybakingapp;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nuhkoca.udacitybakingapp.util.ConfigurationDetector;
import com.nuhkoca.udacitybakingapp.view.recipe.activity.RecipeActivity;
import com.nuhkoca.udacitybakingapp.view.recipe.fragment.RecipeFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeAdapterItemOpensActivityTest {

    private static final String ACTIVITY_PHONE_TITLE = "Steps";
    private static final String ACTIVITY_TABLET_TITLE = "Steps & Ingredients";

    private IdlingResource mIdlingResource;

    private int orientation = ConfigurationDetector.isTabletInLandscapeMode(getInstrumentation().getContext());

    @Before
    public void registerIdlingResource() {
        mIdlingResource = RecipeFragment.getInstance().getIdlingResource();

        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void recyclerViewItem_OpensActivity() {
        if (!isTablet()
                || (isTablet() && orientation == Configuration.ORIENTATION_PORTRAIT)) {

            onView(withId(R.id.rvRecipes))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            onView(withId(R.id.tvToolbarHeader)).check(matches(withText(ACTIVITY_PHONE_TITLE)));
        } else {
            onView(withId(R.id.rvRecipes))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            onView(withId(R.id.tvToolbarHeader)).check(matches(withText(ACTIVITY_TABLET_TITLE)));
        }
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    private boolean isTablet(){
        Resources resources = getInstrumentation().getTargetContext().getResources();

        return resources.getBoolean(R.bool.isTablet);
    }
}