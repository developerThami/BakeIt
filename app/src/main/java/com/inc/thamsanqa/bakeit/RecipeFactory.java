package com.inc.thamsanqa.bakeit;

import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.inc.thamsanqa.bakeit.IngredientsContract.IngredientEntry;

import java.util.ArrayList;
import java.util.List;


public class RecipeFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    List<Ingredient> ingredients;
    private Cursor ingredientCursor;

    public RecipeFactory(Context appContext) {

        this.ingredients = new ArrayList<>();
        context = appContext;
        initIngredientCursor();
    }

    private void initIngredientCursor() {
        ingredientCursor = context.getContentResolver().query(IngredientEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        getIngredientData();
    }

    private void getIngredientData() {

        if (ingredientCursor.moveToFirst()) {
            do {

                Ingredient ingredient = new Ingredient();
                ingredient.setIngredient(ingredientCursor.getString(
                        ingredientCursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT)));
                ingredient.setMeasure(ingredientCursor.getString(
                        ingredientCursor.getColumnIndex(IngredientEntry.COLUMN_MEASURE)));
                ingredient.setQuantity(ingredientCursor.getInt(
                        ingredientCursor.getColumnIndex(IngredientEntry.COLUMN_QUANTITY)));
                ingredients.add(ingredient);

            } while (ingredientCursor.moveToNext());
        }

        ingredientCursor.close();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Ingredient ingredient = ingredients.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_item_remote);
        views.setTextViewText(R.id.tv_ingredient, String.format("%s (%d%s)%n",
                ingredient.getIngredient(),
                ingredient.getQuantity(),
                ingredient.getMeasure()));

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
