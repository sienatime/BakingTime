package net.emojiparty.android.bakingtime.data;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
  private static RecipeRepository instance;
  private List<Recipe> recipes = new ArrayList<>();

  private RecipeRepository() {
  }

  public static RecipeRepository getInstance() {
    if (instance == null) {
      instance = new RecipeRepository();
    }
    return instance;
  }

  // TODO how to deal with the idling resource?? maybe just add it to the callback? sux
  public void getRecipes(final OnRecipesLoadedCallback callback) {
    if (recipes.size() == 0) {
      setIdlingState(false);
      RecipeLoader recipeLoader = new RecipeLoader();
      recipeLoader.loadAllRecipes().enqueue(new Callback<List<Recipe>>() {
        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
          if (response.isSuccessful()) {
            recipes.clear();
            recipes.addAll(response.body());
            callback.success(recipes);
          }
          setIdlingState(true);
        }

        @Override public void onFailure(Call<List<Recipe>> call, Throwable t) {
          Log.i("RecipesViewModel", t.toString());
          setIdlingState(true);
        }
      });
    } else {
      callback.success(recipes);
    }
  }

  public Recipe getRecipeById(int id) {
    for (Recipe recipe : recipes) {
      if (recipe.getId() == id) {
        return recipe;
      }
    }
    return null;
  }

  private void setIdlingState(boolean state) {
    //if (idlingResource != null) {
    //  idlingResource.setIdleState(state);
    //}
  }

  public interface OnRecipesLoadedCallback {
    void success(List<Recipe> recipes);
  }
}
