package com.example.studentacademiccalendar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), EventsAdapter.OnDataChangedListener, EventsAdapter.OnItemClickListener {

    private lateinit var db: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventsAdapter

    private val PERMISSION_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Notification channel needed for Android 8+
        ReminderReceiver.createChannel(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), PERMISSION_REQUEST_CODE)
            }
        }

        db = DBHelper(this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerEvents)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EventsAdapter(this, db.getAllEvents(), this)
        adapter.setOnItemClickListener(this)
        recyclerView.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fabAddEvent)
        fab.setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        adapter.setEvents(db.getAllEvents())
        if (db.getAllEvents().isEmpty()) {
            Toast.makeText(this, "No events. Tap + to add one.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDataChanged() {
        refreshList()
    }

    override fun onItemClick(event: Event) {
        val options = arrayOf("View", "Edit")
        AlertDialog.Builder(this)
            .setTitle(event.title)
            .setItems(options) { dialog, which ->
                val intent = Intent(this, AddEventActivity::class.java)
                intent.putExtra(ReminderReceiver.EVENT_ID, event.id)
                if (which == 0) { // View
                    intent.putExtra("view_mode", true)
                }
                startActivity(intent)
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_calendar -> {
                startActivity(Intent(this, CalendarViewActivity::class.java))
                true
            }
            R.id.menu_view_all -> {
                startActivity(Intent(this, ViewEventsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
