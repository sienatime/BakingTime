package net.emojiparty.android.bakingtime.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import java.text.DecimalFormat;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.Ingredient;
import net.emojiparty.android.bakingtime.data.Recipe;

// https://developer.android.com/topic/libraries/architecture/viewmodel#sharing
public class RecipeDetailViewModel extends AndroidViewModel {
  private MutableLiveData<Recipe> selected = new MutableLiveData<>();
  private Resources resources;
  private String packageName;

  public RecipeDetailViewModel(@NonNull Application application) {
    super(application);
    this.resources = application.getResources();
    this.packageName = application.getPackageName();
  }

  public MutableLiveData<Recipe> getSelected() {
    return selected;
  }

  public void select(Recipe selected) {
    this.selected.setValue(selected);
  }

  // seems weird to pass the livedata? but otherwise it doesn't update
  public String allIngredients(MutableLiveData<Recipe> recipe) {
    DecimalFormat df = new DecimalFormat("#.##");
    StringBuilder builder = new StringBuilder();

    Recipe anotherRecipe = recipe.getValue();
    if (anotherRecipe == null) {
      return "";
    } else {
      for (Ingredient ingredient : anotherRecipe.getIngredients()) {
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
