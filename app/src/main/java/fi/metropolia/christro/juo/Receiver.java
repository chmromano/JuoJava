package fi.metropolia.christro.juo;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fi.metropolia.christro.juo.database.JuoViewModel;


public class Receiver extends BroadcastReceiver {

    Application application;

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        // check the last time the user drank water - database
        // check current time
        // get the difference
        // if the difference is 2 hours then fire event
        // if the user has already drank  meet goal - no notification

        this.context = context;
        this.application = (Application) context.getApplicationContext();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);


        /**
         *   get notification between 7 and 21
         */

        if (hourOfDay > 7 && hourOfDay < 21) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                    .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
                    .setContentTitle("Juo! reminder")
                    .setContentText("Time to drink")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        }
    }

    /**
     * notification method when user forgets to drink in 2 hrs and when goal is not achieved.
     *
     * @return
     * @throws ParseException
     */


    private boolean fireNotification() throws ParseException {

        Date lastTimeDrink = this.getLastTimeDrink();

        Date currentTime = Calendar.getInstance().getTime();

        if (lastTimeDrink == null) {
            return false;
        }

        long timeDifference = currentTime.getTime() - lastTimeDrink.getTime();

        long differenceInHours = timeDifference / (1000 * 60 * 60);

        long totalIntakeToday = this.getTotalDrinkToday();
        long goalAmount = this.getGoalAmount();

        if (goalAmount <= totalIntakeToday) {
            return false;
        }

        return differenceInHours >= 2;

    }

    /**
     * Get the last time the user hydrated
     *
     * @return
     * @throws ParseException
     */


    private Date getLastTimeDrink() throws ParseException {

        JuoViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.application))
                .get(JuoViewModel.class);
        ;

        LiveData<String> mLiveData = Notification.viewModel.getLatestIntake();

        if (mLiveData.getValue() == null) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Log.i("DATE_DRINK", dateFormat.parse(mLiveData.getValue()).toString());

        return dateFormat.parse(mLiveData.getValue());

    }

    /**
     * get total daily intake.
     *
     * @return
     */

    private int getTotalDrinkToday() {

        LiveData<Integer> liveData = Notification.viewModel.getDailyTotal();

        if (liveData.getValue() != null) {
            return liveData.getValue();
        }

        return 0;
    }

    /**
     * method to get users targeted goal.
     *
     * @return int
     */
    private int getGoalAmount() {

        SharedPreferences sharedPreferences = this.context.getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);

        return (sharedPreferences.getInt(SettingsActivity.SHARED_GOAL, 0));
    }


}

