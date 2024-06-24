package com.darke.habithive;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Before
    public void setUp() {
        mAuth = Mockito.mock(FirebaseAuth.class);
        db = FirebaseFirestore.getInstance();
    }

    @Test
    public void testUserSignIn() {
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mAuth.getCurrentUser()).thenReturn(mockUser);

        String userId = "user123";
        when(mockUser.getUid()).thenReturn(userId);

        // Assuming user is signed in, fetch user info from Firestore
        db.collection("users").document(userId).collection("info").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assertThat(document.exists()).isTrue();
                        String userEmail = document.getString("email");
                        assertThat(userEmail).isNotNull();
                    } else {
                        throw new AssertionError("Failed to fetch user info");
                    }
                });
    }
}
