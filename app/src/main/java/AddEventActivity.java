package com.example.studentacademiccalendar;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    EditText inputTitle, inputDescription, inputDate, inputTime;
    Spinner spinnerCategory;
    Button btnSave;
    DBHelper db;
    Calendar cal = Calendar.getInstance();
    Event currentEvent = null;
    boolean isViewMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        db = new DBHelper(this);
        findViews();
        setupUI();

        isViewMode = getIntent().getBooleanExtra("view_mode", false);
        int eventId = getIntent().getIntExtra(ReminderReceiver.EVENT_ID, -1);
        if (eventId != -1) {
            loadEvent(eventId);
        }

        if (isViewMode) {
            enterViewMode();
        }
    }

    private void findViews() {
        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);
        inputDate = findViewById(R.id.inputDate);
        inputTime = findViewById(R.id.inputTime);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSaveEvent);
    }

    private void setupUI() {
        String[] categories = {"Lecture", "Test", "Assignment", "Exam"};
        spinnerCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));

        inputDate.setOnClickListener(v -> pickDate());
        inputTime.setOnClickListener(v -> pickTime());
        btnSave.setOnClickListener(v -> saveEvent());
    }

    private void loadEvent(int eventId) {
        currentEvent = db.getEvent(eventId);
        if (currentEvent != null) {
            inputTitle.setText(currentEvent.getTitle());
            inputDescription.setText(currentEvent.getDescription());
            inputDate.setText(currentEvent.getDate());
            inputTime.setText(currentEvent.getTime());

            String[] categories = {"Lecture", "Test", "Assignment", "Exam"};
            int categoryIndex = Arrays.asList(categories).indexOf(currentEvent.getCategory());
            if (categoryIndex >= 0) {
                spinnerCategory.setSelection(categoryIndex);
            }
        }
    }

    private void enterViewMode() {
        inputTitle.setEnabled(false);
        inputDescription.setEnabled(false);
        inputDate.setEnabled(false);
        inputTime.setEnabled(false);
        spinnerCategory.setEnabled(false);
        btnSave.setVisibility(View.GONE);
    }

    private void pickDate() {
        if(isViewMode) return;
        DatePickerDialog dp = new DatePickerDialog(this,
                (view, y, m, d) -> {
                    cal.set(y, m, d);
                    inputDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime()));
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }

    private void pickTime() {
        if(isViewMode) return;
        TimePickerDialog tp = new TimePickerDialog(this,
                (view, h, min) -> {
                    cal.set(Calendar.HOUR_OF_DAY, h);
                    cal.set(Calendar.MINUTE, min);
                    inputTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(cal.getTime()));
                },
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        tp.show();
    }

    private void saveEvent() {
        String title = inputTitle.getText().toString().trim();
        String desc = inputDescription.getText().toString().trim();
        String date = inputDate.getText().toString().trim();
        String time = inputTime.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = (currentEvent == null) ? -1 : currentEvent.getId();
        if (id == -1) {
            id = db.addEvent(new Event(title, desc, date, time, category));
        } else {
            db.updateEvent(new Event((int) id, title, desc, date, time, category));
        }

        if (id > 0) {
            scheduleReminder((int) id, title, time);
            Toast.makeText(this, "Event Saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving event", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleReminder(int eventId, String title, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
            Toast.makeText(this, "Please grant permission to schedule exact alarms and save again", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:" + getPackageName())));
            return; // Stop if permission is not granted
        }

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra(ReminderReceiver.EVENT_ID, eventId);
        intent.putExtra("title", title);
        intent.putExtra("time", time);

        PendingIntent pending = PendingIntent.getBroadcast(this, eventId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pending);
    }
}
