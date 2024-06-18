// CalendarAdapter.java
package com.darke.habithive;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final Context context;
    private final ArrayList<String> dates;

    public CalendarAdapter(Context context, ArrayList<String> dates) {
        this.context = context;
        this.dates = dates;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_day, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String date = dates.get(position);
        if (!date.isEmpty()) {
            holder.dayCheckBox.setText(date);
            holder.dayCheckBox.setChecked(false);
            holder.dayCheckBox.setEnabled(true);
        } else {
            holder.dayCheckBox.setText("");
            holder.dayCheckBox.setChecked(false);
            holder.dayCheckBox.setEnabled(false);
            holder.dayCheckBox.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox dayCheckBox;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayCheckBox = itemView.findViewById(R.id.dayCheckBox);
        }
    }
}
