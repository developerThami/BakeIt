package com.inc.thamsanqa.bakeit;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity implements RecipeFragment.RecipeFragmentListener, RecipeDetailsFragment.RecipeDetailsFragmentListener {

    private static final String CURRENT_TITLE = "Title";
    private Recipe recipe;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.tablet)
    public RelativeLayout masterDetailFlow;

    @Nullable
    @BindView(R.id.step_container)
    FrameLayout mStepFrame;

    @Nullable
    @BindView(R.id.step_detail_container)
    FrameLayout mStepDetailFrame;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(Recipe._RECIPE);
            title = savedInstanceState.getString(CURRENT_TITLE);
            setTitle(title);

        } else {
            showRecipes();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                navigateBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showRecipes() {

        getSupportActionBar().show();
        toolbar.setTitle(getString(R.string.main_title));

        if (masterDetailFlow != null) {
            controlStepFrameVisibility(View.GONE);
            showFragment(new RecipeFragment(),
                    RecipeFragment.TAG,
                    R.id.step_detail_container);
        } else {
            showFragment(new RecipeFragment(),
                    RecipeFragment.TAG,
                    R.id.container);
        }
    }


    private void showFragment(Fragment fragment, String tag, int container) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        this.recipe = recipe;
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelable(RecipeDetailsFragment.RECIPE_BUNDLE, recipe);
        fragment.setArguments(bundle);

        if (masterDetailFlow != null) {

            controlStepDetailFrameVisibility(View.INVISIBLE);
            controlStepFrameVisibility(View.VISIBLE);
            showFragment(fragment,
                    RecipeDetailsFragment.TAG,
                    R.id.step_container);

        } else {
            showFragment(fragment, RecipeDetailsFragment.TAG, R.id.container);
        }
    }

    @Override
    public void onStepSelected(int position) {

        StepDetailFragment fragment = new StepDetailFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(StepDetailFragment.STEP_POSITION, position);
        bundle.putParcelable(StepDetailFragment.RECIPE, recipe);
        fragment.setArguments(bundle);

        if (masterDetailFlow != null) {
            controlStepDetailFrameVisibility(View.VISIBLE);
            showFragment(fragment,
                    StepDetailFragment.TAG,
                    R.id.step_detail_container);
        } else {
            showFragment(fragment,
                    StepDetailFragment.TAG,
                    R.id.container);
        }
    }

    @Override
    public void onBackPressed() {
        navigateBack();
    }

    private void navigateBack() {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() == 1) {
            finish();
        }

        if (fm.getBackStackEntryCount() > 0) {

            if (masterDetailFlow != null) {
                controlStepFrameVisibility(View.GONE);
                controlStepDetailFrameVisibility(View.VISIBLE);
                fm.popBackStack(StepDetailFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                fm.popBackStack();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_TITLE, title);
        outState.putParcelable(Recipe._RECIPE, recipe);
    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
        this.title = title;
    }

    public void controlStepFrameVisibility(int visibility) {
        mStepFrame.setVisibility(visibility);
    }

    public void controlStepDetailFrameVisibility(int visibility) {
        mStepDetailFrame.setVisibility(visibility);
    }

}

