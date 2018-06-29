package net.emojiparty.android.bakingtime.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import net.emojiparty.android.bakingtime.R;

public class RecipesActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipes);

    if (findViewById(R.id.fragment_container) != null) {
      RecipeListFragment recipeListFragment = new RecipeListFragment();
      recipeListFragment.setOnRecipeClicked(new OnRecipeClicked() {
        @Override public void onClick() {
          replaceFragment(new RecipeFragment());
        }
      });
      replaceFragment(recipeListFragment);
    }
  }

  private void replaceFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, fragment)
        .addToBackStack(fragment.getClass().getName())
        .commit();
  }

  public interface OnRecipeClicked {
    void onClick();
  }
}
