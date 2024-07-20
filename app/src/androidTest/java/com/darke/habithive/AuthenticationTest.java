//package com.darke.habithive;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.espresso.intent.Intents;
//import androidx.test.espresso.intent.matcher.IntentMatchers;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.espresso.Espresso;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.hamcrest.Matcher;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.InstrumentationRegistry.getTargetContext;
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.replaceText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
//import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import static java.util.concurrent.CompletableFuture.allOf;
//
//import android.content.ComponentName;
//import android.content.Intent;
//import android.util.Log;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import java.util.concurrent.CompletableFuture;
//
//@RunWith(AndroidJUnit4.class)
//public class AuthenticationTest {
//
//    @Rule
//    public ActivityScenarioRule<User> activityScenarioRule =
//            new ActivityScenarioRule<>(User.class);
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
//    public void testEmailPasswordSignup() {
//
//        // Perform Firebase check for signup
//        onView(withId(R.id.main_user)).check(matches(isCompletelyDisplayed()));
//
//        Espresso.onView(ViewMatchers.withId(R.id.toolbar))
//                .check(matches(isCompletelyDisplayed()));
//
//        Espresso.onView(ViewMatchers.withId(R.id.view_pager))
//                .check(matches(isCompletelyDisplayed()));
//
//        Espresso.onView(ViewMatchers.withId(R.id.tab_layout))
//                .check(matches(isCompletelyDisplayed()));
//
//        Espresso.onView(withText("Signup"))
//                .check(matches(isCompletelyDisplayed()))
//                .perform(click());
//
//        onView(withId(R.id.fullName_user)).perform(replaceText("Test User"));
//        onView(withId(R.id.email)).perform(replaceText("test@example.com"));
//        onView(withId(R.id.password_edit_text)).perform(replaceText("12345678"));
//        onView(withId(R.id.confirm_password_edit_text)).perform(replaceText("12345678"));
//
//        // Click signup button
//        onView(withId(R.id.signupBtn)).perform(click());
//
//        // Delay to wait for Firebase authentication to complete (adjust as needed)
//        try {
//            Thread.sleep(3000); // Wait for 3 seconds (adjust as needed)
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }        // Verify signup success and redirection to Home activity
//
//        FirebaseUser user = mAuth.getCurrentUser();
//        Log.d("User", user.getUid().toString());
//        assertNotNull(user);
//        mAuth.signOut();
//    }
//    @Test
//    public void testEmailPasswordLogin() {
//
//        // Click logout or navigate back to login screen
//        onView(withId(R.id.main_user)).check(matches(isCompletelyDisplayed()));
//
//        Espresso.onView(ViewMatchers.withId(R.id.toolbar))
//                .check(matches(isCompletelyDisplayed()));
//
//        Espresso.onView(ViewMatchers.withId(R.id.view_pager))
//                .check(matches(isCompletelyDisplayed()));
//
//        Espresso.onView(ViewMatchers.withId(R.id.tab_layout))
//                .check(matches(isCompletelyDisplayed()));
//
//        // Enter email and password
//        onView(withId(R.id.email)).perform(replaceText("test@example.com"));
//        onView(withId(R.id.password_edit_text)).perform(replaceText("12345678"));
//
//        // Click login button
//        onView(withId(R.id.loginBtn)).perform(click());
//
//        // Verify login success and redirection to Home activity
//        FirebaseUser user = mAuth.getCurrentUser();
//        user = mAuth.getCurrentUser();
//        assertNotNull(user);
//    }
//}
