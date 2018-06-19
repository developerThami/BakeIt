package com.inc.thamsanqa.bakeit;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeParser {

    private static List<Ingredient> getIngredients(JSONArray jsonIngredientArray) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int index = 0; index < jsonIngredientArray.length(); index++) {

            JSONObject ingredientJson = jsonIngredientArray.optJSONObject(index);
            Ingredient ingredient = new Ingredient();
            ingredient.setQuantity(ingredientJson.optInt(Ingredient._QUANTITY));
            ingredient.setIngredient(ingredientJson.optString(Ingredient._INGREDIENT));
            ingredient.setMeasure(ingredientJson.optString(Ingredient._MEASURE));
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    private static List<Step> getSteps(JSONArray jsonStepArray) {

        List<Step> steps = new ArrayList<>();
        for (int index = 0; index < jsonStepArray.length(); index++) {

            JSONObject stepJson = jsonStepArray.optJSONObject(index);
            Step step = new Step();

            step.setId(stepJson.optInt(Step._ID));
            step.setDescription(stepJson.optString(Step._DESCRIPTION));
            step.setShortDescription(stepJson.optString(Step._SHORT_DESCRIPTION));
            step.setVideoUrl(stepJson.optString(Step._VIDEO_URL));
            step.setThumbnailUrl(stepJson.optString(Step._THUMBNAIL_URL));
            steps.add(step);
        }
        return steps;
    }

    public static List<Recipe> getRecipes(String resultsStr) {
        List<Recipe> recipes = new ArrayList<>();
        try {

            JSONArray jsonRecipeArray = new JSONArray(resultsStr);

            for (int index = 0; index < jsonRecipeArray.length(); index++) {

                JSONObject recipeJson = jsonRecipeArray.optJSONObject(index);
                Recipe recipe = new Recipe();

                recipe.setId(recipeJson.optInt(Recipe._ID));
                recipe.setImage(recipeJson.optString(Recipe._IMAGE));
                recipe.setName(recipeJson.optString(Recipe._NAME));
                recipe.setServings(recipeJson.optInt(Recipe._SERVINGS));

                recipe.setIngredients(getIngredients(recipeJson.optJSONArray(Recipe._INGREDIENTS)));
                recipe.setSteps(getSteps(recipeJson.optJSONArray(Recipe._STEPS)));

                recipes.add(recipe);
            }

            return recipes;

        } catch (JSONException e) {
            e.printStackTrace();
            return recipes;
        }
    }
}