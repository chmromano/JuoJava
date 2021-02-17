package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircularProgressBar circularProgressBar = findViewById(R.id.circularProgressBar);

        Button button1 = findViewById(R.id.button1);

        Button button2 = findViewById(R.id.button2);

        long l = 1000;

        button1.setOnClickListener(view -> {
            circularProgressBar.setProgressWithAnimation(55f, l);

            Intent intent = new Intent(this, ListIntakeInputActivity.class);

            startActivity(intent);
        });

        button2.setOnClickListener(view -> {

        });
    }
}