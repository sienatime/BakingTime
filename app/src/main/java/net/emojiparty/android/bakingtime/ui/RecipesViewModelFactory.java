package net.emojiparty.android.bakingtime.ui;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import net.emojiparty.android.bakingtime.SimpleIdlingResource;

// https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
public class RecipesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
  private Application application;
  @Nullable private SimpleIdlingResource idlingResource;

  public RecipesViewModelFactory(Application application, SimpleIdlingResource simpleIdlingResource) {
    this.application = application;
    this.idlingResource = simpleIdlingResource;
  }

  @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    return (T) new RecipesViewModel(application, idlingResource);

  }
}
