package com.inc.thamsanqa.bakeit;


import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.inc.thamsanqa.bakeit.IngredientsContract.IngredientEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeListener {

    public static final String TAG = "RecipeFragment";

    @BindView(R.id.rv_recipes)
    RecyclerView recipeRecyclerView;

    @BindView(R.id.pb_loading)
    ProgressBar progressBar;

    int spanCount = 2;

    public RecipeFragmentListener listener;
    private RecipesActivity activity;

    interface RecipeFragmentListener {
        void onRecipeSelected(Recipe recipe);
    }

    public RecipeFragment() {
        // Required empty public constructor
    }

    private void startRecipeService() {
        getContext().registerReceiver(recipeReceiver, new IntentFilter(RecipeService.RECIPE_RECEIVER_ACTION));
        Intent intent = new Intent(getContext(), RecipeService.class);
        getContext().startService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);

        activity = (RecipesActivity) getActivity();
        activity.setTitle(getString(R.string.main_title));
        startRecipeService();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RecipeFragmentListener) context;
    }

    private BroadcastReceiver recipeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            String result = intent.getStringExtra(RecipeService.RESULT);
            List<Recipe> recipes = RecipeParser.getRecipes(result);
            showRecipes(recipes);
        }
    };

    public void showRecipes(List<Recipe> recipes) {
        adjustForOrientation();
        hideLoading();
        recipeRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recipeRecyclerView.setLayoutManager(layoutManager);
        RecipeAdapter adapter = new RecipeAdapter(recipes, this);
        recipeRecyclerView.setAdapter(adapter);
    }

    private void adjustForOrientation() {
        int currentOrientation = getContext().getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4;
        }
    }

    private void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        saveRecipeIngredients(recipe.getIngredients(), String.valueOf(recipe.getName()));
        listener.onRecipeSelected(recipe);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(recipeReceiver);
    }

    private void saveRecipeIngredients(List<Ingredient> ingredients, String recipeId) {

        getContext().getContentResolver()
                .delete(IngredientsContract.BASE_CONTENT_URI, null, null);

        for (Ingredient ingredient : ingredients) {

            ContentValues values = new ContentValues();
            values.put(IngredientEntry.COLUMN_RECIPE_ID, recipeId);
            values.put(IngredientEntry.COLUMN_INGREDIENT, ingredient.getIngredient());
            values.put(IngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());
            values.put(IngredientEntry.COLUMN_QUANTITY, ingredient.getQuantity());

            getContext().getContentResolver()
                    .insert(IngredientEntry.CONTENT_URI, values);
        }

        AppWidgetManager manager = AppWidgetManager.getInstance(getContext());
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(getContext(), RecipesWidget.class));
        manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_ingredients);

    }
}
