package com.example.studentacademiccalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "academic_events.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE events (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, date TEXT, time TEXT, category TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }

    public long addEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", event.getTitle());
        cv.put("description", event.getDescription());
        cv.put("date", event.getDate());
        cv.put("time", event.getTime());
        cv.put("category", event.getCategory());
        return db.insert("events", null, cv);
    }

    public Event getEvent(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("events", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Event event = null;
        if (c.moveToFirst()) {
            event = new Event(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("title")),
                    c.getString(c.getColumnIndexOrThrow("description")),
                    c.getString(c.getColumnIndexOrThrow("date")),
                    c.getString(c.getColumnIndexOrThrow("time")),
                    c.getString(c.getColumnIndexOrThrow("category"))
            );
        }
        c.close();
        return event;
    }

    public int updateEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", event.getTitle());
        cv.put("description", event.getDescription());
        cv.put("date", event.getDate());
        cv.put("time", event.getTime());
        cv.put("category", event.getCategory());
        return db.update("events", cv, "id=?", new String[]{String.valueOf(event.getId())});
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM events ORDER BY date,time", null);

        while (c.moveToNext()) {
            list.add(new Event(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)
            ));
        }
        c.close();
        return list;
    }

    public ArrayList<Event> getEventsByDate(String date) {
        ArrayList<Event> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM events WHERE date=?", new String[]{date});

        while (c.moveToNext()) {
            list.add(new Event(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)
            ));
        }
        c.close();
        return list;
    }

    public void deleteEvent(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("events", "id=?", new String[]{String.valueOf(id)});
    }
}
