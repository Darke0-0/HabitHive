package com.darke.habithive;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public class CreationHabitFragment extends Fragment {

    public CreationHabitFragment() {
        // Required empty public constructor
    }
    private EditText habitNameInput;

    private final int[] iconResIds = {R.drawable.ic_finance, R.drawable.ic_health, R.drawable.ic_home,
            R.drawable.ic_meditation, R.drawable.ic_nutrition,R.drawable.ic_outdoor, R.drawable.ic_quit,
            R.drawable.ic_run, R.drawable.ic_social, R.drawable.ic_study, R.drawable.ic_work};
    private RadioGroup habitTypeRadioGroup;

    private RadioButton radioDaily, radioWeekly, radioMonthly;
    private LinearLayout dailyDynamicContentContainer, weeklyDynamicContentContainer, monthlyDynamicContentContainer;

    private CheckBox[] day_checkBoxes;

    private RecyclerView calendarRecyclerView;
    private CalendarAdapter CalendarAdapter;
    private ArrayList<String> dates;

    private LinearLayout reminderContainer;
    private Button addReminderButton;
    private List<String> reminders;

    private RadioButton radioCount, radioTimer, radioChecklist;
    private LinearLayout goalResponseContainer;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

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

        // Add this line at the beginning of your class
        habitNameInput = view.findViewById(R.id.input_habit_name);

        habitTypeRadioGroup = view.findViewById(R.id.radioGroupHabitType);
        addRadioButtonsWithIcons();
        habitTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Reset all radio buttons to default background
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    radioButton.setBackground(getResources().getDrawable(R.color.button_color, null));
                }

                // Set selected radio button background to highlighted
                RadioButton selectedRadioButton = group.findViewById(checkedId);
                selectedRadioButton.setBackground(getResources().getDrawable(R.drawable.radio_button_selected, null));
            }
        });

//      Daily weekly Monthly Button
        radioDaily = view.findViewById(R.id.radio_daily);
        radioWeekly = view.findViewById(R.id.radio_weekly);
        radioMonthly = view.findViewById(R.id.radio_monthly);

        dailyDynamicContentContainer = view.findViewById(R.id.daily_dynamic_content_container);
        weeklyDynamicContentContainer = view.findViewById(R.id.weekly_dynamic_content_container);
        monthlyDynamicContentContainer = view.findViewById(R.id.monthly_dynamic_content_container);

        View.OnClickListener radioDMCListener = v -> {
            clearRadioDMCButtons();
            ((RadioButton) v).setChecked(true);
            updateDynamicContent(v.getId());
        };

        radioDaily.setOnClickListener(radioDMCListener);
        radioWeekly.setOnClickListener(radioDMCListener);
        radioMonthly.setOnClickListener(radioDMCListener);

        // Initialize with daily selected
        radioDaily.setChecked(true);
        updateDynamicContent(radioDaily.getId());

//      Daily weekly Monthly Button

//      Day Selector Button

        day_checkBoxes = new CheckBox[]{
                view.findViewById(R.id.check_sunday),
                view.findViewById(R.id.check_monday),
                view.findViewById(R.id.check_tuesday),
                view.findViewById(R.id.check_wednesday),
                view.findViewById(R.id.check_thursday),
                view.findViewById(R.id.check_friday),
                view.findViewById(R.id.check_saturday)
        };

        for (CheckBox checkBox : day_checkBoxes) {
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

        CalendarAdapter = new CalendarAdapter(getContext(), dates);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        calendarRecyclerView.setAdapter(CalendarAdapter);

//      Calender View

//      Reminder View

        reminderContainer = view.findViewById(R.id.reminder_container);
        addReminderButton = view.findViewById(R.id.add_reminder_button);
        reminders = new ArrayList<>();

        addReminderButton.setOnClickListener(v -> showTimePickerDialog());

//      Reminder View

//       Goal View
        // Initialize views
        radioCount = view.findViewById(R.id.radio_count);
        radioTimer = view.findViewById(R.id.radio_timer);
        radioChecklist = view.findViewById(R.id.radio_checklist);
        goalResponseContainer = view.findViewById(R.id.goal_response_container);

        View.OnClickListener radioGoalListener = v -> {
            clearRadioGoalButtons();
            ((RadioButton) v).setChecked(true);
            showGoalInputDialog(v.getId());
        };

        radioCount.setOnClickListener(radioGoalListener);
        radioChecklist.setOnClickListener(radioGoalListener);
        radioTimer.setOnClickListener(radioGoalListener);

//       Goal View

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FloatingActionButton submitButton = view.findViewById(R.id.habit_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHabit();
            }
        });

        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addRadioButtonsWithIcons() {
        for (int iconResId : iconResIds) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setButtonDrawable(android.R.color.transparent); // Remove default radio button

            Drawable topDrawable = ContextCompat.getDrawable(getContext(), iconResId);
            if (topDrawable != null) {
                topDrawable.setBounds(0, 0, 120, 120);
                radioButton.setCompoundDrawables(null, topDrawable, null, null);
            }
            radioButton.setText(iconResId);
            radioButton.setBackground(getResources().getDrawable(R.color.button_color, null));

            // Add padding and gravity to center the drawable
            radioButton.setPadding(16, 48, 16, 0);
            radioButton.setGravity(android.view.Gravity.CENTER);

            habitTypeRadioGroup.addView(radioButton);
        }
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
        } else if (selectedRadioButtonId == R.id.radio_weekly) {
            weeklyDynamicContentContainer.setVisibility(View.VISIBLE);
        } else if (selectedRadioButtonId == R.id.radio_monthly) {
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute1) -> {
            String time = String.format("%02d:%02d", hourOfDay, minute1);
            addNewReminder(time);
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
        timeTextView.setTextColor(getResources().getColor(R.color.secondaryTextColor, null));
        timeTextView.setPadding(0, 0, 16, 0);

        // Add the TextView to the layout
        timeLayout.addView(timeTextView);

        // Add a remove button
        Button removeButton = new Button(getContext());
        removeButton.setText("Remove");
        removeButton.setTextColor(getResources().getColor(R.color.secondaryTextColor, null));
        removeButton.setBackgroundColor(getResources().getColor(R.color.colorUnchecked, null));

        // Set an onClickListener to remove the layout when the button is clicked
        removeButton.setOnClickListener(v -> {
            reminderContainer.removeView(timeLayout);
            reminders.remove(time);
        });

        // Add the remove button to the layout
        timeLayout.addView(removeButton);

        // Add the layout to the reminder container
        reminderContainer.addView(timeLayout);

    }

    private void clearRadioGoalButtons() {
        radioCount.setChecked(false);
        radioTimer.setChecked(false);
        radioChecklist.setChecked(false);
    }

    private void showGoalInputDialog(int goalType) {
        goalResponseContainer.removeAllViews();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView;

        if (goalType == R.id.radio_checklist) {
            dialogView = inflater.inflate(R.layout.dialog_goal_checklist, null);

            // Handle checklist item addition
            EditText inputChecklistItem = dialogView.findViewById(R.id.input_goal_checklist_item);
            Button addItemButton = dialogView.findViewById(R.id.add_checklist_item_button);
            LinearLayout checklistContainer = dialogView.findViewById(R.id.checklist_items_container);

            addItemButton.setOnClickListener(v -> {
                String itemText = inputChecklistItem.getText().toString().trim();
                if (!itemText.isEmpty()) {
                    addChecklistItem(itemText, checklistContainer);
                    inputChecklistItem.setText("");
                }
            });

            builder.setView(dialogView)
                    .setTitle("Checklist Goal")
                    .setPositiveButton("OK", (dialog, which) -> {
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
                    })
                    .setNegativeButton("Cancel", null);
        } else if (goalType == R.id.radio_count) {
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
        } else if (goalType == R.id.radio_timer) {
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
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        addGoalResponse("Timer Goal: " + timerType + " " + String.format("%02d:%02d", hour, minute) + " ");
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
        itemTextView.setTextColor(getResources().getColor(R.color.primaryTextColor, null));
        itemTextView.setPadding(0, 0, 16, 0);
        itemTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button removeButton = new Button(getContext());
        removeButton.setText("Remove");
        removeButton.setTextColor(getResources().getColor(R.color.primaryTextColor, null));
        removeButton.setBackgroundColor(getResources().getColor(R.color.colorUnchecked, null));
        removeButton.setOnClickListener(v -> checklistContainer.removeView(itemLayout));

        itemLayout.addView(itemTextView);
        itemLayout.addView(removeButton);
        checklistContainer.addView(itemLayout);
    }

    private void addGoalResponse(String responseText) {
        TextView responseTextView = new TextView(getContext());
        responseTextView.setText(responseText);
        responseTextView.setTextColor(getResources().getColor(R.color.secondaryTextColor, null));
        responseTextView.setPadding(8, 8, 8, 8);
        goalResponseContainer.addView(responseTextView);
    }

    private void submitHabit() {

        Map<String, Object> habitData = new HashMap<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        habitData.put("userId", user.getUid());
        habitData.put("createdAt", FieldValue.serverTimestamp());

//        Name
        String habitName = habitNameInput.getText().toString().trim();
        if (habitName.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a habit name", Toast.LENGTH_SHORT).show();
            return;
        }
        habitData.put("habitName", habitName);

//        Type
        if (habitTypeRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Please select a habit type", Toast.LENGTH_SHORT).show();
            return;
        }
        int selectedRadioButtonId = habitTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = habitTypeRadioGroup.findViewById(selectedRadioButtonId);
        String habitType = selectedRadioButton.getText().toString();
        habitData.put("habitType", habitType);

//        Frequency
        String frequency = "";
        if (radioDaily.isChecked()) frequency = "Daily";
        if (radioMonthly.isChecked()) frequency = "Monthly";

        habitData.put("frequency", frequency);

        if (frequency.equals("Daily")) {
            ArrayList<Integer> daysOfWeek = new ArrayList<>();
            for (CheckBox dayCheckBox : day_checkBoxes) {
                daysOfWeek.add(dayCheckBox.isChecked() ? 1 : 0);
            }
            if (daysOfWeek.stream().noneMatch(day -> day == 1)) {
                Toast.makeText(getContext(), "Please select at least one day of the week",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            habitData.put("daysOfWeek", daysOfWeek);
        }else if (frequency.equals("Monthly")) {
            // Add selected dates to habitData
            // You can add more complex logic to handle monthly habits if needed
            ArrayList<Integer> dayOfMonth = CalendarAdapter.getSelectedDay(); // Assuming you have a method to get the selected day
            if (dayOfMonth.isEmpty()) {
                Toast.makeText(getContext(), "Please select at least one day of the month",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            habitData.put("dayOfMonth", dayOfMonth);
        }

        // Reminder
        ArrayList<String> remindersList = new ArrayList<>();
        for (int i = 0; i < reminderContainer.getChildCount(); i++) {
            View childView = reminderContainer.getChildAt(i);
            TextView reminderTextView = (TextView) ((LinearLayout) childView).getChildAt(0);
            String reminderTime = reminderTextView.getText().toString().trim();
            if (!reminderTime.isEmpty()) {
                remindersList.add(reminderTime);
            }
        }
        habitData.put("reminders", remindersList);

//        Goal
        String goalType = "";
        for (int i = 0; i < goalResponseContainer.getChildCount(); i++) {
            View childView = goalResponseContainer.getChildAt(i);
            if (childView instanceof TextView) {
                String goalResponse = ((TextView) childView).getText().toString().trim();
                if (goalResponse.contains("Checklist Goal")) {
                    goalType = "Checklist";
                } else if (goalResponse.contains("Count Goal")) {
                    goalType = "Count";
                } else if (goalResponse.contains("Timer Goal")) {
                    goalType = "Timer";
                }
            }
        }
        if (goalType.isEmpty()) {
            Toast.makeText(getContext(), "Please add a goal response",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        habitData.put("goalType", goalType);

        // Collect goal responses
        if (goalResponseContainer.getChildCount() == 0) {
            Toast.makeText(getContext(), "Please add at least one goal response",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> goalResponseList = new ArrayList<>();
        for (int i = 0; i < goalResponseContainer.getChildCount(); i++) {
            View childView = goalResponseContainer.getChildAt(i);
            if (childView instanceof TextView) {
                String goalResponse = ((TextView) childView).getText().toString().trim();
                goalResponseList.add(goalResponse);
            }
        }
        habitData.put("goalResponse", goalResponseList);

        db.collection("users").document(user.getUid()).collection("habits")
                .add(habitData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Habit added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Habit.class);
                    intent.putExtra("HABIT_ID", documentReference.getId());
                    startActivity(intent);
                    getActivity().finish();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error saving habit", Toast.LENGTH_SHORT).show());
    }
}