package com.nuhkoca.udacitybakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nuhkoca.udacitybakingapp.view.ingredients.activity.IngredientsActivity;
import com.nuhkoca.udacitybakingapp.view.ingredients.fragment.IngredientsFragment;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by nuhkoca on 3/21/18.
 * Not working at the moment. Model returns null as I am running the app over the last fragment.
 */

@RunWith(AndroidJUnit4.class)
public class TextShowsRecyclerViewItemCountTest {

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<IngredientsActivity> mActivityTestRule = new ActivityTestRule<>(IngredientsActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = IngredientsFragment.getInstance().getIdlingResource();

        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void textShows_RecyclerViewItemCount() {
        String count = getText(withId(R.id.tvIngredientCount));

        onView(withId(R.id.rvIngredients)).check(new RecyclerViewItemCountAssertion(Integer.parseInt(count)));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

    private String getText(final Matcher<View> matcher) {
        final String[] stringHolder = {null};
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view;
                stringHolder[0] = tv.getText().toString().substring(0, 1);
            }
        });
        return stringHolder[0];
    }
}