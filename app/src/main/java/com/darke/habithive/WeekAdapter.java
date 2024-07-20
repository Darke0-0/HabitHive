package com.darke.habithive;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {

    private List<LocalDate> weeksList;
    private LocalDate selectedDate;
    private OnDateSelectedListener dateSelectedListener;

    public WeekAdapter(List<LocalDate> weeksList, LocalDate selectedDate, OnDateSelectedListener listener) {
        this.weeksList = weeksList;
        this.selectedDate = selectedDate;
        this.dateSelectedListener = listener;
    }

    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_item, parent, false);
        return new WeekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        LocalDate weekStartDate = weeksList.get(position);
        holder.bind(weekStartDate);
    }

    @Override
    public int getItemCount() {
        return weeksList.size();
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
//        notifyDataSetChanged();
        notifyItemChanged(weeksList.indexOf(selectedDate));
    }


    public class WeekViewHolder extends RecyclerView.ViewHolder {

        private TextView[] dayTextViews;

        public WeekViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextViews = new TextView[]{
                    itemView.findViewById(R.id.day1_text_view),
                    itemView.findViewById(R.id.day2_text_view),
                    itemView.findViewById(R.id.day3_text_view),
                    itemView.findViewById(R.id.day4_text_view),
                    itemView.findViewById(R.id.day5_text_view),
                    itemView.findViewById(R.id.day6_text_view),
                    itemView.findViewById(R.id.day7_text_view)
            };
        }

        public void bind(LocalDate weekStartDate) {
            DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault());
            DateTimeFormatter dayOfMonthFormatter = DateTimeFormatter.ofPattern("dd", Locale.getDefault());
            LocalDate dateIterator = weekStartDate;
            for (int i = 0; i < 7; i++) {
                LocalDate currentDate = dateIterator;
                String dayOfWeek = currentDate.format(dayOfWeekFormatter);
                String dayOfMonth = currentDate.format(dayOfMonthFormatter);

                SpannableString spannableString = new SpannableString(dayOfWeek + "\n" + dayOfMonth);
                spannableString.setSpan(new RelativeSizeSpan(0.7f), 0, dayOfWeek.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new RelativeSizeSpan(1f), dayOfWeek.length() + 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                dayTextViews[i].setText(spannableString);

                // Highlight selected date
                if (currentDate.equals(selectedDate)) {
                    dayTextViews[i].setBackgroundResource(R.drawable.date_background);
                } else {
                    dayTextViews[i].setBackgroundResource(android.R.color.transparent);
                }

                dayTextViews[i].setOnClickListener(v -> {
                    selectedDate = currentDate;
                    dateSelectedListener.onDateSelected(currentDate);
                    notifyDataSetChanged();
                });

                dateIterator = dateIterator.plusDays(1);
            }
        }
    }

    public interface OnDateSelectedListener {
        void onDateSelected(LocalDate date);
    }
}