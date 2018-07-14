package net.emojiparty.android.bakingtime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import java.util.ArrayList;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.data.RecipeRepository;
import net.emojiparty.android.bakingtime.ui.RecipeDetailActivity;
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
import static net.emojiparty.android.bakingtime.ui.RecipeDetailActivity.RECIPE_ID;
import static org.hamcrest.CoreMatchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class) public class RecipeDetailActivityTest {
  private Recipe testRecipe = TestRecipeFactory.nutellaPie();

  // https://github.com/chiuki/espresso-samples/blob/master/rotate-screen/app/src/androidTest/java/com/sqisland/espresso/rotate_screen/MainActivityTest.java
  private void rotateScreen() {
    Context context = InstrumentationRegistry.getTargetContext();
    int orientation = context.getResources().getConfiguration().orientation;

    Activity activity = activityTestRule.getActivity();
    activity.setRequestedOrientation(
        (orientation == Configuration.ORIENTATION_PORTRAIT) ?
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  }

  @Rule public ActivityTestRule<RecipeDetailActivity> activityTestRule =
      new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {
        @Override protected void beforeActivityLaunched() {
          super.beforeActivityLaunched();
          ArrayList<Recipe> stubbedRecipes = new ArrayList<>();
          stubbedRecipes.add(testRecipe);
          RecipeRepository.getInstance().setRecipes(stubbedRecipes);
        }

        @Override
        protected Intent getActivityIntent() {
          Intent intent = new Intent();
          intent.putExtra(RECIPE_ID, testRecipe.getId());
          return intent;
        }
      };

  @Before public void assertAcitivtyLoadedWithRecipe() {
    onView(withId(R.id.detail_recipe_name)).check(matches(withText("Nutella Pie")));
  }

  @Test public void clickingRecipeStep_opensStep() {
    onView(withId(R.id.steps_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.step_short_description)).check(matches(withText("Recipe Introduction")));
    onView(withId(R.id.step_description)).check(matches(withText("Recipe Introduction")));
  }

  @Test public void firstStep_hasNextNotPrevious() {
    onView(withId(R.id.steps_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));

    onView(withId(R.id.step_short_description)).check(matches(withText("Recipe Introduction")));
    onView(withId(R.id.step_description)).check(matches(withText("Recipe Introduction")));

    onView(withId(R.id.next)).check(matches(isDisplayed()));
    onView(withId(R.id.previous)).check(matches(not(isDisplayed())));

    onView(withId(R.id.next)).perform(click());
    onView(withId(R.id.step_short_description)).check(matches(withText("Starting prep")));
  }

  @Test public void secondStep_hasNext() {
    onView(withId(R.id.steps_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(1, click()));

    onView(withId(R.id.step_short_description)).check(matches(withText("Starting prep")));
    onView(withId(R.id.next)).check(matches(isDisplayed()));
    onView(withId(R.id.next)).perform(click());

    onView(withId(R.id.step_short_description)).check(matches(withText("Finish filling prep")));
  }

  @Test public void secondStep_hasPrevious() {
    onView(withId(R.id.steps_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(1, click()));

    onView(withId(R.id.step_short_description)).check(matches(withText("Starting prep")));
    onView(withId(R.id.previous)).check(matches(isDisplayed()));
    onView(withId(R.id.previous)).perform(click());

    onView(withId(R.id.step_short_description)).check(matches(withText("Recipe Introduction")));
  }

  @Test public void lastStep_hasPreviousNotNext() {
    onView(withId(R.id.steps_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(2, click()));

    onView(withId(R.id.step_short_description)).check(matches(withText("Finish filling prep")));
    onView(withId(R.id.next)).check(matches(not(isDisplayed())));
    onView(withId(R.id.previous)).check(matches(isDisplayed()));

    onView(withId(R.id.previous)).perform(click());
    onView(withId(R.id.step_short_description)).check(matches(withText("Starting prep")));
  }

  // note this test does not pass on tablets, which is okay in theory
  // but it would be nice to test this behavior differently for phones and tablets...
  @Test public void stepBackButton_goesToRecipe() {
    onView(withId(R.id.steps_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.step_short_description)).check(matches(withText("Recipe Introduction")));

    onView(withContentDescription("Navigate up")).perform(click());

    onView(withId(R.id.steps_recycler_view)).check(matches(isDisplayed()));
  }

  @Test public void step_survivesRotation() {
    onView(withId(R.id.steps_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.step_short_description)).check(matches(withText("Recipe Introduction")));

    rotateScreen();
    onView(withId(R.id.step_short_description)).check(matches(withText("Recipe Introduction")));
    rotateScreen();
  }
}
