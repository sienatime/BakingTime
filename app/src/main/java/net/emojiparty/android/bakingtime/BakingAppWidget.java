package net.emojiparty.android.bakingtime;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import java.util.List;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.data.RecipeRepository;
import net.emojiparty.android.bakingtime.ui.IngredientsPresenter;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BakingAppWidgetConfigureActivity BakingAppWidgetConfigureActivity}
 */
public class BakingAppWidget extends AppWidgetProvider {

  static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
      final int appWidgetId) {
    final int recipeId = BakingAppWidgetConfigureActivity.loadRecipePref(context, appWidgetId);
    final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

    RecipeRepository.getInstance().getRecipes(null, new RecipeRepository.OnRecipesLoadedCallback() {
      @Override public void success(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
          if (recipeId == recipe.getId()) {
            views.setTextViewText(R.id.widget_recipe_name, recipe.getName());
            IngredientsPresenter ingredientsPresenter =
                new IngredientsPresenter(recipe, context.getResources(), context.getPackageName());
            views.setTextViewText(R.id.widget_recipe_ingredients,
                ingredientsPresenter.allIngredients());
            appWidgetManager.updateAppWidget(appWidgetId, views);
          }
        }
      }
    });
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override public void onDeleted(Context context, int[] appWidgetIds) {
    // When the user deletes the widget, delete the preference associated with it.
    for (int appWidgetId : appWidgetIds) {
      BakingAppWidgetConfigureActivity.deleteRecipePref(context, appWidgetId);
    }
  }

  @Override public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}

