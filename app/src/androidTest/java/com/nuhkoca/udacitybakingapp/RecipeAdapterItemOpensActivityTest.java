package com.nuhkoca.udacitybakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nuhkoca.udacitybakingapp.view.recipe.activity.RecipeActivity;
import com.nuhkoca.udacitybakingapp.view.recipe.fragment.RecipeFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    private static final String ACTIVITY_TITLE = "Steps";

    private IdlingResource mIdlingResource;


    @Before
    public void registerIdlingResource(){
        mIdlingResource = RecipeFragment.getInstance(null).getIdlingResource();

        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void recyclerViewItem_OpensActivity() {
        onView(withId(R.id.rvRecipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.tvToolbarHeader)).check(matches(withText(ACTIVITY_TITLE)));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
