package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Notification extends AppCompatActivity {

     Button btnGo;

     public static JuoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification2);

        btnGo = findViewById(R.id.btnOn);

        btnGo.setOnClickListener(v -> {
            createNotificationChannel();
            Toast.makeText(Notification.this, "Notification Allowed", Toast.LENGTH_SHORT).show();

            // AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            Intent intent = new Intent(Notification.this, Receiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(Notification.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);
        });
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("notification", "Alert",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("good morning");

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(notificationChannel);

            Log.i("HERE_TO_SET","SET_ME");
            dateIntake();
        }
    }

    public void dateIntake() {

        JuoViewModel juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        viewModel = juoViewModel;

        IntakeEntity intakeEntity = viewModel.getLatestIntake();

       if(intakeEntity == null){

           Log.i("NULL_HERE_TOO", "INTENET");
       }
        //dateTime is in format "yyyy-MM-dd HH:mm:ss.SSS"

   //     String dateTime = intakeEntity.getDate() + " " + intakeEntity.getTime();
     //   SimpleDateFormat date  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
       // date.parse(dateTime);
    }



}