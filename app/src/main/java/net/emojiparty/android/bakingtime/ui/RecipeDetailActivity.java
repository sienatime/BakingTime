package net.emojiparty.android.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.Step;

public class RecipeDetailActivity extends AppCompatActivity {
  public static final String RECIPE_ID = "RECIPE_ID";
  private static final int RECIPE_NOT_FOUND = -1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setupViewModel();
    setContentView(R.layout.activity_recipe_detail);
    if (findViewById(R.id.fragment_container) != null) {
      RecipeFragment recipeFragment = new RecipeFragment();
      recipeFragment.setOnStepClicked(new OnStepClicked() {
        @Override public void onClick(Step step) {
          replaceFragment(new RecipeStepFragment());
        }
      });
      replaceFragment(recipeFragment);
    }
  }

  private void setupViewModel() {
    int recipeId = getIntent().getIntExtra(RECIPE_ID, RECIPE_NOT_FOUND);
    if (recipeId != RECIPE_NOT_FOUND) {
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
    void onClick(Step step);
  }
}
