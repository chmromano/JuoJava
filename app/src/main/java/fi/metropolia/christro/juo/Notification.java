package fi.metropolia.christro.juo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import fi.metropolia.christro.juo.database.JuoViewModel;


public class Notification extends AppCompatActivity {

     Button btnGo;

     public static JuoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        btnGo = findViewById(R.id.btnOn);

        btnGo.setOnClickListener(v -> {
           setModel();
            Calendar calendar = Calendar.getInstance();
            Intent intent = new Intent(Notification.this, Receiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(Notification.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  90 * 60 * 1000, pendingIntent);
        });
    }

    public void setModel() {

        viewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        LiveData liveData = viewModel.getLatestIntake();

       if(liveData == null){

           Log.i("CHECK_NULL_INTAKE", "NULL");
       }

    }



}