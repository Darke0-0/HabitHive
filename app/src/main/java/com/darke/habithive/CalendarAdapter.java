// CalendarAdapter.java
package com.darke.habithive;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
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
    private final SparseBooleanArray itemStateArray= new SparseBooleanArray();

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
        holder.dayCheckBox.setText(date.isEmpty() ? "" : date);
        holder.dayCheckBox.setChecked(itemStateArray.get(position));
        if (date.isEmpty()) {
            holder.dayCheckBox.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CheckBox dayCheckBox;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayCheckBox = itemView.findViewById(R.id.dayCheckBox);
            dayCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getBindingAdapterPosition();
            if (!itemStateArray.get(adapterPosition, false)) {
                dayCheckBox.setChecked(true);
                itemStateArray.put(adapterPosition, true);
            } else {
                dayCheckBox.setChecked(false);
                itemStateArray.put(adapterPosition, false);
            }
        }
    }
    public ArrayList<Integer> getSelectedDay() {
        ArrayList<Integer> selectedDays = new ArrayList<>();
        for (int i = 0; i < itemStateArray.size(); i++) {
            if (itemStateArray.valueAt(i)) {
                selectedDays.add(i + 1); // Adding 1 because day of month starts from 1
            }
        }
        return selectedDays;
    }
}