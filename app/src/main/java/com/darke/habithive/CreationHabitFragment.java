package com.darke.habithive;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreationHabitFragment extends Fragment {

    public CreationHabitFragment() {
        // Required empty public constructor
    }

    private RadioButton radioDaily, radioWeekly, radioMonthly;
    private LinearLayout dailyDynamicContentContainer, weeklyDynamicContentContainer, monthlyDynamicContentContainer;

    private CheckBox[] checkBoxes;

    private RecyclerView calendarRecyclerView;
    private ArrayList<String> dates;

    private LinearLayout reminderContainer;
    private Button addReminderButton;
    private List<String> reminders;

    private RadioButton radioCheckbox, radioCount, radioTimer, radioChecklist;
    private LinearLayout goalResponseContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override// Inflate the layout for this fragment
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_habit, container, false);

        String email = getActivity().getIntent().getStringExtra("email");
        String name = getActivity().getIntent().getStringExtra("name");
        Toast.makeText(getContext(), String.format("Email: %s, Name: %s", email, name), Toast.LENGTH_SHORT).show();

//      Daily weekly Monthly Button
        radioDaily = view.findViewById(R.id.radio_daily);
        radioWeekly = view.findViewById(R.id.radio_weekly);
        radioMonthly = view.findViewById(R.id.radio_monthly);

        dailyDynamicContentContainer = view.findViewById(R.id.daily_dynamic_content_container);
        weeklyDynamicContentContainer = view.findViewById(R.id.weekly_dynamic_content_container);
        monthlyDynamicContentContainer = view.findViewById(R.id.monthly_dynamic_content_container);

        View.OnClickListener radioDMCListener = new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                clearRadioDMCButtons();
                ((RadioButton) v).setChecked(true);
                updateDynamicContent(v.getId());
            }
        };

        radioDaily.setOnClickListener(radioDMCListener);
        radioWeekly.setOnClickListener(radioDMCListener);
        radioMonthly.setOnClickListener(radioDMCListener);

        // Initialize with daily selected
        radioDaily.setChecked(true);
        updateDynamicContent(radioDaily.getId());

//      Daily weekly Monthly Button

//      Day Selector Button

        checkBoxes = new CheckBox[]{
                view.findViewById(R.id.check_sunday),
                view.findViewById(R.id.check_monday),
                view.findViewById(R.id.check_tuesday),
                view.findViewById(R.id.check_wednesday),
                view.findViewById(R.id.check_thursday),
                view.findViewById(R.id.check_friday),
                view.findViewById(R.id.check_saturday)
        };

        for (CheckBox checkBox : checkBoxes) {
            setupCheckbox(checkBox);
        }

//      Calendar View

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);

        dates = new ArrayList<>();

        // Assuming Wednesday is the 3rd day of the week (0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday)
        int firstDayOfMonth = 3; // Wednesday

        // Add empty strings for days of the week before the first day of the month
        for (int i = 0; i < firstDayOfMonth; i++) {
            dates.add("");
        }

        // Add dates of the month (fixed to 31 days)
        for (int i = 1; i <= 31; i++) {
            dates.add(String.valueOf(i));
        }

        CalendarAdapter CalAdapter = new CalendarAdapter(getContext(), dates);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        calendarRecyclerView.setAdapter(CalAdapter);

//      Calender View

//      Reminder View

        reminderContainer = view.findViewById(R.id.reminder_container);
        addReminderButton = view.findViewById(R.id.add_reminder_button);
        reminders = new ArrayList<>();

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

//      Reminder View

//       Goal View
        // Initialize views
        radioCheckbox = view.findViewById(R.id.radio_checkbox);
        radioCount = view.findViewById(R.id.radio_count);
        radioTimer = view.findViewById(R.id.radio_timer);
        radioChecklist = view.findViewById(R.id.radio_checklist);
        goalResponseContainer = view.findViewById(R.id.goal_response_container);

        View.OnClickListener radioGoalListener = new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                clearRadioGoalButtons();
                ((RadioButton) v).setChecked(true);
                showGoalInputDialog(v.getId());
            }
        };

        radioCheckbox.setOnClickListener(radioGoalListener);
        radioCount.setOnClickListener(radioGoalListener);
        radioChecklist.setOnClickListener(radioGoalListener);
        radioTimer.setOnClickListener(radioGoalListener);

//       Goal View
        return view;
    }

    private void clearRadioDMCButtons() {
        radioDaily.setChecked(false);
        radioWeekly.setChecked(false);
        radioMonthly.setChecked(false);
    }

    private void updateDynamicContent(int selectedRadioButtonId) {
        // Hide all dynamic content containers
        dailyDynamicContentContainer.setVisibility(View.GONE);
        weeklyDynamicContentContainer.setVisibility(View.GONE);
        monthlyDynamicContentContainer.setVisibility(View.GONE);

        // Show the selected dynamic content container
        if (selectedRadioButtonId == R.id.radio_daily) {
            dailyDynamicContentContainer.setVisibility(View.VISIBLE);
        }

        else if (selectedRadioButtonId == R.id.radio_weekly) {
            weeklyDynamicContentContainer.setVisibility(View.VISIBLE);
        }

        else if (selectedRadioButtonId == R.id.radio_monthly) {
            monthlyDynamicContentContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setupCheckbox(CheckBox checkBox) {
        // Set the initial color of the checkbox
        updateCheckboxColor(checkBox);

        // Set listener to change color on check/uncheck
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> updateCheckboxColor(checkBox));
    }

    private void updateCheckboxColor(@NonNull CheckBox checkBox) {
        ColorStateList colorStateList = ContextCompat.getColorStateList(requireContext(), R.color.colorChecked);
        checkBox.setButtonTintList(colorStateList);
    }

    private void showTimePickerDialog() {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d:%02d", hourOfDay, minute);
                addNewReminder(time);
            }
        }, hour, minute, true); // true for 24-hour format

        timePickerDialog.show();
    }

    private void addNewReminder(String time) {
        // Add the reminder to the list
        reminders.add(time);

        // Create a new layout for the reminder time
        LinearLayout timeLayout = new LinearLayout(getContext());
        timeLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create a new TextView to display the time
        TextView timeTextView = new TextView(getContext());
        timeTextView.setText(time);
        timeTextView.setTextColor(getResources().getColor(R.color.secondaryTextColor));
        timeTextView.setPadding(0, 0, 16, 0);

        // Add the TextView to the layout
        timeLayout.addView(timeTextView);

        // Add a remove button
        Button removeButton = new Button(getContext());
        removeButton.setText("Remove");
        removeButton.setTextColor(getResources().getColor(R.color.secondaryTextColor));
        removeButton.setBackgroundColor(getResources().getColor(R.color.colorUnchecked));

        // Set an onClickListener to remove the layout when the button is clicked
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderContainer.removeView(timeLayout);
                reminders.remove(time);
            }
        });

        // Add the remove button to the layout
        timeLayout.addView(removeButton);

        // Add the layout to the reminder container
        reminderContainer.addView(timeLayout);
    }

    private void clearRadioGoalButtons() {
        radioCheckbox.setChecked(false);
        radioCount.setChecked(false);
        radioTimer.setChecked(false);
        radioChecklist.setChecked(false);
    }

    private void showGoalInputDialog(int goalType) {
        goalResponseContainer.removeAllViews();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView;

        if (goalType == R.id.radio_checkbox) {
            dialogView = inflater.inflate(R.layout.dialog_goal_checkbox, null);
            builder.setView(dialogView)
                    .setTitle("Checkbox Goal")
                    .setPositiveButton("OK", (dialog, which) -> {
                        EditText editTextDescription = dialogView.findViewById(R.id.goal_checkbox_description);
                        String description = editTextDescription.getText().toString().trim();
                        addGoalResponse("Checkbox Goal: " + description);
                    })
                    .setNegativeButton("Cancel", null);
        }

        else if (goalType == R.id.radio_count) {
            dialogView = inflater.inflate(R.layout.dialog_goal_count, null);
            builder.setView(dialogView)
                    .setTitle("Count Goal")
                    .setPositiveButton("OK", (dialog, which) -> {
                        Spinner spinnerCount = dialogView.findViewById(R.id.spinner_goal_count);
                        EditText editTextCountNumber = dialogView.findViewById(R.id.input_goal_count_number);
                        EditText editTextCountUnit = dialogView.findViewById(R.id.input_goal_count_unit);
                        String countType = spinnerCount.getSelectedItem().toString();
                        String countNumber = editTextCountNumber.getText().toString().trim();
                        String countUnit = editTextCountUnit.getText().toString().trim();
                        addGoalResponse("Count Goal: " + countType + " " + countNumber + " " + countUnit + " a day");
                    })
                    .setNegativeButton("Cancel", null);
        }

        else if (goalType == R.id.radio_timer) {
            dialogView = inflater.inflate(R.layout.dialog_goal_timer, null);
            TimePicker timePicker = dialogView.findViewById(R.id.input_goal_timer);
            timePicker.setIs24HourView(true);
            timePicker.setHour(0);
            timePicker.setMinute(0);
            builder.setView(dialogView)
                    .setTitle("Timer Goal")
                    .setPositiveButton("OK", (dialog, which) -> {
                        Spinner spinnerTimer = dialogView.findViewById(R.id.spinner_goal_timer);
                        String timerType = spinnerTimer.getSelectedItem().toString();
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();
                        addGoalResponse("Timer Goal: " + timerType + " " + String.format("%02d:%02d", hour, minute) + " ");
                    })
                    .setNegativeButton("Cancel", null);
        }

        else if (goalType == R.id.radio_checklist) {
            dialogView = inflater.inflate(R.layout.dialog_goal_checklist, null);

            // Handle checklist item addition
            EditText inputChecklistItem = dialogView.findViewById(R.id.input_goal_checklist_item);
            Button addItemButton = dialogView.findViewById(R.id.add_checklist_item_button);
            LinearLayout checklistContainer = dialogView.findViewById(R.id.checklist_items_container);

            addItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String itemText = inputChecklistItem.getText().toString().trim();
                    if (!itemText.isEmpty()) {
                        addChecklistItem(itemText, checklistContainer);
                        inputChecklistItem.setText("");
                    }
                }
            });

            builder.setView(dialogView)
                    .setTitle("Checklist Goal")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StringBuilder checklistItems = new StringBuilder("Checklist Goal:\n");
                            for (int i = 0; i < checklistContainer.getChildCount(); i++) {
                                View itemView = checklistContainer.getChildAt(i);
                                if (itemView instanceof LinearLayout) {
                                    LinearLayout itemLayout = (LinearLayout) itemView;
                                    TextView itemTextView = (TextView) itemLayout.getChildAt(0); // Assuming item text is the first child
                                    checklistItems.append("- ").append(itemTextView.getText().toString()).append("\n");
                                }
                            }
                            addGoalResponse(checklistItems.toString());
                        }
                    })
                    .setNegativeButton("Cancel", null);
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addChecklistItem(String itemText, @NonNull LinearLayout checklistContainer) {
        LinearLayout itemLayout = new LinearLayout(getContext());
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(8, 8, 8, 8);

        TextView itemTextView = new TextView(getContext());
        itemTextView.setText(itemText);
        itemTextView.setTextColor(getResources().getColor(R.color.primaryTextColor));
        itemTextView.setPadding(0, 0, 16, 0);
        itemTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button removeButton = new Button(getContext());
        removeButton.setText("Remove");
        removeButton.setTextColor(getResources().getColor(R.color.primaryTextColor));
        removeButton.setBackgroundColor(getResources().getColor(R.color.colorUnchecked));
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklistContainer.removeView(itemLayout);
            }
        });

        itemLayout.addView(itemTextView);
        itemLayout.addView(removeButton);
        checklistContainer.addView(itemLayout);
    }

    private void addGoalResponse(String responseText) {
        TextView responseTextView = new TextView(getContext());
        responseTextView.setText(responseText);
        responseTextView.setTextColor(getResources().getColor(R.color.secondaryTextColor));
        responseTextView.setPadding(8, 8, 8, 8);
        goalResponseContainer.addView(responseTextView);
    }

//    private void saveHabitToFirestore() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user == null) {
//            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String habitName = habitNameEditText.getText().toString().trim();
//        if (habitName.isEmpty()) {
//            Toast.makeText(getContext(), "Please enter a habit name", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String frequency = "";
//        if (radioDaily.isChecked()) frequency = "Daily";
//        if (radioWeekly.isChecked()) frequency = "Weekly";
//        if (radioMonthly.isChecked()) frequency = "Monthly";
//
//        Map<String, Object> habitData = new HashMap<>();
//        habitData.put("name", habitName);
//        habitData.put("frequency", frequency);
//        habitData.put("created_at", FieldValue.serverTimestamp());
//        habitData.put("updated_at", FieldValue.serverTimestamp());
//
//        if (frequency.equals("Daily")) {
//            Map<String, Boolean> daysOfWeek = new HashMap<>();
//            daysOfWeek.put("Sunday", checkSunday.isChecked());
//            daysOfWeek.put("Monday", checkMonday.isChecked());
//            daysOfWeek.put("Tuesday", checkTuesday.isChecked());
//            daysOfWeek.put("Wednesday", checkWednesday.isChecked());
//            daysOfWeek.put("Thursday", checkThursday.isChecked());
//            daysOfWeek.put("Friday", checkFriday.isChecked());
//            daysOfWeek.put("Saturday", checkSaturday.isChecked());
//            habitData.put("days_of_week", daysOfWeek);
//        }
//
//        // Add reminder times
//        // You can add more complex logic to handle reminders if needed
//        int childCount = reminderContainer.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View childView = reminderContainer.getChildAt(i);
//            // Assuming each child is an EditText for simplicity
//            EditText reminderEditText = (EditText) childView;
//            String reminderTime = reminderEditText.getText().toString().trim();
//            if (!reminderTime.isEmpty()) {
//                habitData.put("reminder_" + i, reminderTime);
//            }
//        }
//
//        db.collection("users").document(user.getUid()).collection("habits")
//                .add(habitData)
//                .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "Habit saved successfully", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error saving habit", Toast.LENGTH_SHORT).show());
//    }
}