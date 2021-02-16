package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private IntakeList intakeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intakeList = IntakeList.getInstance();

        CircularProgressBar circularProgressBar = findViewById(R.id.circularProgressBar);

        Button button1 = findViewById(R.id.button1);

        Button button2 = findViewById(R.id.button2);

        long l = 1000;

        button1.setOnClickListener(view -> {
            circularProgressBar.setProgressWithAnimation(55f, l);
            intakeList.addIntake(250);
            for(IntakeObject intakeObject: intakeList.getAllIntakes()){
                Log.d("INTAKE_TESTING: ", intakeObject.toString());
            }
        });

        button2.setOnClickListener(view -> {
            intakeList.addIntake(500);
            for(IntakeObject intakeObject: intakeList.getAllIntakes()){
                Log.d("INTAKE_TESTING: ", intakeObject.toString());
            }
        });
    }

    private void updateList(){
        intakeList = IntakeList.getInstance();
    }
}