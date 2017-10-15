package com.chefless.ela.urbanweather;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.chefless.ela.urbanweather.citiesforecast.CitiesForecastAdapter;
import com.chefless.ela.urbanweather.citiesforecast.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CitiesForecastRecyclerViewTest {

    private static final int ITEM_BELOW_THE_FOLD = 5;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void scrollToItemBelowFold_checkItsText() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.forecasts_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD, click()));

        // Match the text in an item below the fold and check that it's displayed.
        String itemElementText = mActivityRule.getActivity().getResources().getString(R.string.Berlin);
        onView(withText(itemElementText)).check(matches(isDisplayed()));
    }

    @Test
    public void cityItemInList_hasSpecialText() {
        // First, scroll to the view holder using the isHolderForCityName matcher.
        String elementText = mActivityRule.getActivity().getResources().getString(R.string.Berlin);
        onView(ViewMatchers.withId(R.id.forecasts_rv)).perform(RecyclerViewActions.scrollToHolder(isHolderForCityName(elementText)));

        // Check that the item has the special text.
        onView(withText(elementText)).check(matches(isDisplayed()));
    }

    /**
     * Matches the {@link CitiesForecastAdapter.CitiesForecastAdapterViewHolder}s in the list.
     */
    private static Matcher<CitiesForecastAdapter.CitiesForecastAdapterViewHolder> isHolderForCityName(final String name) {
        return new TypeSafeMatcher<CitiesForecastAdapter.CitiesForecastAdapterViewHolder>() {
            @Override
            protected boolean matchesSafely(CitiesForecastAdapter.CitiesForecastAdapterViewHolder customHolder) {
                return customHolder.binding.getItem().getName().contentEquals(name);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("city item with name");
            }
        };
    }
}