package com.example.studentacademiccalendar;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewEventsActivity extends AppCompatActivity implements EventsAdapter.OnDataChangedListener {

    RecyclerView recycler;
    DBHelper db;
    EventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        db = new DBHelper(this);

        recycler = findViewById(R.id.recyclerAllEvents);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventsAdapter(this, db.getAllEvents(), this);
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setEvents(db.getAllEvents());
    }

    @Override
    public void onDataChanged() {
        adapter.setEvents(db.getAllEvents());
    }
}
