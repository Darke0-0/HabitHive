package com.darke.habithive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private ArrayList<Habit> habitList;

    public HabitAdapter(List<Habit> habitList) {
        this.habitList = (ArrayList<Habit>) habitList;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_habit_item, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitList.get(position);
        holder.nameTextView.setText(habit.getName());
        holder.descriptionTextView.setText(habit.getDescription());
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
        }
    }
}

