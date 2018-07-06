package net.emojiparty.android.bakingtime.ui;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

// https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
public class RecipeDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
  private Application application;
  private int recipeId;

  public RecipeDetailViewModelFactory(Application application, int recipeId) {
    this.application = application;
    this.recipeId = recipeId;
  }

  @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    return (T) new RecipeDetailViewModel(application, recipeId);

  }
}
