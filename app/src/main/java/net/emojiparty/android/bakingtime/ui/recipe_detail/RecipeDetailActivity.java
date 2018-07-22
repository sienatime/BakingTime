package net.emojiparty.android.bakingtime.ui.recipe_detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.models.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {
  public static final String RECIPE_ID = "RECIPE_ID";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setupViewModel();
    setContentView(R.layout.activity_recipe_detail);
    transactRecipeFragment();
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

  private void setupViewModel() {
    int recipeId = getIntent().getIntExtra(RECIPE_ID, Recipe.RECIPE_NOT_FOUND);
    if (recipeId != Recipe.RECIPE_NOT_FOUND) {
      ViewModelProviders.of(RecipeDetailActivity.this,
          new RecipeDetailViewModelFactory(getApplication(), recipeId))
          .get(RecipeDetailViewModel.class);
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
