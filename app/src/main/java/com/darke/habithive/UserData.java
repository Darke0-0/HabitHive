package com.darke.habithive;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserData {
    private static final String PREF_NAME = "UserPrefs";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void loadUserDataAndSaveToPrefs(Context context) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Example: Loading user profile
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserClass user = documentSnapshot.toObject(UserClass.class);

                        assert user != null;
                        // Save user data to SharedPreferences
                        saveUserData(context, user);

                        // Load user habits and save them
                        loadUserHabitsAndSave(context, userId);

                        // Optionally, you can also load and save other data like achievements, quests, etc.
                    } else {
                        Toast.makeText(context, "User does not exist", Toast.LENGTH_SHORT).show();
                        // Document does not exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    private static void loadUserHabitsAndSave(Context context, String userId) {
        List<HabitClass> habits = new ArrayList<>();
        db.collection("users").document(userId)
                .collection("habits")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot habitSnapshot : queryDocumentSnapshots) {
                        String habitId = habitSnapshot.getId();
                        HabitClass habit = habitSnapshot.toObject(HabitClass.class);
                        assert habit != null;
                        loadUserHabitsProgress(userId, habitId, progress -> {
                            habit.setProgress(progress);
                            habits.add(habit);
                            if (habits.size() == queryDocumentSnapshots.size()) {
                                // All habits have been loaded
                                saveUserHabits(context, habits);
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    private static void saveUserHabits(Context context, List<HabitClass> habits) {
        // Retrieve existing user data from SharedPreferences
        UserClass user = getUserData(context);
        if (user != null) {
            user.setHabits(habits);
            saveUserData(context, user);
        }
    }

    private interface ProgressLoadCallback {
        void onProgressLoaded(Map<String,Map<String, Object>> progress);
    }

    // Method to load user habits progress
    private static void loadUserHabitsProgress(String userId, String habitId, ProgressLoadCallback callback) {
        Map<String, Map<String, Object>> progress = new HashMap<>();

        db.collection("users").document(userId)
                .collection("habits").document(habitId)
                .collection("progress")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot progressSnapshot : queryDocumentSnapshots) {
                        Map<String, Object> progressData = progressSnapshot.getData();
                        progress.put(progressSnapshot.getId(), progressData);
                        Log.d("Progress", "Progress: " + progress);
                    } // Call the callback with the retrieved data
                    callback.onProgressLoaded(progress);
                })
                .addOnFailureListener(e -> {
                    Log.e("Progress", "Error loading progress", e);
                });
    }

    public static void saveUserData(Context context, UserClass user) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString("user", userJson);
        editor.apply();
    }

    public static UserClass getUserData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = prefs.getString("user", null);
        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, UserClass.class);
        }
        return null;
    }
}
