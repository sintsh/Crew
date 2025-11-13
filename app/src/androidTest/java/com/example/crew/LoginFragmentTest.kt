package com.example.crew
import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.crew.app.ui.fragments.login.LoginFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
class LoginFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.crew_navigation)
    }

    @Test
    fun loginFragment_isDisplayed_andViewsAreVisible() {
        launchFragmentInHiltContainer<LoginFragment> {
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }


        onView(ViewMatchers.withId(R.id.title)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.title)).check(matches(withText("Crew")))
    }

    @Test
    fun loginFragment_editTextFieldsTakeInput() {
        launchFragmentInHiltContainer<LoginFragment> {
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }


        onView(ViewMatchers.withId(R.id.username)).perform(typeText("username"))
        onView(ViewMatchers.withId(R.id.password)).perform(typeText("password"))


        onView(ViewMatchers.withId(R.id.username)).check(matches(withText("username")))
        onView(ViewMatchers.withId(R.id.password)).check(matches(withText("password")))

    }


    @Test
    fun loginFragment_necessaryErrorTextIsDisplayed_AndSignInButtonIsDisabled() {
        launchFragmentInHiltContainer<LoginFragment> {
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }


        onView(ViewMatchers.withId(R.id.username)).perform(typeText(""))

        onView(ViewMatchers.withId(R.id.password)).perform(typeText("pass"))
        onView(withId(R.id.username))
            .check(matches(hasErrorText("Not a valid username")))

        onView(ViewMatchers.withId(R.id.username)).perform(typeText("as"))


        onView(withId(R.id.password))
            .check(matches(hasErrorText("Password must be >5 characters")))


        onView(withId(R.id.login))
            .check(matches(not(isEnabled())))

    }



    @Test
    fun loginFragmentCorrectDataEntered_loginButtonIsEnabled() {
        launchFragmentInHiltContainer<LoginFragment> {
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }


        onView(withId(R.id.username)).perform(typeText("as"))

        onView(withId(R.id.password)).perform(typeText("password"))
        onView(withId(R.id.login))
            .check(matches(isEnabled()))

    }


    @Test
    fun loginFragment_EmployeeNotFoundMessageIsShown() {
        launchFragmentInHiltContainer<LoginFragment> {
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }


        onView(ViewMatchers.withId(R.id.username)).perform(typeText("as"))

        onView(ViewMatchers.withId(R.id.password)).perform(typeText("password"))
        onView(withId(R.id.login))
            .perform(click())

        onView(withText("Employee not found!"))
            .inRoot(withDecorView(not(`is`(Activity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()))



    }
}
