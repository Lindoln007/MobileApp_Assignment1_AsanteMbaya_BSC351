# Student Academic Calendar

**Developed by:** Asante Mbaya  
**Course:** BSC351  
**Assignment:** Mobile App Assignment 1

---

## 📱 App Overview

**Student Academic Calendar** is a native Android application developed to assist students in managing their academic schedules effectively. The app serves as a centralized hub for tracking lectures, tests, assignments, and exams, ensuring that no deadline is missed.

It features a robust **notification system** that works even when the app is closed, leveraging Android's `AlarmManager` and `BroadcastReceiver` to deliver timely reminders.

## 🚀 Key Features

*   **📅 Comprehensive Event Management**
    *   **Add Events:** Schedule new academic tasks with a title, description, date, time, and category.
    *   **Edit & Update:** Modify existing event details.
    *   **Delete:** Remove completed or cancelled events.
    *   **View Mode:** Read-only mode to prevent accidental changes when checking details.

*   **🔔 Smart Notifications**
    *   Receive push notifications at the scheduled time for every event.
    *   Tapping a notification opens the app directly to the specific event's details.
    *   Requests `POST_NOTIFICATIONS` and `SCHEDULE_EXACT_ALARM` permissions for reliability on Android 13+.

*   **📂 Categorization**
    *   Organize tasks by type: **Lecture**, **Test**, **Assignment**, or **Exam**.

*   **💾 Offline Capability**
    *   All data is stored locally using **SQLite**, ensuring privacy and access without an internet connection.

## 📸 Screenshots

*To add screenshots, create a folder named `screenshots` in this directory and save your images there. Then, uncomment the lines below and update the filenames.*

| Main Screen | Add Event | Event Details |
|:---:|:---:|:---:|
| <!-- ![Main Screen](screenshots/main.png) --> <br> ![image alt](https://github.com/Lindoln007/MobileApp_Assignment1_AsanteMbaya_BSC351/blob/97cf0041af86528cfefe73043cdbd349be012beb/Screenshot_2025-11-23-19-22-04-676_com.example.studentacademiccalendar.jpg) | <!-- ![Add Event](screenshots/add.png) --> <br> *(Place Screenshot Here)* | <!-- ![Details](screenshots/details.png) --> <br> *(Place Screenshot Here)* |

## 🛠 Technical Stack

*   **Language:** Kotlin / Java
*   **Minimum SDK:** API 24 (Android 7.0)
*   **Target SDK:** API 36
*   **Database:** SQLite (Custom `DBHelper` implementation)
*   **Architecture:** MVC Pattern
*   **Key Components:**
    *   `RecyclerView` for efficient list rendering.
    *   `AlarmManager` for precise scheduling.
    *   `BroadcastReceiver` for handling background notifications.

## ⚙️ Setup & Installation

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/Lindoln007/MobileApp_Assignment1_AsanteMbaya_BSC351.git
    ```
2.  **Open in Android Studio:**
    *   Select "Open" and navigate to the project folder.
3.  **Build the Project:**
    *   Allow Gradle to sync dependencies.
4.  **Run:**
    *   Connect a device or start an emulator and press **Run**.

## 📄 Permissions Used

*   `android.permission.POST_NOTIFICATIONS`: For showing reminders in the status bar.
*   `android.permission.SCHEDULE_EXACT_ALARM`: For ensuring reminders fire at the exact right minute.
*   `android.permission.WAKE_LOCK`: To wake the CPU when a reminder needs to be sent.
