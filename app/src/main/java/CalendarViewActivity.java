package com.example.studentacademiccalendar;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalendarViewActivity extends AppCompatActivity {

    CalendarView calendarView;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        db = new DBHelper(this);

        calendarView.setOnDateChangeListener((view, y, m, d) -> {

            Calendar c = Calendar.getInstance();
            c.set(y, m, d);

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
            ArrayList<Event> events = db.getEventsByDate(date);

            if (events.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle("No Events")
                        .setMessage("No events on " + date)
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            StringBuilder msg = new StringBuilder();
            for (Event e : events) {
                msg.append(e.getTime()).append(" - ").append(e.getTitle()).append(" (").append(e.getCategory()).append(")\n");
            }

            new AlertDialog.Builder(this)
                    .setTitle("Events on " + date)
                    .setMessage(msg.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}
