package net.emojiparty.android.bakingtime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.SimpleIdlingResource;
import net.emojiparty.android.bakingtime.data.Recipe;

import static net.emojiparty.android.bakingtime.ui.RecipeDetailActivity.RECIPE_ID;

public class RecipesActivity extends AppCompatActivity {
  @Nullable public SimpleIdlingResource idlingResource;

  @NonNull @VisibleForTesting public SimpleIdlingResource getIdlingResource() {
    if (idlingResource == null) {
      idlingResource = new SimpleIdlingResource();
    }
    return idlingResource;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipes);

    RecipeListFragment recipeListFragment = new RecipeListFragment();
    recipeListFragment.setOnRecipeClicked(new OnRecipeClicked() {
      @Override public void onClick(Recipe recipe) {
        Intent intent = new Intent(RecipesActivity.this, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_ID, recipe.getId());
        RecipesActivity.this.startActivity(intent);
      }
    });
    recipeListFragment.setIdlingResource(getIdlingResource());
    replaceFragment(recipeListFragment);
  }

  private void replaceFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, fragment)
        .addToBackStack(fragment.getClass().getName())
        .commit();
  }

  public interface OnRecipeClicked {
    void onClick(Recipe recipe);
  }
}
