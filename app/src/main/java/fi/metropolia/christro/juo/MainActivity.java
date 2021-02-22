package fi.metropolia.christro.juo;

import androidx.annotation.Nullable;
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

    private IntakeInputViewModel intakeInputViewModel;

    private IntakeInputRepository repository;

    private CircularProgressBar circularProgressBar;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new IntakeInputRepository(this.getApplication());

        textView = findViewById(R.id.intakeText);
        circularProgressBar = findViewById(R.id.circularProgressBar);

        intakeInputViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(IntakeInputViewModel.class);

        final Observer<Integer> dailyTotalObserver = newTotal -> {
            // Update the UI, in this case, a TextView.
            textView.setText(String.valueOf(newTotal));

            if(newTotal != null) {
                circularProgressBar.setProgressWithAnimation(newTotal, (long) 300);
            }
        };

        intakeInputViewModel.getDailyTotal().observe(this, dailyTotalObserver);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(view -> {
            repository.insert(new IntakeInput(400));
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            repository.insert(new IntakeInput(250));
        });
    }
}