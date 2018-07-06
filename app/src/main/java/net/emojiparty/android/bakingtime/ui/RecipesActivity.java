package net.emojiparty.android.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.SimpleIdlingResource;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.data.RecipeMasterPresenter;

import static net.emojiparty.android.bakingtime.ui.RecipeDetailActivity.RECIPE_ID;

public class RecipesActivity extends AppCompatActivity {
  @Nullable public SimpleIdlingResource idlingResource;

  @NonNull @VisibleForTesting public SimpleIdlingResource getIdlingResource() {
    if (idlingResource == null) {
      idlingResource = new SimpleIdlingResource();
    }
    return idlingResource;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipes);
    populateRecipes();
  }

  private void populateRecipes() {
    final DataBindingAdapter adapter = new DataBindingAdapter(R.layout.list_item_recipe);
    RecyclerView recipeRecyclerView = findViewById(R.id.recipe_recycler_view);
    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
    recipeRecyclerView.setLayoutManager(layoutManager);
    recipeRecyclerView.setAdapter(adapter);

    RecipesViewModel allRecipesViewModel = ViewModelProviders.of(this,
        new RecipesViewModelFactory(this.getApplication(), getIdlingResource()))
        .get(RecipesViewModel.class);
    allRecipesViewModel.getList().observe(this, new Observer<List<Recipe>>() {
      @Override public void onChanged(@Nullable List<Recipe> recipes) {
        adapter.setItems(mapRecipesToPresenters(recipes));
      }
    });
  }

  private List<RecipeMasterPresenter> mapRecipesToPresenters(List<Recipe> recipes) {
    List<RecipeMasterPresenter> presenters = new ArrayList<>();
    OnRecipeClicked clickForRecipe = new OnRecipeClicked() {
      @Override public void onClick(Recipe recipe) {
        Intent intent = new Intent(RecipesActivity.this, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_ID, recipe.getId());
        RecipesActivity.this.startActivity(intent);
      }
    };

    for (int i = 0; i < recipes.size(); i++) {
      Recipe recipe = recipes.get(i);
      presenters.add(new RecipeMasterPresenter(recipe, clickForRecipe));
    }
    return presenters;
  }

  public interface OnRecipeClicked {
    void onClick(Recipe recipe);
  }
}
