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
      MasterFragment masterFragment = new MasterFragment();
      masterFragment.setOnRecipeClicked(new OnRecipeClicked() {
        @Override public void onClick() {
          replaceFragment(new DetailFragment());
        }
      });
      replaceFragment(masterFragment);
    }
  }

  private void replaceFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commit();
  }

  public interface OnRecipeClicked {
    void onClick();
  }
}
