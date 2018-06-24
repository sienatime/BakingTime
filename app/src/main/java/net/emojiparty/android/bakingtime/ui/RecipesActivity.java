package net.emojiparty.android.bakingtime.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import net.emojiparty.android.bakingtime.R;

public class RecipesActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipes);
    getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, new MasterFragment())
        .commit();
  }
}
