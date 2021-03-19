package com.goggxi.covid19detector.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.utils.NestedScrollViewExtension
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

class MainActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testHomeFragment() {
        sleep(15000)
        Espresso.onView(allOf(ViewMatchers.withId(R.id.bottom_navigation_id), isDisplayed()))
        Espresso.onView(allOf(ViewMatchers.withId(R.id.relativeLayout), isDisplayed()))
        Espresso.onView(allOf(ViewMatchers.withId(R.id.btnSelengkapnya), isDisplayed()))
        Espresso.onView(allOf(ViewMatchers.withId(R.id.btnSelengkapnya))).perform(click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.chartUsia))).perform(NestedScrollViewExtension())
        sleep(2000)
        pressBack()
        sleep(10000)
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonWeek))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonMonth))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonMax))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonDirawat))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonWeek))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonMonth))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonMax))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonSembuh))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonWeek))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonMonth))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonMax))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonMeninggal))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonWeek))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonMonth))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.radioButtonMax))).perform(NestedScrollViewExtension(), click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.recyclerNews), isDisplayed()))
        Espresso.onView(withId(R.id.recyclerNews)).perform(
            NestedScrollViewExtension(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                object : ViewAction {
                    override fun getConstraints(): Matcher<View> {
                        TODO("Not yet implemented")
                    }

                    override fun getDescription(): String {
                        return "Click on specific button"
                    }

                    override fun perform(uiController: UiController?, view: View?) {
                        val button: View = view!!.findViewById(R.id.btnRead)
                        button.performClick()
                    }

                }
            )
        )
        sleep(5000)
    }

    @Test
    fun testInformationFragment() {
        sleep(8000)
        Espresso.onView(allOf(ViewMatchers.withId(R.id.informasiFragment))).perform(click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.bottom_navigation_id), isDisplayed()))
        Espresso.onView(allOf(ViewMatchers.withId(R.id.btn_mencegah), isDisplayed()))
        Espresso.onView(allOf(ViewMatchers.withId(R.id.btn_mengenal), isDisplayed()))
        Espresso.onView(allOf(ViewMatchers.withId(R.id.btn_mengobati), isDisplayed()))
        Espresso.onView(allOf(ViewMatchers.withId(R.id.btn_mengantisipasi), isDisplayed()))
        Espresso.onView(allOf(ViewMatchers.withId(R.id.btn_mengenal))).perform(click())
        pressBack()
        Espresso.onView(allOf(ViewMatchers.withId(R.id.btn_mencegah))).perform(click())
        pressBack()
        Espresso.onView(allOf(ViewMatchers.withId(R.id.btn_mengobati))).perform(click())
        pressBack()
        Espresso.onView(allOf(ViewMatchers.withId(R.id.btn_mengobati))).perform(click())
    }

    @Test
    fun testKlasifikasiFragment() {
        sleep(8000)
        Espresso.onView(allOf(ViewMatchers.withId(R.id.classificationFragment))).perform(click())
        Espresso.onView(allOf(ViewMatchers.withId(R.id.bottom_navigation_id), isDisplayed()))
    }

    @Test
    fun testRujukanFragment() {
        sleep(8000)
        Espresso.onView(allOf(ViewMatchers.withId(R.id.referralFragment))).perform(click())
        sleep(15000)
        Espresso.onView(allOf(ViewMatchers.withId(R.id.bottom_navigation_id), isDisplayed()))
        Espresso.onView(allOf(ViewMatchers.withId(R.id.recyclerReferral), isDisplayed()))
        Espresso.onView(withId(R.id.recyclerReferral)).perform(
            NestedScrollViewExtension(),
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                15,
                object : ViewAction {
                    override fun getConstraints(): Matcher<View> {
                        TODO("Not yet implemented")
                    }

                    override fun getDescription(): String {
                        return "Click on specific button"
                    }

                    override fun perform(uiController: UiController?, view: View?) {
                        val button: View = view!!.findViewById(R.id.btnCall)
                        button.performClick()
                    }

                }
            )
        )
    }

}