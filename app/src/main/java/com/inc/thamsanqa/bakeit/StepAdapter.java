package com.inc.thamsanqa.bakeit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private List<Step> steps;
    StepListener listener;

    interface StepListener {
        void onStepClick(int position);
    }

    StepAdapter(List<Step> steps, StepListener listener) {
        this.steps = steps;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View stepView = inflater.inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(stepView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, final int position) {
        holder.bind(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStepClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    protected class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView((R.id.tv_step_des))
        TextView stepTitle;

        @BindView((R.id.tv_step))
        TextView stepNumber;

        private StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(int position) {
            Step step = steps.get(position);
            stepTitle.setText(step.getShortDescription());

            if (position > 0) {
                stepNumber.setText(String.format("Step %s", position));
            } else {
                stepNumber.setText(R.string.intro);
            }

        }
    }
}