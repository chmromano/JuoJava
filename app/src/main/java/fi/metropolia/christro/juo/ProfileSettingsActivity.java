package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileSettingsActivity extends AppCompatActivity {

    public static final String SHARED_WEIGHT = "fi.metropolia.christro.juo.SHARED_WEIGHT";
    public static final String SHARED_USERNAME = "fi.metropolia.christro.juo.SHARED_USERNAME";
    public static final String SHARED_GOAL = "fi.metropolia.christro.juo.SHARED_GOAL";
    public static final String SHARED_AGE = "fi.metropolia.christro.juo.SHARED_AGE";

    private TextInputEditText editTextName;
    private TextInputEditText editTextAge;
    private TextInputEditText editTextWeight;
    private TextInputEditText editTextGoal;

    private TextInputLayout textLayoutAge;
    private TextInputLayout textLayoutWeight;
    private TextInputLayout textLayoutGoal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        Button button = findViewById(R.id.buttonSaveProfileSettings);

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextGoal = findViewById(R.id.editTextGoal);

        textLayoutAge = findViewById(R.id.textLayoutAge);
        textLayoutWeight = findViewById(R.id.textLayoutWeight);
        textLayoutGoal = findViewById(R.id.textLayoutGoal);

        button.setOnClickListener((view) -> {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

            String username = editTextName.getText().toString();
            sharedPreferencesEditor.putString(SHARED_USERNAME, username);

            String stringAge = editTextAge.getText().toString();
            try {
                int age = Integer.parseInt(stringAge);
                sharedPreferencesEditor.putInt(SHARED_AGE, age);
                textLayoutAge.setError(null);
            } catch (Exception e) {
                textLayoutAge.setError(getString(R.string.profile_settings_error_message));
                return;
            }

            String stringWeight = editTextWeight.getText().toString();
            try {
                int weight = Integer.parseInt(stringWeight);
                sharedPreferencesEditor.putInt(SHARED_WEIGHT, weight);
                textLayoutWeight.setError(null);
            } catch (Exception e) {
                textLayoutWeight.setError(getString(R.string.profile_settings_error_message));
                return;
            }

            String stringGoal = editTextGoal.getText().toString();
            try {
                int goal = Integer.parseInt(stringGoal);
                sharedPreferencesEditor.putInt(SHARED_GOAL, goal);
                textLayoutGoal.setError(null);
            } catch (NumberFormatException e) {
                textLayoutGoal.setError(getString(R.string.profile_settings_error_message));
                return;
            }


            sharedPreferencesEditor.apply();

            Intent intent = getIntent();

            boolean isFirstStartUp = intent.getBooleanExtra(MainActivity.EXTRA_IS_FIRST_START_UP, false);

            if (isFirstStartUp) {
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}