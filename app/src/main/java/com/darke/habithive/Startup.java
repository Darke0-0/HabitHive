package com.darke.habithive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Startup extends AppCompatActivity {

    private static final int LOADING_TIME = 3000;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Simulate loading of resources
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // After loading, start the login activity
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is signed in, navigate to the next activity.
                    String userId = currentUser.getUid();
                    db.collection("users").document(userId)
                            .collection("info")
                            .get()
                            .addOnCompleteListener(infoTask -> {
                                if (infoTask.isSuccessful()) {
                                    Intent intent = new Intent(Startup.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }else {
                    Intent intent = new Intent(Startup.this, User.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, LOADING_TIME);
    }
}