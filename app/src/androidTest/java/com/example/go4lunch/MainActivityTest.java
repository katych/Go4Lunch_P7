package com.example.go4lunch;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import com.example.go4lunch.views.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

public class MainActivityTest
{
    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void bottomNavigationView_clickListRestaurants_DisplayGoodFragment()
    {
        Espresso.onView(ViewMatchers.withId(R.id.listView)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView_list_restaurant)).check(matches(isDisplayed()));
    }

    @Test
    public void bottomNavigationView_clickListWorkmates_DisplayGoodFragment()
    {
        Espresso.onView(ViewMatchers.withId(R.id.workmates)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.list_workmates_recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void bottomNavigationView_clickMap_DisplayGoodFragment()
    {
        Espresso.onView(ViewMatchers.withId(R.id.mapView)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.map)).check(matches(isDisplayed()));
    }



}