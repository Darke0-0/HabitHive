package com.darke.habithive;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class HomeHabitFragment extends Fragment {

    public HomeHabitFragment() {
        // Required empty public constructor
    }

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ArrayList<HabitClass> habitList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HabitAdapter habitAdapter;

    @Override// Inflate the layout for this fragment
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_habit, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        habitAdapter = new HabitAdapter(habitList);
        recyclerView.setAdapter(habitAdapter);

        loadHabits();

        return view;
    }

    private void loadHabits() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("users").document(userId)
                .collection("habits")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            habitList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HabitClass habit = document.toObject(HabitClass.class);
                                habitList.add(habit);
                            }
                            habitAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Error getting habits", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}