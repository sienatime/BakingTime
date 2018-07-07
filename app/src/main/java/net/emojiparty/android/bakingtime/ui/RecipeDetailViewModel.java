package net.emojiparty.android.bakingtime.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import java.text.DecimalFormat;
import java.util.List;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.Ingredient;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.data.RecipeRepository;
import net.emojiparty.android.bakingtime.data.Step;

// https://developer.android.com/topic/libraries/architecture/viewmodel#sharing
public class RecipeDetailViewModel extends AndroidViewModel {
  private MutableLiveData<Recipe> recipe = new MutableLiveData<>();
  private MutableLiveData<Step> selectedStep = new MutableLiveData<>();
  private MutableLiveData<Step> nextStep = new MutableLiveData<>();
  private MutableLiveData<Step> previousStep = new MutableLiveData<>();
  private static final int STEP_NOT_FOUND = -1;
  private Resources resources;
  private String packageName;

  public RecipeDetailViewModel(@NonNull Application application, int recipeId) {
    super(application);
    this.resources = application.getResources();
    this.packageName = application.getPackageName();
    loadRecipeById(recipeId);
  }

  public MutableLiveData<Recipe> getRecipe() {
    return recipe;
  }

  public MutableLiveData<Step> getSelectedStep() {
    return selectedStep;
  }

  public MutableLiveData<Step> getNextStep() {
    return nextStep;
  }

  public MutableLiveData<Step> getPreviousStep() {
    return previousStep;
  }

  public void setSelectedStep(Step selectedStep) {
    this.selectedStep.setValue(selectedStep);
    List<Step> recipeSteps = getRecipe().getValue().getSteps();
    int index = findStepIndex(recipeSteps, selectedStep);
    if (index > STEP_NOT_FOUND) {
      setPreviousStep(recipeSteps, index);
      setNextStep(recipeSteps, index);
    }
  }

  private int findStepIndex(List<Step> recipeSteps, Step selectedStep) {
    for (int i = 0; i < recipeSteps.size(); i++) {
      Step step = recipeSteps.get(i);
      if (step.getId() == selectedStep.getId()) {
        return i;
      }
    }
    return STEP_NOT_FOUND;
  }

  private void setPreviousStep(List<Step> recipeSteps, int index) {
    if (index > 0) {
      previousStep.setValue(recipeSteps.get(index - 1));
    } else {
      previousStep.setValue(null);
    }
  }

  private void setNextStep(List<Step> recipeSteps, int index) {
    if (index < recipeSteps.size() - 1) {
      nextStep.setValue(recipeSteps.get(index + 1));
    } else {
      nextStep.setValue(null);
    }
  }

  private void loadRecipeById(int id) {
    Recipe recipe = RecipeRepository.getInstance().getRecipeById(id);
    this.recipe.setValue(recipe);
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
