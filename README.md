# HabitHive

HabitHive is an Android application designed to help users build, manage, and track habits effectively. The app provides a comprehensive dashboard with calendar-based tracking, rich habit customization, and data synchronization.

## Capabilities & Features

### 1. Advanced Habit Tracking
- **Flexible Scheduling:** Set habits to occur daily (on specific days of the week) or monthly (on specific days of the month).
- **Goal Types:** Customize your habits tracking with different goal methods:
  - **Checklists:** Mark items off as you go.
  - **Counters:** Track numeric values (e.g., drink 8 glasses of water).
  - **Timers:** Time your habits.
- **Progress Tracking:** Monitor your completion rate and maintain streaks to keep yourself motivated.

### 2. Diverse Habit Categories
Organize your routines with specialized categories:
- 💼 Work
- 📚 Study
- 🏥 Health
- 💰 Finance
- 🏕️ Outdoor
- 🥗 Nutrition
- 🤝 Social
- 🧘 Meditation
- 🚫 Quit
- 🏃 Run

### 3. User Interface & Dashboards
- **Weekly/Calendar View:** A dynamic view pager that lets you seamlessly navigate through weeks and see exactly what habits are due on any given day.
- **Data Visualization:** Integrated with MPAndroidChart to provide visual insights into your habit-building progress.

### 4. Cloud Database & Authentication
- **User Accounts:** Secure authentication handled via Firebase Auth. Connect, sign up, and log in seamlessly.
- **Cloud Sync:** Uses Firebase Firestore to store, update, and real-time sync habit data across your instances to ensure you never lose your progress.

## System Overview

HabitHive is built using modern Android development practices:
- **Language:** Java
- **Architecture:** The application relies on Android Activity/Fragment lifecycles combined with `RecyclerView`s and custom adapters (`HabitAdapter`, `WeekAdapter`) for UI modeling.
- **Backend:** 
  - **Firebase Firestore:** NoSQL cloud database for data persistence.
  - **Firebase Auth:** Handles secure user authentication.
- **Key Libraries:**
  - `MPAndroidChart` - For rendering statistics and progress charts.
  - `ThreeTenABP` - For robust date and time manipulation (Java 8 time API backport).
  - Material Design Components (`com.google.android.material`) for rich UI building.
- **Minimum SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)

---
*Start building better habits with HabitHive today!*
