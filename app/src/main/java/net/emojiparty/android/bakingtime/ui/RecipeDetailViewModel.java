package net.emojiparty.android.bakingtime.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import net.emojiparty.android.bakingtime.data.Recipe;

// https://developer.android.com/topic/libraries/architecture/viewmodel#sharing
public class RecipeDetailViewModel extends AndroidViewModel {
  private MutableLiveData<Recipe> selected = new MutableLiveData<>();

  public RecipeDetailViewModel(@NonNull Application application) {
    super(application);
  }

  public MutableLiveData<Recipe> getSelected() {
    return selected;
  }

  public void select(Recipe selected) {
    this.selected.setValue(selected);
  }
}
