package net.emojiparty.android.bakingtime.data;

import android.support.annotation.Nullable;
import net.emojiparty.android.bakingtime.ui.RecipesActivity;

public class RecipeMasterPresenter {
  private Recipe recipe;
  @Nullable private RecipesActivity.OnRecipeClicked onRecipeClicked;

  public RecipeMasterPresenter(Recipe recipe,
      RecipesActivity.OnRecipeClicked onRecipeClicked) {
    this.recipe = recipe;
    this.onRecipeClicked = onRecipeClicked;
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public void onRecipeClicked() {
    if (onRecipeClicked != null) {
      onRecipeClicked.onClick(recipe);
    }
  }
}
