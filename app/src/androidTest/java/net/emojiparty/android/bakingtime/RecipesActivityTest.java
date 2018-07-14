package net.emojiparty.android.bakingtime;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import net.emojiparty.android.bakingtime.ui.RecipesActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class) public class RecipesActivityTest {
  @Rule public ActivityTestRule<RecipesActivity> activityTestRule =
      new ActivityTestRule<>(RecipesActivity.class);
  private IdlingResource idlingResource;

  @Before public void registerIdlingResource() {
    idlingResource = activityTestRule.getActivity().getIdlingResource();
    IdlingRegistry.getInstance().register(idlingResource);
  }

  @After public void unregisterIdlingResource() {
    IdlingRegistry.getInstance().unregister(idlingResource);
  }

  @Test public void openingApp_LoadsRecipes() {
    onView(withId(R.id.recipe_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));

    onView(withId(R.id.detail_recipe_name)).check(matches(withText("Nutella Pie")));
  }

  @Test public void backButton_goesAllTheWay() {
    onView(withId(R.id.recipe_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));

    onView(withId(R.id.detail_recipe_name)).check(matches(withText("Nutella Pie")));
    onView(withId(R.id.steps_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.step_short_description)).check(matches(withText("Recipe Introduction")));

    Espresso.pressBack();
    onView(withId(R.id.steps_recycler_view)).check(matches(isDisplayed()));

    Espresso.pressBack();
    onView(withId(R.id.recipe_recycler_view)).check(matches(isDisplayed()));
  }

  @Test public void navigateUp_goesAllTheWay() {
    onView(withId(R.id.recipe_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));

    onView(withId(R.id.detail_recipe_name)).check(matches(withText("Nutella Pie")));
    onView(withId(R.id.steps_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.step_short_description)).check(matches(withText("Recipe Introduction")));

    onView(withContentDescription("Navigate up")).perform(click());
    onView(withId(R.id.steps_recycler_view)).check(matches(isDisplayed()));

    onView(withContentDescription("Navigate up")).perform(click());
    onView(withId(R.id.recipe_recycler_view)).check(matches(isDisplayed()));
  }
}
