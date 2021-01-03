package com.doublea.td2.authentification


import android.app.PendingIntent.getActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.doublea.td2.R
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginFragmentTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var activityRule: ActivityScenarioRule<AuthentificationActivity>
            = ActivityScenarioRule(AuthentificationActivity::class.java)

    private lateinit var email_correct: String
    private lateinit var email_incorrect: String
    private lateinit var blank: String
    private lateinit var password: String
    private lateinit var fullname: String

    @Before
    fun initValidString() {
        email_correct = "mimi.keene@gmail.com"
        email_incorrect = "mimi.ceene@incorrect.com"
        blank = ""
        password = "mimikeene"
        fullname = "mimi Keene"
    }

    @Test
    fun login_correct() {
        //simulation du clic sur le bouton "login" de la Main Activity
        launchFragmentInContainer<LoginFragment>()
        //onView(withId(R.id.go_login_button)).perform(click())
        // Remplissage des champs
        onView(withId(R.id.email_login))
                .perform(typeText(email_correct), closeSoftKeyboard())
        onView(withId(R.id.password_login))
                .perform(typeText(password), closeSoftKeyboard())
        //Clic sur le bouton de submit
        onView(withId(R.id.button_login)).perform(click())

        onView(withId(R.id.super_text))
            .check(matches(withText(fullname)))
    }

    @Test
    fun login_blank() {
        //simulation du clic sur le bouton "login" de la Main Activity
        launchFragmentInContainer<LoginFragment>()
        //onView(withId(R.id.go_login_button)).perform(click())
        // Remplissage des champs
        onView(withId(R.id.email_login))
            .perform(typeText(blank), closeSoftKeyboard())
        onView(withId(R.id.password_login))
            .perform(typeText(password), closeSoftKeyboard())
        //Clic sur le bouton de submit
        onView(withId(R.id.button_login)).perform(click())
        //onView(withText("Veuillez compléter les champs vides"))
                //.inRoot(withDecorView(not(`is`(getActivity().getWindow().getDecorView()))))
                //.check(matches(isDisplayed()))
    }
}