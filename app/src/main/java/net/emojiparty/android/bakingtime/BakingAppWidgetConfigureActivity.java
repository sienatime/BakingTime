package net.emojiparty.android.bakingtime;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.List;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.data.RecipeRepository;

import static net.emojiparty.android.bakingtime.data.Recipe.RECIPE_NOT_FOUND;

/**
 * The configuration screen for the {@link BakingAppWidget BakingAppWidget} AppWidget.
 */
public class BakingAppWidgetConfigureActivity extends Activity {

  private static final String PREFS_NAME = "net.emojiparty.android.bakingtime.BakingAppWidget";
  private static final String PREF_PREFIX_KEY = "appwidget_";
  int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

  private Spinner recipeSpinner;

  public BakingAppWidgetConfigureActivity() {
    super();
  }

  // Write the prefix to the SharedPreferences object for this widget
  static void saveRecipePref(Context context, int appWidgetId, int recipeId) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
    prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipeId);
    prefs.apply();
  }

  // Read the prefix from the SharedPreferences object for this widget.
  // If there is no preference saved, get the default from a resource
  static int loadRecipePref(Context context, int appWidgetId) {
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
    return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, RECIPE_NOT_FOUND);
  }

  static void deleteRecipePref(Context context, int appWidgetId) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
    prefs.remove(PREF_PREFIX_KEY + appWidgetId);
    prefs.apply();
  }

  @Override public void onCreate(Bundle icicle) {
    super.onCreate(icicle);

    // Set the result to CANCELED.  This will cause the widget host to cancel
    // out of the widget placement if the user presses the back button.
    setResult(RESULT_CANCELED);

    setContentView(R.layout.baking_app_widget_configure);
    recipeSpinner = findViewById(R.id.widget_recipe_spinner);
    findViewById(R.id.add_button).setOnClickListener(onAddWidgetButtonClicked);

    // Find the widget id from the intent.
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      mAppWidgetId =
          extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    // If this activity was started with an intent without an app widget ID, finish with an error.
    if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish();
      return;
    }

    // get recipes from repository and put in spinner
    final RecipeSpinnerAdapter spinnerAdapter =
        new RecipeSpinnerAdapter(this, android.R.layout.simple_spinner_item);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    recipeSpinner.setAdapter(spinnerAdapter);

    RecipeRepository.getInstance().getRecipes(null, new RecipeRepository.OnRecipesLoadedCallback() {
      @Override public void success(List<Recipe> recipes) {
        spinnerAdapter.addAll(recipes);
        setPreviousSelection(recipes);
      }
    });
  }

  private void setPreviousSelection(List<Recipe> recipes) {
    int previouslySavedId = loadRecipePref(BakingAppWidgetConfigureActivity.this, mAppWidgetId);
    if (previouslySavedId != RECIPE_NOT_FOUND) {
      for (int i = 0; i < recipes.size(); i++) {
        Recipe recipe = recipes.get(i);
        if (recipe.getId() == previouslySavedId) {
          recipeSpinner.setSelection(i);
          return;
        }
      }
    }
  }

  View.OnClickListener onAddWidgetButtonClicked = new View.OnClickListener() {
    public void onClick(View v) {
      final Context context = BakingAppWidgetConfigureActivity.this;

      Recipe recipe = (Recipe) recipeSpinner.getSelectedItem();
      saveRecipePref(context, mAppWidgetId, recipe.getId());

      // It is the responsibility of the configuration activity to update the app widget
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
      BakingAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

      // Make sure we pass back the original appWidgetId
      Intent resultValue = new Intent();
      resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
      setResult(RESULT_OK, resultValue);
      finish();
    }
  };

  class RecipeSpinnerAdapter extends ArrayAdapter<Recipe> {
    private int listItemResourceId;
    private LayoutInflater inflater;

    RecipeSpinnerAdapter(@NonNull Context context, int listItemResourceId) {
      super(context, listItemResourceId);
      this.listItemResourceId = listItemResourceId;
      inflater = LayoutInflater.from(context);
    }

    // http://www.zoftino.com/android-spinner-custom-adapter-&-layout
    @NonNull @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      return createRecipeView(position, parent);
    }

    @Override public View getDropDownView(int position, @Nullable View convertView,
        @NonNull ViewGroup parent) {
      return createRecipeView(position, parent);
    }

    private View createRecipeView(int position, @NonNull ViewGroup parent) {
      Recipe recipe = getItem(position);
      View view = inflater.inflate(listItemResourceId, parent, false);
      TextView textView = view.findViewById(android.R.id.text1);
      textView.setText(recipe.getName());
      return view;
    }
  }
}
