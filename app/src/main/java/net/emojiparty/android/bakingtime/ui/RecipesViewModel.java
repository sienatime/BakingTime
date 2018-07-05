package net.emojiparty.android.bakingtime.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.List;
import net.emojiparty.android.bakingtime.SimpleIdlingResource;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.data.RecipeLoader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesViewModel extends AndroidViewModel {
  private MutableLiveData<List<Recipe>> list = new MutableLiveData<>();
  @Nullable private SimpleIdlingResource idlingResource;

  public RecipesViewModel(Application application, SimpleIdlingResource idlingResource) {
    super(application);
    this.idlingResource = idlingResource;
    loadRecipes();
  }

  private void loadRecipes() {
    setIdlingState(false);
    RecipeLoader recipeLoader = new RecipeLoader();
    recipeLoader.loadAllRecipes().enqueue(new Callback<List<Recipe>>() {
      @Override
      public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        if (response.isSuccessful()) {
          setList(response.body());
        }
        setIdlingState(true);
      }

      @Override public void onFailure(Call<List<Recipe>> call, Throwable t) {
        Log.i("RecipesViewModel", t.toString());
        setIdlingState(true);
      }
    });
  }

  private void setIdlingState(boolean state) {
    if (idlingResource != null) {
      idlingResource.setIdleState(state);
    }
  }

  public void setList(List<Recipe> list) {
    this.list.setValue(list);
  }

  public LiveData<List<Recipe>> getList() {
    return list;
  }
}
