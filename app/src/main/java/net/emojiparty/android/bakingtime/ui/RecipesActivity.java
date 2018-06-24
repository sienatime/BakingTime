package net.emojiparty.android.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import java.util.List;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.Recipe;

public class RecipesActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipes);
    RecipesViewModel viewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
    final DataBindingAdapter adapter = new DataBindingAdapter(R.layout.list_item_recipe);
    RecyclerView recipeRecyclerView = findViewById(R.id.recipe_recycler_view);
    recipeRecyclerView.setAdapter(adapter);

    viewModel.getList().observe(this, new Observer<List<Recipe>>() {
      @Override public void onChanged(@Nullable List<Recipe> recipes) {
        adapter.setItems(recipes);
      }
    });
  }
}
