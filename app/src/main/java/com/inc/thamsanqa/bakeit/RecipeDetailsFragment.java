package com.inc.thamsanqa.bakeit;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsFragment extends Fragment implements StepAdapter.StepListener {

    public static String TAG = "RecipeDetailsFragment";
    public static String RECIPE_BUNDLE = "RecipeDetailsFragment.Recipe";
    private Recipe recipe;

    int spanCount = 1;

    @BindView(R.id.rv_steps)
    RecyclerView stepRecyclerView;

    @BindView(R.id.tv_ingredients)
    TextView mIngredients;

    private RecipesActivity activity;
    public RecipeDetailsFragmentListener listener;

    interface RecipeDetailsFragmentListener {
        void onStepSelected(int position);
    }

    public RecipeDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        if (bundle != null) {

            recipe = bundle.getParcelable(RECIPE_BUNDLE);

            activity = (RecipesActivity) getActivity();
            activity.setTitle(recipe.getName());
            activity.getSupportActionBar().show();

            showIngredients(recipe.getIngredients());
            showSteps(recipe.getSteps());
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RecipeDetailsFragmentListener) context;
    }

    private void showSteps(List<Step> steps) {
        stepRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        stepRecyclerView.setLayoutManager(layoutManager);
        StepAdapter adapter = new StepAdapter(steps, this);
        stepRecyclerView.setAdapter(adapter);
    }

    private void showIngredients(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            mIngredients.append(String.format("%s (%d%s)%n",
                    ingredient.getIngredient(),
                    ingredient.getQuantity(),
                    ingredient.getMeasure()));
        }
    }

    @Override
    public void onStepClick(int position) {
        listener.onStepSelected(position);
    }
}
