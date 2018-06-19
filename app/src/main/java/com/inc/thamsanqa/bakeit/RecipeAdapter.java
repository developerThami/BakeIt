package com.inc.thamsanqa.bakeit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;
    private Context context;

    RecipeListener listener;

    interface RecipeListener {
        void onRecipeClick(Recipe recipe);
    }

    RecipeAdapter(List<Recipe> recipes, RecipeListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recipeView = inflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {
        holder.bind(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClick(recipes.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    protected class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView((R.id.tv_recipe_title))
        TextView mTitle;

        @BindView((R.id.tv_servings))
        TextView mServings;

        @BindView((R.id.iv_thumbnail))
        ImageView mThumbnail;



        private RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(int position) {
            Recipe recipe = recipes.get(position);
            mServings.setText(String.format("Servings: %d", recipe.getServings()));
            mTitle.setText(recipe.getName());

            if (recipe.getImage().isEmpty()) {

                Picasso.with(context)
                        .load(R.drawable.placeholder)
                        .into(mThumbnail);

            } else {

                Picasso.with(context)
                        .load(recipe.getImage())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(mThumbnail);
            }
        }

    }
}