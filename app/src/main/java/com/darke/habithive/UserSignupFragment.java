package com.darke.habithive;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

public class UserSignupFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    EditText name, email;
    TextInputEditText password, confirmPassword;
    Button signupButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_user_signup, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        name = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirm_password);
        signupButton = view.findViewById(R.id.button);

        email.setTranslationX(800);
        password.setTranslationX(800);
        confirmPassword.setTranslationX(800);
        name.setTranslationX(800);
        signupButton.setTranslationY(0);

        email.setAlpha(0);
        password.setAlpha(0);
        confirmPassword.setAlpha(0);
        name.setAlpha(0);
        signupButton.setAlpha(0);

        name.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(200).start();
        email.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        confirmPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        signupButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();

        // Toggle password visibility
        password.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    togglePasswordVisibility(password, true);
                    return true;
                }
            }
            return false;
        });

        // Toggle confirm password visibility
        confirmPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (confirmPassword.getRight() - confirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    togglePasswordVisibility(confirmPassword, false);
                    return true;
                }
            }
            return false;
        });

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
    private void togglePasswordVisibility(TextInputEditText editText, boolean isPassword) {
        if (isPassword) {
            if (isPasswordVisible) {
                // Hide Password
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off_24px, 0);
            } else {
                // Show Password
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_24px, 0);
            }
            isPasswordVisible = !isPasswordVisible;
        } else {
            if (isConfirmPasswordVisible) {
                // Hide Password
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off_24px, 0);
            } else {
                // Show Password
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_24px, 0);
            }
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
        }
        // Move the cursor to the end of the text
        editText.setSelection(Objects.requireNonNull(editText.getText()).length());
    }

    private void checkIfEmailExists(String name, String email, String password) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SignInMethodQueryResult result = task.getResult();
                if (result != null && result.getSignInMethods() != null && !result.getSignInMethods().isEmpty()) {
                    // Email already exists
                    Toast.makeText(getActivity(), "Email is already in use", Toast.LENGTH_SHORT).show();
                } else {
                    // Email does not exist, proceed with sign up
                    signUpUser(name, email, password);
                }
            } else {
                Toast.makeText(getActivity(), "Error checking email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUpUser(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sendEmailVerification(user);
                            saveUserToFirestore(user, name);
                        }
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

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