package fi.metropolia.christro.juo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

/**
 * BroadcastReceiver passes notification to the user when event occurs
 *
 * @author Taranath Pokhrel
 * @version 1.0
 */
//https://www.youtube.com/watch?v=tTbd1Mfi-Sk&list=PLrnPJCHvNZuCN52QwGu7YTSLIMrjCF0gM
public class Receiver extends BroadcastReceiver {

    /**
     * onReceive() method  sends the notification
     *
     * @param context Access information  or start an activity
     * @param intent  Checks register of an event
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        //Get notifications between 7 and 21.
        if (hourOfDay > 7 && hourOfDay < 21) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                    .setSmallIcon(R.drawable.ic_round_circle_notifications_24)
                    .setContentTitle("Juo!")
                    .setContentText("Time to drink!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        }
    }
}

