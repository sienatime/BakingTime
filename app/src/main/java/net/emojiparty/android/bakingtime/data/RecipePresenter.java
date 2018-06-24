package net.emojiparty.android.bakingtime.data;

import android.support.annotation.Nullable;
import net.emojiparty.android.bakingtime.ui.RecipeDetailViewModel;
import net.emojiparty.android.bakingtime.ui.RecipesActivity;

public class RecipePresenter {
  private Recipe recipe;
  private RecipeDetailViewModel recipeDetailViewModel;
  @Nullable
  private RecipesActivity.OnRecipeClicked onRecipeClicked;

  public RecipePresenter(Recipe recipe, RecipeDetailViewModel recipeDetailViewModel,
      RecipesActivity.OnRecipeClicked onRecipeClicked) {
    this.recipe = recipe;
    this.recipeDetailViewModel = recipeDetailViewModel;
    this.onRecipeClicked = onRecipeClicked;
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public void onRecipeClicked() {
    recipeDetailViewModel.select(recipe);
    if (onRecipeClicked != null) {
      onRecipeClicked.onClick();
    }
  }
}
