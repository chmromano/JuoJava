package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ProfileSettingsActivity extends AppCompatActivity {

    public static final String SHARED_WEIGHT = "fi.metropolia.christro.juo.SHARED_WEIGHT";
    public static final String SHARED_USERNAME = "fi.metropolia.christro.juo.SHARED_USERNAME";
    public static final String SHARED_GOAL = "fi.metropolia.christro.juo.SHARED_GOAL";

    private TextInputEditText editTextUsername;
    private TextInputEditText editTextWeight;
    private TextInputEditText editTextGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        Button button = findViewById(R.id.buttonSaveProfileSettings);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextGoal = findViewById(R.id.editTextGoal);

        button.setOnClickListener((view) -> {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

            String username = editTextUsername.getText().toString();
            sharedPreferencesEditor.putString(SHARED_USERNAME, username);

            String stringWeight = editTextWeight.getText().toString();
            try {
                int weight = Integer.parseInt(stringWeight);
                sharedPreferencesEditor.putInt(SHARED_WEIGHT, weight);
            } catch (Exception e) {
                return;
            }

            String stringGoal = editTextGoal.getText().toString();
            try {
                int goal = Integer.parseInt(stringGoal);
                sharedPreferencesEditor.putInt(SHARED_GOAL, goal);
            } catch (NumberFormatException e) {
                return;
            }


            sharedPreferencesEditor.commit();

            Intent intent = getIntent();

            boolean isFirstStartUp = intent.getBooleanExtra(MainActivity.EXTRA_IS_FIRST_START_UP, false);

            if(isFirstStartUp == true) {
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}