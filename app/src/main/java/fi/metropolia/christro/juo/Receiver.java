package fi.metropolia.christro.juo;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;

/**
 *
 *
 * @author Taranath Pokhrel
 * @version 1.0
 */
public class Receiver extends BroadcastReceiver {

    Application application;

    Context context;

    /**
     *
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        this.application = (Application) context.getApplicationContext();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (hourOfDay > 7 && hourOfDay < 21) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                    .setSmallIcon(R.drawable.ic_round_circle_notifications_24)
                    .setContentTitle("Juo! reminder")
                    .setContentText("Time to drink")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        }
    }
}

