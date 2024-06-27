//package com.darke.habithive;
//
//import androidx.test.espresso.intent.Intents;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.replaceText;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//@RunWith(AndroidJUnit4.class)
//public class AuthenticationTest {
//
//    @Rule
//    public ActivityScenarioRule<User> activityRule = new ActivityScenarioRule<>(User.class);
//
//    private FirebaseAuth mAuth;
//
//    @Before
//    public void setUp() {
//        Intents.init();
//        mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
//    }
//
//    @After
//    public void tearDown() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            user.delete();
//        }
//        Intents.release();
//    }
//
//    @Test
//    public void testEmailPasswordSignupAndLogin() {
//        // Mock a click on the signup tab in TabLayout
//        onView(withText("Signup")).perform(click());
//
//        // Enter user details in the signup form
//        onView(withId(R.id.fullName)).perform(replaceText("Test User"));
//        onView(withId(R.id.email)).perform(replaceText("testuser@example.com"));
//        onView(withId(R.id.password_edit_text)).perform(replaceText("testpassword123"));
//        onView(withId(R.id.confirm_password_edit_text)).perform(replaceText("testpassword123"));
//
//        // Click the signup button
//        onView(withId(R.id.signupBtn)).perform(click());
//
//        // Verify the signup is successful by checking for a message or a redirection
//        FirebaseUser user = mAuth.getCurrentUser();
//        assertNotNull(user);
//
//        // Logout after signup
//        mAuth.signOut();
//
//        // Mock a click on the login tab in TabLayout
//        onView(withText("Login")).perform(click());
//
//        // Enter login details
//        onView(withId(R.id.email)).perform(replaceText("testuser@example.com"));
//        onView(withId(R.id.password_edit_text)).perform(replaceText("testpassword123"));
//
//        // Click the login button
//        onView(withId(R.id.loginBtn)).perform(click());
//
//        // Verify the login is successful by checking for the HomeActivity or a specific message
//        user = mAuth.getCurrentUser();
//        assertNotNull(user);
//        assertTrue(user.isEmailVerified());
//    }
//}
