package net.emojiparty.android.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import java.util.ArrayList;
import java.util.List;
import net.emojiparty.android.bakingtime.data.models.Recipe;
import net.emojiparty.android.bakingtime.data.network.RecipeRepository;
import net.emojiparty.android.bakingtime.ui.IngredientsPresenter;

import static net.emojiparty.android.bakingtime.widget.BakingAppWidget.WIDGET_RECIPE_ID;
import static net.emojiparty.android.bakingtime.data.models.Recipe.RECIPE_NOT_FOUND;

public class RecipeWidgetService extends RemoteViewsService {
  @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
    int recipeId = intent.getIntExtra(WIDGET_RECIPE_ID, RECIPE_NOT_FOUND);
    return new RecipeViewsFactory(getApplicationContext(), recipeId);
  }
}

class RecipeViewsFactory implements RemoteViewsService.RemoteViewsFactory {
  private Context context;
  private int recipeId;
  private List<String> ingredients = new ArrayList<>();

  RecipeViewsFactory(Context context, int recipeId) {
    this.context = context;
    this.recipeId = recipeId;
  }

  @Override public void onCreate() {
  }

  @Override public void onDataSetChanged() {
    RecipeRepository.getInstance().getRecipeById(recipeId, new RecipeRepository.OnRecipeLoadedCallback() {
      @Override public void success(Recipe recipe) {
        if (recipe != null) {
          IngredientsPresenter ingredientsPresenter =
              new IngredientsPresenter(recipe, context.getResources(), context.getPackageName());
          ingredients = ingredientsPresenter.formattedIngredients();
        }
      }
    });
  }

  @Override public void onDestroy() {

  }

  @Override public int getCount() {
    return ingredients.size();
  }

  @Override public RemoteViews getViewAt(int i) {
    String formattedIngredient = ingredients.get(i);
    final RemoteViews views =
        new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
    views.setTextViewText(android.R.id.text1, formattedIngredient);
    return views;
  }

  @Override public RemoteViews getLoadingView() {
    return null;
  }

  @Override public int getViewTypeCount() {
    return 1;
  }

  @Override public long getItemId(int i) {
    return i;
  }

  @Override public boolean hasStableIds() {
    return false;
  }
}