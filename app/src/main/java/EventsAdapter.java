package com.example.studentacademiccalendar;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.Holder> {

    Context context;
    ArrayList<Event> events;
    DBHelper db;
    OnDataChangedListener onDataChangedListener;
    OnItemClickListener onItemClickListener;

    public interface OnDataChangedListener {
        void onDataChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public EventsAdapter(Context context, ArrayList<Event> events, OnDataChangedListener onDataChangedListener) {
        this.context = context;
        this.events = events;
        this.onDataChangedListener = onDataChangedListener;
        db = new DBHelper(context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Event e = events.get(position);

        holder.title.setText(e.getTitle());
        holder.datetime.setText(e.getDate() + "   " + e.getTime());
        holder.category.setText(e.getCategory());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(e);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Delete " + e.getTitle() + "?")
                    .setPositiveButton("Delete", (d, w) -> {
                        db.deleteEvent(e.getId());
                        onDataChangedListener.onDataChanged();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() { return events.size(); }

    public void setEvents(ArrayList<Event> list) {
        this.events = list;
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView title, datetime, category;

        public Holder(View v) {
            super(v);
            title = v.findViewById(R.id.eventTitle);
            datetime = v.findViewById(R.id.eventDateTime);
            category = v.findViewById(R.id.eventCategory);
        }
    }
}
