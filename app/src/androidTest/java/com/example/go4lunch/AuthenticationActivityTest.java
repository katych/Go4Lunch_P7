package com.example.go4lunch;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import com.example.go4lunch.views.activities.AuthenticationActivity;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

public class AuthenticationActivityTest {

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<>(AuthenticationActivity.class);

    @Test
    public void AuthenticationActivity_GoogleButton_isDisplayed()
    {
        Espresso.onView(ViewMatchers.withId(R.id.googleLoginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void AuthenticationActivity_FacebookButton_isDisplayed()
    {
        Espresso.onView(ViewMatchers.withId(R.id.facebookLoginButton)).check(matches(isDisplayed()));
    }


    @Test
    public void AuthenticationActivity_EmailButton_isDisplayed()
    {
        Espresso.onView(ViewMatchers.withId(R.id.emailLoginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void AuthenticationActivity_TwitterButton_isDisplayed()
    {
        Espresso.onView(ViewMatchers.withId(R.id.twitterLoginButton
        )).check(matches(isDisplayed()));
    }
}
