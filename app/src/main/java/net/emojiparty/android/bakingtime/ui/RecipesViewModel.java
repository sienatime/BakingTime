package net.emojiparty.android.bakingtime.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import java.util.List;
import net.emojiparty.android.bakingtime.SimpleIdlingResource;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.data.RecipeRepository;

public class RecipesViewModel extends AndroidViewModel {
  private MutableLiveData<List<Recipe>> list = new MutableLiveData<>();

  public RecipesViewModel(Application application, SimpleIdlingResource idlingResource) {
    super(application);
    RecipeRepository.getInstance()
        .getRecipes(idlingResource, new RecipeRepository.OnRecipesLoadedCallback() {
          @Override public void success(List<Recipe> recipes) {
            setList(recipes);
          }
        });
  }

  public void setList(List<Recipe> list) {
    this.list.setValue(list);
  }

  public LiveData<List<Recipe>> getList() {
    return list;
  }
}
