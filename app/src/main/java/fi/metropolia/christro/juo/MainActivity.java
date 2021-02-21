package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DailyTotalViewModel dailyTotalViewModel;

    private IntakeInputRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new IntakeInputRepository(this.getApplication());

        long longish = 1000;

        TextView textView = findViewById(R.id.intakeText);
        CircularProgressBar circularProgressBar = findViewById(R.id.circularProgressBar);

        dailyTotalViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(DailyTotalViewModel.class);

        final Observer<Integer> dailyTotalObserver = newName -> {
            // Update the UI, in this case, a TextView.
            textView.setText(String.valueOf(newName));
            circularProgressBar.setProgressWithAnimation(newName, longish);
        };

        dailyTotalViewModel.getDailyTotal().observe(this, dailyTotalObserver);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(view -> {
            Intent intent = new Intent(this, ListIntakeInputActivity.class);
            startActivity(intent);
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            repository.insert(new IntakeInput(250));
        });
    }
}