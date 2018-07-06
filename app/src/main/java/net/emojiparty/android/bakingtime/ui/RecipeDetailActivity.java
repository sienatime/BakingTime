package net.emojiparty.android.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import net.emojiparty.android.bakingtime.R;

public class RecipeDetailActivity extends AppCompatActivity {
  public static final String RECIPE_ID = "RECIPE_ID";
  private static final int RECIPE_NOT_FOUND = -1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupViewModel();
    setContentView(R.layout.activity_recipe_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  private void setupViewModel() {
    int recipeId = getIntent().getIntExtra(RECIPE_ID, RECIPE_NOT_FOUND);
    if (recipeId != RECIPE_NOT_FOUND) {
      ViewModelProviders.of(RecipeDetailActivity.this,
          new RecipeDetailViewModelFactory(getApplication(), recipeId))
          .get(RecipeDetailViewModel.class);
    }
  }
}
