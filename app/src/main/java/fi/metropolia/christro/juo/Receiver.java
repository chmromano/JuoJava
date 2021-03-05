package fi.metropolia.christro.juo;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.DateInterval;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoDatabase_Impl;
import fi.metropolia.christro.juo.database.JuoViewModel;


public class Receiver extends BroadcastReceiver {

    Application application;

    @Override
    public void onReceive(Context context, Intent intent) {


        // check the last time the user drank water - database
        // check current time
        // get the difference
        // if the difference is 2 hours then fire event
        // if the user has already drank 5 litres - meet goal - no notification

         this.application = (Application) context.getApplicationContext();

        try {
            if (this.fireNotification()) {


                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                        .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
                        .setContentTitle("Alert")
                        .setContentText("Time to drink")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);


                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(1, builder.build());
            }
        } catch (ParseException e) {
            Log.i("ERROR_BROADCAST",e.getMessage());
        }

    }


    private boolean fireNotification() throws ParseException {

        Date lastTimeDrink = this.getLastTimeDrink();
        Date currentTime = Calendar.getInstance().getTime();

        long timeDifference = currentTime.getTime() - lastTimeDrink.getTime();

        long differenceInHours =  timeDifference / (1000 * 60 * 60) % 24;


        long totalIntakeToday =this.getTotalDrinkToday();
        long goalAmount = this.getGoalAmount();

        if(goalAmount <= totalIntakeToday){
            return false;
        }

        return differenceInHours >= 2;

    }


    private Date getLastTimeDrink() throws ParseException {




        IntakeEntity intakeEntity = Notification.viewModel.getLatestIntake();



        if(intakeEntity == null){



            Log.i("DATE_LAST", "NULL");
            return Calendar.getInstance().getTime();
        }

        SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Log.i("DATE_DRINK", dateFormat.parse(intakeEntity.getDate()).toString());

      return dateFormat.parse(intakeEntity.getDate());

    }

    private int getTotalDrinkToday(){


        return 1;
    }

    private int getGoalAmount(){


        return 1;
    }



}

