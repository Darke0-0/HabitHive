package com.darke.habithive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserLoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    EditText email;
    EditText password;
    Button loginBtn;
    CheckBox rememberMeCheckBox;
    Button forgetPassword;
    TextInputLayout passwordBox;
    private SharedPreferences sharedPreferences;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_user_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password_edit_text);
        passwordBox = view.findViewById(R.id.password);
        rememberMeCheckBox = view.findViewById(R.id.remember_me);
        forgetPassword = view.findViewById(R.id.forgot_password);
        loginBtn = view.findViewById(R.id.loginBtn);
        sharedPreferences = requireActivity().getSharedPreferences("HabitHivePrefs", Context.MODE_PRIVATE);

        email.setTranslationX(800);
        password.setTranslationX(800);
        passwordBox.setTranslationX(800);
        rememberMeCheckBox.setTranslationX(800);
        forgetPassword.setTranslationX(800);
        loginBtn.setTranslationY(0);

        email.setAlpha(0);
        password.setAlpha(0);
        passwordBox.setAlpha(0);
        rememberMeCheckBox.setAlpha(0);
        forgetPassword.setAlpha(0);
        loginBtn.setAlpha(0);

        email.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        passwordBox.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        rememberMeCheckBox.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        forgetPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        loginBtn.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();

        // Load saved email if "Remember Me" was checked
        loadLogin();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().isEmpty()) {
                    email.setError("Email is required");

                } else if (password.getText().toString().trim().isEmpty()) {
                    password.setError("Password is required");
                }
                else{
                    onLogin();
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().isEmpty()) {
                    email.setError("Email is required");
                }
                else{
                    onForgotPassword();
                }
            }
        });
        return view;
    }

    public void onLogin(){
        final String email = this.email.getText().toString();
        final String password = this.password.getText().toString();
        boolean rememberMe = rememberMeCheckBox.isChecked();

        assert getActivity() != null;
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (rememberMe) {
                                saveLogin(email, password);
                            } else {
                                clearLogin();
                            }
                            if (currentUser != null) {
                                // User is signed in, navigate to the next activity.
                                String userId = currentUser.getUid();
                                db.collection("users").document(userId)
                                        .collection("info")
                                        .get()
                                        .addOnCompleteListener(infoTask -> {
                                            if (infoTask.isSuccessful()) {
                                                Intent intent = new Intent(getContext(), Home.class);
                                                startActivity(intent);
                                                requireActivity().finish();
                                            } else {
                                                Toast.makeText(getContext(), "Failed to fetch user info", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onForgotPassword() {
        String email = this.email.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Enter your email to reset password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Failed to send reset email.";
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("ForgotPassword", "Failed to send reset email", task.getException());
                    }
                });
    }

//  Remember Me
    private void loadLogin() {
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");
        if (!TextUtils.isEmpty(savedEmail)) {
            email.setText(savedEmail);
            password.setText(savedPassword);
            rememberMeCheckBox.setChecked(true);
        }
    }

    private void saveLogin(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    private void clearLogin() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.apply();
    }
    //  Remember Me
}