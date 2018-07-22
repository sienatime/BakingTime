package net.emojiparty.android.bakingtime.ui;

import android.content.res.Resources;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.models.Ingredient;
import net.emojiparty.android.bakingtime.data.models.Recipe;

public class IngredientsPresenter {
  private Recipe recipe;
  private Resources resources;
  private String packageName;

  public IngredientsPresenter(Recipe recipe, Resources resources, String packageName) {
    this.recipe = recipe;
    this.resources = resources;
    this.packageName = packageName;
  }

  public List<String> formattedIngredients() {
    DecimalFormat df = new DecimalFormat("#.##");
    ArrayList<String> ingredients = new ArrayList<>();

    if (recipe == null) {
      return ingredients;
    } else {
      for (Ingredient ingredient : recipe.getIngredients()) {
        String quantity = df.format(ingredient.getQuantity());
        if (ingredient.getMeasure().equals(("UNIT"))) {
          ingredients.add(resources.getString(R.string.ingredient_format_no_measure, quantity,
              ingredient.getIngredient()));
        } else {
          String measure = pluralizeMeasurement(ingredient);
          ingredients.add(resources.getString(R.string.ingredient_format, quantity, measure,
              ingredient.getIngredient()));
        }
      }
      return ingredients;
    }
  }

  public String allIngredients() {
    StringBuilder builder = new StringBuilder();

    for (String formattedIngredient : formattedIngredients()) {
      builder.append(formattedIngredient);
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
