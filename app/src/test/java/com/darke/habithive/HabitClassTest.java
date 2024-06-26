//package com.darke.habithive;
//import static com.google.common.truth.Truth.assertThat;
//
//import com.google.firebase.Timestamp;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//
//public class HabitClassTest {
//
//    @Test
//    public void testHabitClassConstructor() {
//        Timestamp timestamp = new Timestamp(new Date());
//        ArrayList<Integer> daysOfWeek = new ArrayList<>(Arrays.asList(1, 2, 3));
//        ArrayList<Integer> dayOfMonth = new ArrayList<>(Arrays.asList(10, 20));
//        ArrayList<String> reminders = new ArrayList<>(Arrays.asList("08:00 AM", "12:00 PM"));
//        ArrayList<String> goalResponse = new ArrayList<>(Arrays.asList("08:00 AM", "12:00 PM"));
//
//        HabitClass habit = new HabitClass("Exercise", "user123", timestamp, "Type 1", "Daily", daysOfWeek, dayOfMonth, reminders, "Checklist", goalResponse);
//
//        assertThat(habit.getHabitName()).isEqualTo("Exercise");
//        assertThat(habit.getUserId()).isEqualTo("user123");
//        assertThat(habit.getCreatedAt()).isEqualTo(timestamp);
//        assertThat(habit.getHabitType()).isEqualTo("Type 1");
//        assertThat(habit.getFrequency()).isEqualTo("Daily");
//        assertThat(habit.getDaysOfWeek()).isEqualTo(daysOfWeek);
//        assertThat(habit.getDayOfMonth()).isEqualTo(dayOfMonth);
//        assertThat(habit.getReminders()).isEqualTo(reminders);
//        assertThat(habit.getGoalType()).isNull();
//        assertThat(habit.getGoalResponse()).isNull();
//    }
//
//    @Test
//    public void testHabitClassSetters() {
//        HabitClass habit = new HabitClass();
//        Timestamp timestamp = new Timestamp(new Date());
//        ArrayList<Integer> daysOfWeek = new ArrayList<>(Arrays.asList(1, 2, 3));
//        ArrayList<Integer> dayOfMonth = new ArrayList<>(Arrays.asList(10, 20));
//        ArrayList<String> reminders = new ArrayList<>(Arrays.asList("08:00 AM", "12:00 PM"));
//        ArrayList<String> goalResponse = new ArrayList<>(Arrays.asList("08:00 AM", "12:00 PM"));
//
//        habit.setHabitName("Meditation");
//        habit.setUserId("user456");
//        habit.setCreatedAt(timestamp);
//        habit.setHabitType("Type 2");
//        habit.setFrequency("Weekly");
//        habit.setDaysOfWeek(daysOfWeek);
//        habit.setDayOfMonth(dayOfMonth);
//        habit.setReminders(reminders);
//        habit.setGoalType("Checklist");
//        habit.setGoalResponse(goalResponse);
//
//        assertThat(habit.getHabitName()).isEqualTo("Meditation");
//        assertThat(habit.getUserId()).isEqualTo("user456");
//        assertThat(habit.getCreatedAt()).isEqualTo(timestamp);
//        assertThat(habit.getHabitType()).isEqualTo("Type 2");
//        assertThat(habit.getFrequency()).isEqualTo("Weekly");
//        assertThat(habit.getDaysOfWeek()).isEqualTo(daysOfWeek);
//        assertThat(habit.getDayOfMonth()).isEqualTo(dayOfMonth);
//        assertThat(habit.getReminders()).isEqualTo(reminders);
//        assertThat(habit.getGoalType()).isEqualTo("Checklist");
//        assertThat(habit.getGoalResponse()).isEqualTo(goalResponse);
//    }
//}
