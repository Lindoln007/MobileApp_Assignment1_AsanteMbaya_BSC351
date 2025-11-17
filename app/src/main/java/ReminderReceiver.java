package com.example.studentacademiccalendar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String EVENT_ID = "event_id";

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(
                    "academic_channel",
                    "Academic Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(ch);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        int eventId = intent.getIntExtra(EVENT_ID, -1);

        if (eventId == -1) return;

        Intent openAppIntent = new Intent(context, AddEventActivity.class);
        openAppIntent.putExtra(EVENT_ID, eventId);
        openAppIntent.putExtra("view_mode", true); // Always open in view mode from notification
        openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, eventId, openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "academic_channel")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("Event Reminder")
                .setContentText(title + " at " + time)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(eventId, builder.build());
    }
}
