package net.emojiparty.android.bakingtime.data.network;

import android.support.annotation.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import net.emojiparty.android.bakingtime.SimpleIdlingResource;
import net.emojiparty.android.bakingtime.data.models.Recipe;
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
        setIdlingState(idlingResource, true);
      }
    });
  }

  public void getRecipeById(final int id, final OnRecipeLoadedCallback callback) {
    getRecipes(null, new OnRecipesLoadedCallback() {
      @Override public void success(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
          if (recipe.getId() == id) {
            callback.success(recipe);
          }
        }
      }
    });
  }

  private void setIdlingState(SimpleIdlingResource idlingResource, boolean state) {
    if (idlingResource != null) {
      idlingResource.setIdleState(state);
    }
  }

  public interface OnRecipesLoadedCallback {
    void success(List<Recipe> recipes);
  }

  public interface OnRecipeLoadedCallback {
    void success(Recipe recipe);
  }
}
