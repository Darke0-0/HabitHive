package com.darke.habithive;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private ArrayList<HabitClass> habitList;

    public HabitAdapter(List<HabitClass> habitList) {
        this.habitList = (ArrayList<HabitClass>) habitList;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_habit_item, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        HabitClass habit = habitList.get(position);


        // Set the habit name as the button text
        holder.nameButton.setText(habit.getName());

        // Set the habit type icon as the button drawable end
        int habitTypeIconResId = getHabitTypeIcon(habit.getHabitType()); // Replace this with your method to get the icon resource id based on the habit type
        Drawable habitTypeIcon = ContextCompat.getDrawable(holder.nameButton.getContext(), habitTypeIconResId);
        holder.nameButton.setCompoundDrawablesWithIntrinsicBounds(null, null, habitTypeIcon, null);

        holder.habitTypeTextView.setText(habit.getHabitType());
        holder.frequencyTextView.setText(habit.getFrequency());
        holder.createdAtTextView.setText(habit.getCreatedAt().toString());
        holder.daysOfWeekTextView.setText(habit.getDaysOfWeek().toString());
        holder.dayOfMonthTextView.setText(habit.getDayOfMonth().toString());
        holder.remindersTextView.setText(habit.getReminders().toString());
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        Button nameButton;
        TextView habitTypeTextView;
        TextView frequencyTextView;
        TextView createdAtTextView;
        TextView daysOfWeekTextView;
        TextView dayOfMonthTextView;
        TextView remindersTextView;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            nameButton = itemView.findViewById(R.id.name_button);
        }
    }

    private int getHabitTypeIcon(String habitType) {
        switch (habitType) {
            case "finance":
                return R.drawable.ic_finance;
            case "health":
                return R.drawable.ic_health;
            case "home":
                return R.drawable.ic_home;
            case "meditation":
                return R.drawable.ic_meditation;
            case "nutrition":
                return R.drawable.ic_nutrition;
            case "outdoor":
                return R.drawable.ic_outdoor;
            case "quit":
                return R.drawable.ic_quit;
            case "run":
                return R.drawable.ic_run;
            case "social":
                return R.drawable.ic_social;
            case "study":
                return R.drawable.ic_study;
            case "work":
                return R.drawable.ic_work;
            default:
                return R.drawable.habithivelogo;
        }
    }
}

