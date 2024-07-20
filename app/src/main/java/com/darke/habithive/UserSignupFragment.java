package com.darke.habithive;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserSignupFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    EditText name, email;
    EditText password, confirmPassword;
    TextInputLayout passwordBox, confirmPasswordBox;
    Button signupButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_user_signup, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        name = view.findViewById(R.id.fullName_user);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password_edit_text);
        passwordBox = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirm_password_edit_text);
        confirmPasswordBox = view.findViewById(R.id.confirm_password);
        signupButton = view.findViewById(R.id.signupBtn);

        name.setTranslationX(800);
        email.setTranslationX(800);
        password.setTranslationX(800);
        passwordBox.setTranslationX(800);
        confirmPassword.setTranslationX(800);
        confirmPasswordBox.setTranslationX(800);
        signupButton.setTranslationY(0);

        name.setAlpha(0);
        email.setAlpha(0);
        password.setAlpha(0);
        passwordBox.setAlpha(0);
        confirmPassword.setAlpha(0);
        confirmPasswordBox.setAlpha(0);
        signupButton.setAlpha(0);

        name.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        email.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        passwordBox.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        confirmPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        confirmPasswordBox.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        signupButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();

        signupButton.setOnClickListener(v -> {
            String Name = name.getText().toString().trim();
            String Email = email.getText().toString().trim();
            String Password = Objects.requireNonNull(password.getText()).toString().trim();
            String ConfirmPassword = Objects.requireNonNull(confirmPassword.getText()).toString().trim();

            if (Name.isEmpty() || Email.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
                Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Password.equals(ConfirmPassword)) {
                Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            checkIfEmailExists(Name, Email, Password);
        });

        return view;

    }

    private void checkIfEmailExists(String name, String email, String password) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SignInMethodQueryResult result = task.getResult();
                if (result != null && result.getSignInMethods() != null && !result.getSignInMethods().isEmpty()) {
                    // Email already exists
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Email is already in use", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    // Email does not exist, proceed with sign up
                    signUpUser(name, email, password);
                }
            } else {
                Log.e("EmailCheck", "Error checking email: ", task.getException());
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "Error checking email", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void showToastMessage(String message) {
        requireActivity().runOnUiThread(() -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    private void signUpUser(String name, String email, String password) {
        Executor executor = Executors.newSingleThreadExecutor();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(executor, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
//                            sendEmailVerification(user);
                            saveUserToFirestore(user, name);
                            String userId = user.getUid();
                            showToastMessage(userId);
                            db.collection("users").document(userId)
                                        .collection("info")
                                        .get()
                                        .addOnCompleteListener(infoTask -> {
                                            if (infoTask.isSuccessful()) {
                                                Intent intent = new Intent(getContext(), Dashboard.class);
                                                SharedPreferences.Editor editor = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                                                editor.putString("userId", userId);
                                                editor.apply();
                                                startActivity(intent);
                                                requireActivity().finish();
                                            } else {
                                                Toast.makeText(getContext(), "Failed to fetch user info", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                        }
                        else {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(getActivity(), "Failed to get user", Toast.LENGTH_SHORT).show()
                            );
                        }
                    } else {
                        // If sign up fails, display a message to the user.
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getActivity(), "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show()
                        );
                    }
                });
    }

//    private void sendEmailVerification(FirebaseUser user) {
//        user.sendEmailVerification()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(getActivity(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        // If there is an error, display it
//                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Failed to send verification email.";
//                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void saveUserToFirestore(FirebaseUser user, String name) {
        String userId = user.getUid();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("email", user.getEmail());

        db.collection("users").document(userId).set(userInfo)
                .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "User saved to Firestore", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to save user to Firestore", Toast.LENGTH_SHORT).show());
    }
}