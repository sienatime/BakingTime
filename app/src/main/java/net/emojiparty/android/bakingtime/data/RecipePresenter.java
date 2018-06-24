package net.emojiparty.android.bakingtime.data;

import android.support.v4.app.FragmentManager;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.ui.DetailFragment;
import net.emojiparty.android.bakingtime.ui.RecipeDetailViewModel;


public class RecipePresenter {
  private Recipe recipe;
  private RecipeDetailViewModel recipeDetailViewModel;
  private FragmentManager fragmentManager;

  public RecipePresenter(Recipe recipe, RecipeDetailViewModel recipeDetailViewModel,
      FragmentManager fragmentManager) {
    this.recipe = recipe;
    this.recipeDetailViewModel = recipeDetailViewModel;
    this.fragmentManager = fragmentManager;
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public void onRecipeClicked() {
    recipeDetailViewModel.select(recipe);
    fragmentManager.beginTransaction()
        .replace(R.id.fragment_container, new DetailFragment())
        .commit();
  }
}
