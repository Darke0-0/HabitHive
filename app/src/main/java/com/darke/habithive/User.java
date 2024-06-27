package com.darke.habithive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.viewpager2.widget.ViewPager2;

import java.util.Objects;

public class User extends AppCompatActivity {

    float alpha = 0;
    private static final String TAG = "GoogleSignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager2 viewPager = findViewById(R.id.view_pager);

        FloatingActionButton google = findViewById(R.id.fab_google);

        UserAdapter adapter = new UserAdapter(this);
        viewPager.setAdapter(adapter);

        google.setTranslationY(300);
        tabLayout.setTranslationY(300);

        google.setAlpha(alpha);
        tabLayout.setAlpha(alpha);

        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Login" : "Signup")
        ).attach();

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Specify web client ID token
                .requestEmail() // Request user's email address
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso); // Initialize GoogleSignInClient with the configured options

        // Set click listener on Google Sign-In button
        findViewById(R.id.fab_google).setOnClickListener(view -> signInWithGoogle());
    }

    // Method to start Google Sign-In flow
    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent(); // Get the Google Sign-In intent from GoogleSignInClient
        startActivityForResult(signInIntent, RC_SIGN_IN); // Start the intent with a request code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) { // Check if the request code matches the Google Sign-In request code
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data); // Get the Task containing the GoogleSignInAccount from the intent data
            handleGoogleSignInResult(task); // Handle the Google Sign-In result
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class); // Get the GoogleSignInAccount from the completed task

            // Get Google Sign-In credentials from the account's ID token
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

            // Authenticate with Firebase using the Google credentials
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign-in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                // Check if this is a new user or an existing user
                                if (Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                                    // New user: Handle new user actions (e.g., update UI, store user data)
                                    Toast.makeText(User.this, "Welcome! New User", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Existing user: Handle existing user actions
                                    Toast.makeText(User.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                                }

                                // Navigate to HomeActivity or your main activity
                                startActivity(new Intent(User.this, Home.class));
                                finish(); // Finish current activity to prevent returning to this screen
                            } else {
                                // If sign-in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());

                                // Check if the failure is due to account linking failure
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    // Account already exists with the same email, handle accordingly
                                    linkAccountsAndSignIn(account);
                                } else {
                                    // General authentication failure
                                    Toast.makeText(User.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } catch (ApiException e) {
            // Google Sign-In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e);
            Toast.makeText(User.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to link Google account credentials with existing Firebase account
    private void linkAccountsAndSignIn(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        // Link the Google credential with the current Firebase user
        Objects.requireNonNull(firebaseAuth.getCurrentUser()).linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Successfully linked Google credential to existing Firebase account
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            // Continue with signed-in user information
                            Toast.makeText(User.this, "Google account linked successfully.", Toast.LENGTH_SHORT).show();

                            // Navigate to HomeActivity or your main activity
                            startActivity(new Intent(User.this, Home.class));
                            finish(); // Finish current activity to prevent returning to this screen
                        } else {
                            // Failed to link Google credential with existing Firebase account
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(User.this, "Failed to link Google account.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}