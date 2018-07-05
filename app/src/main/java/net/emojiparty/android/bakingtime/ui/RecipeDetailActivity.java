package net.emojiparty.android.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.data.RecipeRepository;

public class RecipeDetailActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);
    int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
    if (recipeId != -1) {
      Recipe recipe = RecipeRepository.getInstance().getRecipeById(recipeId);
      RecipeDetailViewModel detailViewModel =
          ViewModelProviders.of(this).get(RecipeDetailViewModel.class);
      detailViewModel.loadRecipeById(recipeId);
    }
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
}
