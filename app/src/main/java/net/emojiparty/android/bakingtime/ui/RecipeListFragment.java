package net.emojiparty.android.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.SimpleIdlingResource;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.data.RecipeMasterPresenter;

public class RecipeListFragment extends Fragment {
  private RecipeDetailViewModel detailViewModel;
  private RecipesActivity.OnRecipeClicked onRecipeClicked;

  @Nullable private SimpleIdlingResource idlingResource;

  public void setIdlingResource(@Nullable SimpleIdlingResource idlingResource) {
    this.idlingResource = idlingResource;
  }

  public void setOnRecipeClicked(RecipesActivity.OnRecipeClicked onRecipeClicked) {
    this.onRecipeClicked = onRecipeClicked;
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
    detailViewModel = ViewModelProviders.of(getActivity()).get(RecipeDetailViewModel.class);

    populateRecipes(view);

    return view;
  }

  private void populateRecipes(View view) {
    final DataBindingAdapter adapter = new DataBindingAdapter(R.layout.list_item_recipe);
    RecyclerView recipeRecyclerView = view.findViewById(R.id.recipe_recycler_view);
    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
    recipeRecyclerView.setLayoutManager(layoutManager);
    recipeRecyclerView.setAdapter(adapter);

    RecipesViewModel allRecipesViewModel = ViewModelProviders.of(this,
        new RecipesViewModelFactory(getActivity().getApplication(), idlingResource))
        .get(RecipesViewModel.class);
    allRecipesViewModel.getList().observe(this, new Observer<List<Recipe>>() {
      @Override public void onChanged(@Nullable List<Recipe> recipes) {
        adapter.setItems(mapRecipesToPresenters(recipes));
      }
    });
  }

  private List<RecipeMasterPresenter> mapRecipesToPresenters(List<Recipe> recipes) {
    List<RecipeMasterPresenter> presenters = new ArrayList<>();
    for (int i = 0; i < recipes.size(); i++) {
      Recipe recipe = recipes.get(i);
      presenters.add(new RecipeMasterPresenter(recipe, detailViewModel, onRecipeClicked));
    }
    return presenters;
  }
}
