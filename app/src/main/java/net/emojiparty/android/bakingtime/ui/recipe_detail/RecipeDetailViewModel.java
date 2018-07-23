package net.emojiparty.android.bakingtime.ui.recipe_detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import java.util.List;
import net.emojiparty.android.bakingtime.data.models.Recipe;
import net.emojiparty.android.bakingtime.data.models.Step;
import net.emojiparty.android.bakingtime.data.network.RecipeRepository;
import net.emojiparty.android.bakingtime.ui.IngredientsPresenter;

// https://developer.android.com/topic/libraries/architecture/viewmodel#sharing
public class RecipeDetailViewModel extends AndroidViewModel {
  private MutableLiveData<Recipe> recipe = new MutableLiveData<>();
  private MutableLiveData<Step> selectedStep = new MutableLiveData<>();
  private MutableLiveData<Step> nextStep = new MutableLiveData<>();
  private MutableLiveData<Step> previousStep = new MutableLiveData<>();
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

  public void setRecipe(Recipe recipe) {
    this.recipe.setValue(recipe);
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
    if (index > Step.STEP_NOT_FOUND) {
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
    return Step.STEP_NOT_FOUND;
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
    RecipeRepository.getInstance().getRecipeById(id, new RecipeRepository.OnRecipeLoadedCallback() {
      @Override public void success(Recipe recipe) {
        setRecipe(recipe);
      }
    });
  }

  // seems weird to pass the livedata? but otherwise it doesn't update
  public String allIngredients(MutableLiveData<Recipe> recipe) {
    return new IngredientsPresenter(recipe.getValue(), resources, packageName).allIngredients();
  }
}
