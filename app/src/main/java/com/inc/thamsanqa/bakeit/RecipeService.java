package com.inc.thamsanqa.bakeit;

import android.app.IntentService;
import android.content.Intent;

public class RecipeService extends IntentService {

    public static final String RESULT = "recipe.RESULTS";
    public static final String RECIPE_RECEIVER_ACTION = "recipe.ACTION";

    public RecipeService() {
        super("RecipeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent == null)
            return;

        String results = NetworkUtil.getRecipes();

        Intent recipesIntent = new Intent(RECIPE_RECEIVER_ACTION);
        recipesIntent.putExtra(RESULT, results);
        getApplicationContext().sendBroadcast(recipesIntent);

    }

}
