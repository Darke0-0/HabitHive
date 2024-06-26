//package com.darke.habithive;
//
//import androidx.fragment.app.testing.FragmentScenario;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.FirebaseFirestore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//
//import static com.google.common.truth.Truth.assertThat;
//
//@RunWith(AndroidJUnit4.class)
//public class CreationHabitFragmentTest {
//
//    private FirebaseFirestore db;
//
//    @BeforeEach
//    public void setUp() {
//        db = FirebaseFirestore.getInstance();
//    }
//
//    @Test
//    public void testSubmitHabit() {
//        // Launch the fragment in isolation
//        FragmentScenario<CreationHabitFragment> scenario = FragmentScenario.launchInContainer(CreationHabitFragment.class);
//
//        scenario.onFragment(fragment -> {
//            // Mock data for habit
//            String habitName = "Exercise";
//            String userId = "user123";
//            Timestamp createdAt = new Timestamp(new Date());
//            String habitType = "Type 1";
//            String frequency = "Daily";
//            ArrayList<Integer> daysOfWeek = new ArrayList<>(Arrays.asList(1, 2, 3));
//            ArrayList<Integer> dayOfMonth = new ArrayList<>(Arrays.asList(10, 20));
//            ArrayList<String> reminders = new ArrayList<>(Arrays.asList("08:00 AM", "12:00 PM"));
//            String goalType = "Checklist";
//            ArrayList<String> goalResponse = new ArrayList<>(Arrays.asList("hashish", "asijoajiojioa"));
//
//            HabitClass habit = new HabitClass(habitName, userId, createdAt, habitType, frequency, daysOfWeek, dayOfMonth, reminders, goalType, goalResponse);
//
//            // Submit habit to Firestore
//            db.collection("users").document(userId).collection("habits").add(habit)
//                    .addOnSuccessListener(documentReference -> {
//                        // Verify habit was added
//                        documentReference.get().addOnSuccessListener(documentSnapshot -> {
//                            assertThat(documentSnapshot.exists()).isTrue();
//                            HabitClass fetchedHabit = documentSnapshot.toObject(HabitClass.class);
//                            assertThat(fetchedHabit).isNotNull();
//                            assertThat(fetchedHabit.getHabitName()).isEqualTo(habitName);
//                        });
//                    })
//                    .addOnFailureListener(e -> {
//                        throw new AssertionError("Failed to add habit", e);
//                    });
//        });
//    }
//}
