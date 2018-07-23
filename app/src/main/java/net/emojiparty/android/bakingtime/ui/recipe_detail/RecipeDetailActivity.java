package net.emojiparty.android.bakingtime.ui.recipe_detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.models.Recipe;
import net.emojiparty.android.bakingtime.data.models.Step;

public class RecipeDetailActivity extends AppCompatActivity {
  public static final String RECIPE_ID = "RECIPE_ID";
  public static final String STEP_ID = "STEP_ID";
  private RecipeDetailViewModel viewModel;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    setupViewModel(getRecipeId(savedInstanceState), getStepId(savedInstanceState));
    setContentView(R.layout.activity_recipe_detail);
    transactRecipeFragment();
  }

  private int getRecipeId(@Nullable Bundle savedInstanceState) {
    if (savedInstanceState != null
        && savedInstanceState.getInt(RECIPE_ID) > Recipe.RECIPE_NOT_FOUND) {
      return savedInstanceState.getInt(RECIPE_ID);
    } else {
      return getIntent().getIntExtra(RECIPE_ID, Recipe.RECIPE_NOT_FOUND);
    }
  }

  private int getStepId(@Nullable Bundle savedInstanceState) {
    if (savedInstanceState != null && savedInstanceState.getInt(STEP_ID) > Step.STEP_NOT_FOUND) {
      return savedInstanceState.getInt(STEP_ID);
    } else {
      return Step.STEP_NOT_FOUND;
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (viewModel.getRecipe().getValue() != null) {
      outState.putInt(RECIPE_ID, viewModel.getRecipe().getValue().getId());
      if (viewModel.getSelectedStep().getValue() != null) {
        outState.putInt(STEP_ID, viewModel.getSelectedStep().getValue().getId());
      }
    }
  }

  private void transactRecipeFragment() {
    if (findViewById(R.id.fragment_container) != null && !stepFragmentShowing()) {
      RecipeFragment recipeFragment = new RecipeFragment();
      recipeFragment.setOnStepClicked(new OnStepClicked() {
        @Override public void onClick() {
          replaceFragment(new RecipeStepFragment());
        }
      });
      replaceFragment(recipeFragment);
    }
  }

  private void setupViewModel(int recipeId, final int stepId) {
    if (recipeId != Recipe.RECIPE_NOT_FOUND) {
      viewModel = ViewModelProviders.of(RecipeDetailActivity.this,
          new RecipeDetailViewModelFactory(getApplication(), recipeId))
          .get(RecipeDetailViewModel.class);

      viewModel.getRecipe().observe(this, new Observer<Recipe>() {
        @Override public void onChanged(@Nullable Recipe recipe) {
          if (recipe != null && stepId > Step.STEP_NOT_FOUND) {
            for (Step step : recipe.getSteps()) {
              if (step.getId() == stepId) {
                viewModel.setSelectedStep(step);
                viewModel.getRecipe().removeObserver(this);
              }
            }
          }
        }
      });
    }
  }

  private void replaceFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, fragment)
        .addToBackStack(fragment.getClass().getName())
        .commit();
  }

  public interface OnStepClicked {
    void onClick();
  }

  @Override public boolean onSupportNavigateUp() {
    if (stepFragmentShowing()) {
      getSupportFragmentManager().popBackStack();
      return true;
    } else {
      return super.onSupportNavigateUp();
    }
  }

  private boolean stepFragmentShowing() {
    return getSupportFragmentManager().getBackStackEntryCount() == 2;
  }

  @Override public void onBackPressed() {
    if (stepFragmentShowing()) {
      getSupportFragmentManager().popBackStack();
    } else {
      this.finish();
    }
  }
}
