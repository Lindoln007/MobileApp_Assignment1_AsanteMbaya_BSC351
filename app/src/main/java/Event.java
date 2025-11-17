package com.example.studentacademiccalendar;

public class Event {
    int id;
    String title, description, date, time, category;

    public Event() {}

    public Event(int id, String title, String description, String date, String time, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.category = category;
    }

    public Event(String title, String description, String date, String time, String category) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.category = category;
    }

    // getters & setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getCategory() { return category; }

    public void setId(int id) { this.id = id; }
}
