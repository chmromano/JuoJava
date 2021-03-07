package fi.metropolia.christro.juo;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;


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
            setModel();
        }
    }

    public void setModel() {

        viewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        IntakeEntity intakeEntity = viewModel.getLatestIntake();

       if(intakeEntity == null){

           Log.i("CHECK_NULL_INTAKE", "NULL");
       }

    }



}