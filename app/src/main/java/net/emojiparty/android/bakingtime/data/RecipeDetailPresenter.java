package net.emojiparty.android.bakingtime.data;

import android.content.res.Resources;
import java.text.DecimalFormat;
import net.emojiparty.android.bakingtime.R;

public class RecipeDetailPresenter {
  private Recipe recipe;
  private Resources resources;
  private String packageName;

  public RecipeDetailPresenter(Recipe recipe, Resources resources, String packageName) {
    this.recipe = recipe;
    this.resources = resources;
    this.packageName = packageName;
  }

  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public String allIngredients() {
    DecimalFormat df = new DecimalFormat("#.##");
    StringBuilder builder = new StringBuilder();
    for (Ingredient ingredient : recipe.getIngredients()) {
      String quantity = df.format(ingredient.getQuantity());
      if (ingredient.getMeasure().equals(("UNIT"))) {
        builder.append(resources.getString(R.string.ingredient_format_no_measure, quantity,
            ingredient.getIngredient()));
      } else {
        String measure = pluralizeMeasurement(ingredient);
        builder.append(resources.getString(R.string.ingredient_format, quantity, measure,
            ingredient.getIngredient()));
      }
      builder.append("\n");
    }
    return builder.toString();
  }

  private String pluralizeMeasurement(Ingredient ingredient) {
    int pluralMeasurementId =
        resources.getIdentifier(ingredient.getMeasure(), "plurals", packageName);
    int roundedQuantity = (int) Math.ceil(ingredient.getQuantity());
    if (pluralMeasurementId != 0) {
      return resources.getQuantityString(pluralMeasurementId, roundedQuantity);
    } else {
      return ingredient.getMeasure();
    }
  }
}
