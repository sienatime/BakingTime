package net.emojiparty.android.bakingtime.data;

import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RecipeLoader {
  private static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/";
  private RecipeService service;

  public RecipeLoader() {
    Retrofit retrofit = buildRetrofit();
    this.service = retrofit.create(RecipeService.class);
  }

  public Call<List<Recipe>> loadAllRecipes() {
    return service.allRecipes();
  }

  private Retrofit buildRetrofit() {
    return new Retrofit.Builder().baseUrl(RECIPE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  interface RecipeService {
    @GET("/topher/2017/May/59121517_baking/baking.json") Call<List<Recipe>> allRecipes();
  }
}
