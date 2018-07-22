package net.emojiparty.android.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import java.util.List;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.models.Recipe;
import net.emojiparty.android.bakingtime.data.network.RecipeRepository;
import net.emojiparty.android.bakingtime.ui.recipe_detail.RecipeDetailActivity;

import static net.emojiparty.android.bakingtime.ui.recipe_detail.RecipeDetailActivity.RECIPE_ID;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BakingAppWidgetConfigureActivity BakingAppWidgetConfigureActivity}
 */
public class BakingAppWidget extends AppWidgetProvider {
  public static final String WIDGET_RECIPE_ID = "WIDGET_RECIPE_ID";

  static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
      final int appWidgetId) {
    final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
    final int recipeId = BakingAppWidgetConfigureActivity.loadRecipePref(context, appWidgetId);

    RecipeRepository.getInstance().getRecipes(null, new RecipeRepository.OnRecipesLoadedCallback() {
      @Override public void success(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
          if (recipeId == recipe.getId()) {
            views.setTextViewText(R.id.widget_recipe_name, recipe.getName());

            Intent remoteViewsIntent = new Intent(context, RecipeWidgetService.class);
            remoteViewsIntent.putExtra(WIDGET_RECIPE_ID, recipeId);
            views.setRemoteAdapter(R.id.widget_ingredients, remoteViewsIntent);

            Intent appIntent = new Intent(context, RecipeDetailActivity.class);
            appIntent.putExtra(RECIPE_ID, recipeId);

            PendingIntent
                appPendingIntent = PendingIntent.getActivity(context, appWidgetId, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.widget_recipe_name, appPendingIntent);

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

