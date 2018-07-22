package net.emojiparty.android.bakingtime;

import java.util.ArrayList;
import java.util.List;
import net.emojiparty.android.bakingtime.data.models.Ingredient;
import net.emojiparty.android.bakingtime.data.models.Recipe;
import net.emojiparty.android.bakingtime.data.models.Step;

public class TestRecipeFactory {
  public static Recipe nutellaPie() {
    Recipe recipe = new Recipe();
    recipe.setId(0);
    recipe.setImage("");
    recipe.setName("Nutella Pie");
    recipe.setServings(8);
    recipe.setIngredients(nutellaPieIngredients());
    recipe.setSteps(nutellaPieSteps());
    return recipe;
  }

  private static List<Ingredient> nutellaPieIngredients() {
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(new Ingredient(2, "CUP", "Graham Cracker crumbs"));
    ingredients.add(new Ingredient(6, "TBLSP", "unsalted butter, melted"));
    ingredients.add(new Ingredient(1, "K", "Nutella or other chocolate-hazelnut spread"));
    return ingredients;
  }

  private static List<Step> nutellaPieSteps() {
    ArrayList<Step> steps = new ArrayList<>();
    Step step0 = new Step();
    step0.setId(0);
    step0.setShortDescription("Recipe Introduction");
    step0.setDescription("Recipe Introduction");
    step0.setVideoURL("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
    step0.setThumbnailURL("");

    Step step1 = new Step();
    step1.setId(1);
    step1.setShortDescription("Starting prep");
    step1.setDescription("1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.");
    step1.setVideoURL("");
    step1.setThumbnailURL("");

    Step step5 = new Step();
    step5.setId(5);
    step5.setShortDescription("Finish filling prep");
    step5.setDescription("5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.");
    step5.setVideoURL("");
    step5.setThumbnailURL("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4");

    steps.add(step0);
    steps.add(step1);
    steps.add(step5);

    return steps;
  }
}
