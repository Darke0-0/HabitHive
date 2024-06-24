package com.darke.habithive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_user_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        rememberMeCheckBox = view.findViewById(R.id.remember_me);

        loginBtn = view.findViewById(R.id.loginBtn);
        sharedPreferences = requireActivity().getSharedPreferences("HabitHivePrefs", Context.MODE_PRIVATE);

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
        return view;
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            // User is signed in, navigate to the next activity.
//            String userId = currentUser.getUid();
//            Log.e("check", "onStart: " + db.collection("users").document(userId).collection("info").toString());
//            db.collection("users").document(userId)
//                    .collection("info")
//                    .get()
//                    .addOnCompleteListener(infoTask -> {
//                        if (infoTask.isSuccessful()) {
//                            Intent intent = new Intent(getContext(), Home.class);
//                            startActivity(intent);
//                            requireActivity().finish();
//                        } else {
//                            Toast.makeText(getContext(), "Failed to fetch user info", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }

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