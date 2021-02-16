package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircularProgressBar circularProgressBar = findViewById(R.id.circularProgressBar);

        Button button = findViewById(R.id.button1);

        long l = 1000;

        button.setOnClickListener(view -> {
            circularProgressBar.setProgressWithAnimation(55f, l);
        });
    }
}