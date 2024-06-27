package com.darke.habithive;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationTest {

    @Mock
    private FirebaseAuth mAuth;

    @Mock
    private FirebaseUser mockUser;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mAuth = mock(FirebaseAuth.class);
    }

    @Test
    public void testEmailPasswordSignupAndLogin() {

        when(mAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.isEmailVerified()).thenReturn(true);

        // Simulate sign-up process
        mAuth.createUserWithEmailAndPassword("testuser@example.com", "testpassword123");
        FirebaseUser user = mAuth.getCurrentUser();
        assertNotNull(user);

        // Simulate logout
        mAuth.signOut();

        // Simulate login process
        mAuth.signInWithEmailAndPassword("testuser@example.com", "testpassword123");
        user = mAuth.getCurrentUser();
        assertNotNull(user);
        assertTrue(user.isEmailVerified());
    }
}
