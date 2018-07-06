package net.emojiparty.android.bakingtime;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import java.util.ArrayList;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.data.RecipeRepository;
import net.emojiparty.android.bakingtime.ui.RecipeDetailActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static net.emojiparty.android.bakingtime.ui.RecipeDetailActivity.RECIPE_ID;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class) public class RecipeDetailActivityTest {
  private Recipe testRecipe = TestRecipeFactory.nutellaPie();

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

  @Test public void clickingRecipeStep_opensStep() {
    onView(withId(R.id.detail_recipe_name)).check(matches(withText("Nutella Pie")));
    onView(withId(R.id.steps_recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withId(R.id.step_short_description)).check(matches(withText("Recipe Introduction")));
    onView(withId(R.id.step_description)).check(matches(withText("Recipe Introduction")));
  }
}
