package com.example.go4lunch;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.go4lunch.views.activities.AuthenticationActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

        private AuthenticationActivity mActivity;

        @Rule
        public ActivityTestRule<AuthenticationActivity> mActivityRule =
                new ActivityTestRule<>(AuthenticationActivity.class);

        @Before
        public void setUp() {
            mActivity = mActivityRule.getActivity();
            assertThat(mActivity, notNullValue());
        }

}