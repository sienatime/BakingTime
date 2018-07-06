package net.emojiparty.android.bakingtime.data;

import android.support.annotation.VisibleForTesting;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import net.emojiparty.android.bakingtime.SimpleIdlingResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
  private static RecipeRepository instance;
  private List<Recipe> recipes = new ArrayList<>();

  private RecipeRepository() {
  }

  @VisibleForTesting
  public void setRecipes(List<Recipe> recipes) {
    this.recipes = recipes;
  }

  public static RecipeRepository getInstance() {
    if (instance == null) {
      instance = new RecipeRepository();
    }
    return instance;
  }

  // this acts as a simple cache (load recipes from network, then store in
  // the singleton's instance variable)
  public void getRecipes(final SimpleIdlingResource idlingResource,
      final OnRecipesLoadedCallback callback) {
    if (recipes.size() == 0) {
      loadRecipesFromNetwork(idlingResource, callback);
    } else {
      callback.success(recipes);
    }
  }

  private void loadRecipesFromNetwork(final SimpleIdlingResource idlingResource,
      final OnRecipesLoadedCallback callback) {
    setIdlingState(idlingResource, false);
    RecipeLoader recipeLoader = new RecipeLoader();
    recipeLoader.loadAllRecipes().enqueue(new Callback<List<Recipe>>() {
      @Override public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        if (response.isSuccessful()) {
          recipes.clear();
          recipes.addAll(response.body());
          callback.success(recipes);
        }
        setIdlingState(idlingResource, true);
      }

      @Override public void onFailure(Call<List<Recipe>> call, Throwable t) {
        Log.i("RecipesViewModel", t.toString());
        setIdlingState(idlingResource, true);
      }
    });
  }

  public Recipe getRecipeById(int id) {
    for (Recipe recipe : recipes) {
      if (recipe.getId() == id) {
        return recipe;
      }
    }
    return null;
  }

  private void setIdlingState(SimpleIdlingResource idlingResource, boolean state) {
    if (idlingResource != null) {
      idlingResource.setIdleState(state);
    }
  }

  public interface OnRecipesLoadedCallback {
    void success(List<Recipe> recipes);
  }
}
