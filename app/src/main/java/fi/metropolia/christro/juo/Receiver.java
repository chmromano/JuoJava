package fi.metropolia.christro.juo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                    .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
                    .setContentTitle("Alert")
                    .setContentText("Time to drink")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());

        }
    }

