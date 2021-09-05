package com.example.taskmaster;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    public static final String username = "Aseel";
    public static final String taskName = "taskName";
    public static final String taskBody = "taskBody";
    public static final String taskState = "taskState";

    @Test
    public void testSettingButton() {
        onView(withId(R.id.settingsButton)).perform(click());
        onView(withId(R.id.usernameInput)).perform(typeText(username));
        onView(withId(R.id.saveButton)).perform(click());
        closeSoftKeyboard();
        onView(withId(R.id.myTask)).check(matches(withText( username + "’s tasks")));
    }

    @Test
    public void testAddTaskButton() {
        onView(withId(R.id.addTask)).perform(click());
        onView(withId(R.id.editTextTaskTitle)).perform(typeText(taskName));
        onView(withId(R.id.editTextTaskState)).perform(typeText(taskState));
        onView(withId(R.id.editTextDescription)).perform(typeText(taskBody));
        closeSoftKeyboard();
        onView(withId(R.id.buttonAddTask)).perform(click());
    }

    @Test
    public void testGoToDetails() {
        onView(withId(R.id.taskRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.viewId)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.homeButtonDetail)).perform(click());
    }
}