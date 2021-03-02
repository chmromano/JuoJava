package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_WEIGHT = "fi.metropolia.christro.juo.SHARED_WEIGHT";
    public static final String SHARED_NAME = "fi.metropolia.christro.juo.SHARED_USERNAME";
    public static final String SHARED_GOAL = "fi.metropolia.christro.juo.SHARED_GOAL";
    public static final String SHARED_AGE = "fi.metropolia.christro.juo.SHARED_AGE";
    public static final String SHARED_BUTTON_1 = "fi.metropolia.christro.juo.SHARED_BUTTON_1";
    public static final String SHARED_BUTTON_2 = "fi.metropolia.christro.juo.SHARED_BUTTON_2";
    public static final String SHARED_BUTTON_3 = "fi.metropolia.christro.juo.SHARED_BUTTON_3";
    public static final String SHARED_BUTTON_4 = "fi.metropolia.christro.juo.SHARED_BUTTON_4";
    public static final String SHARED_GENDER = "fi.metropolia.christro.juo.SHARED_GENDER";

    private TextInputEditText editTextName;
    private TextInputEditText editTextAge;
    private TextInputEditText editTextGoal;
    private TextInputEditText editTextSettingsButton1;
    private TextInputEditText editTextSettingsButton2;
    private TextInputEditText editTextSettingsButton3;
    private TextInputEditText editTextSettingsButton4;

    private TextInputLayout textLayoutAge;
    private TextInputLayout textLayoutGoal;
    private TextInputLayout textLayoutSettingsButton1;
    private TextInputLayout textLayoutSettingsButton2;
    private TextInputLayout textLayoutSettingsButton3;
    private TextInputLayout textLayoutSettingsButton4;

    private AutoCompleteTextView dropdownMenuGender;

    private static final String[] GENDER = new String[]{"Female", "Male", "N/A"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        boolean isFirstStartUp = intent.getBooleanExtra(MainActivity.EXTRA_IS_FIRST_START_UP, false);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);

        Button buttonSaveProfileSettings = findViewById(R.id.buttonSaveProfileSettings);

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextGoal = findViewById(R.id.editTextGoal);
        editTextSettingsButton1 = findViewById(R.id.editTextSettingsButton1);
        editTextSettingsButton2 = findViewById(R.id.editTextSettingsButton2);
        editTextSettingsButton3 = findViewById(R.id.editTextSettingsButton3);
        editTextSettingsButton4 = findViewById(R.id.editTextSettingsButton4);

        dropdownMenuGender = findViewById(R.id.dropdownMenuGender);

        textLayoutAge = findViewById(R.id.textLayoutAge);
        textLayoutGoal = findViewById(R.id.textLayoutGoal);
        textLayoutSettingsButton1 = findViewById(R.id.textLayoutSettingsButton1);
        textLayoutSettingsButton2 = findViewById(R.id.textLayoutSettingsButton2);
        textLayoutSettingsButton3 = findViewById(R.id.textLayoutSettingsButton3);
        textLayoutSettingsButton4 = findViewById(R.id.textLayoutSettingsButton4);

        if (isFirstStartUp) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.profile_settings_activity_dialog_title))
                    .setMessage(getString(R.string.profile_settings_activity_dialog_content))
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getString(R.string.dialog_ok), (dialog, which) -> dialog.cancel())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if (!isFirstStartUp) {
            editTextName.setText(sharedPreferences.getString(SHARED_NAME, ""));
            editTextAge.setText(String.valueOf(sharedPreferences.getInt(SHARED_AGE, 0)));
            editTextGoal.setText(String.valueOf(sharedPreferences.getInt(SHARED_GOAL, 0)));
            editTextSettingsButton1.setText(String.valueOf(sharedPreferences.getInt(SHARED_BUTTON_1, 0)));
            editTextSettingsButton2.setText(String.valueOf(sharedPreferences.getInt(SHARED_BUTTON_2, 0)));
            editTextSettingsButton3.setText(String.valueOf(sharedPreferences.getInt(SHARED_BUTTON_3, 0)));
            editTextSettingsButton4.setText(String.valueOf(sharedPreferences.getInt(SHARED_BUTTON_4, 0)));
            dropdownMenuGender.setText(sharedPreferences.getString(SHARED_GENDER,""));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, GENDER);

        dropdownMenuGender.setAdapter(adapter);

        dropdownMenuGender.setOnClickListener((view) -> {
            dropdownMenuGender.showDropDown();
        });

        buttonSaveProfileSettings.setOnClickListener((view) -> {
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

            String username = editTextName.getText().toString();
            sharedPreferencesEditor.putString(SHARED_NAME, username);

            String gender = dropdownMenuGender.getText().toString();
            sharedPreferencesEditor.putString(SHARED_GENDER, gender);

            String stringAge = editTextAge.getText().toString().trim();
            int age = 20;
            if (!stringAge.equals("")) {
                try {
                    age = Integer.parseInt(stringAge);
                    if (age <= 0) {
                        textLayoutAge.setError(getString(R.string.profile_settings_error_negative));
                        return;
                    }
                    sharedPreferencesEditor.putInt(SHARED_AGE, age);
                    textLayoutAge.setError(null);
                } catch (Exception e) {
                    textLayoutAge.setError(getString(R.string.profile_settings_error_exception));
                    return;
                }
            }

            String stringGoal = editTextGoal.getText().toString().trim();
            if (!stringGoal.equals("")) {
                try {
                    int goal = Integer.parseInt(stringGoal);
                    if (goal <= 0) {
                        textLayoutGoal.setError(getString(R.string.profile_settings_error_negative));
                        return;
                    }
                    sharedPreferencesEditor.putInt(SHARED_GOAL, goal);
                    textLayoutGoal.setError(null);
                } catch (NumberFormatException e) {
                    textLayoutGoal.setError(getString(R.string.profile_settings_error_exception));
                    return;
                }
            } else {
                if(age >= 18) {
                    if (gender.toLowerCase().equals("male")) {
                        sharedPreferencesEditor.putInt(SHARED_GOAL, 3700);
                    } else if (gender.toLowerCase().equals(("female"))) {
                        sharedPreferencesEditor.putInt(SHARED_GOAL, 2700);
                    }else{
                        sharedPreferencesEditor.putInt(SHARED_GOAL, 3200);
                    }
                }else{
                    if (gender.toLowerCase().equals("male")){
                        sharedPreferencesEditor.putInt(SHARED_GOAL, 3300);
                    } else if (gender.toLowerCase().equals("female")){
                        sharedPreferencesEditor.putInt(SHARED_GOAL, 2300);
                    } else {
                        sharedPreferencesEditor.putInt(SHARED_GOAL, 2800);
                    }
                }
            }

            String stringButton1 = editTextSettingsButton1.getText().toString().trim();
            if (!stringButton1.equals("")) {
                try {
                    int button1 = Integer.parseInt(stringButton1);
                    if (button1 <= 0) {
                        textLayoutSettingsButton1.setError(getString(R.string.profile_settings_error_negative));
                        return;
                    }
                    sharedPreferencesEditor.putInt(SHARED_BUTTON_1, button1);
                    textLayoutSettingsButton1.setError(null);
                } catch (NumberFormatException e) {
                    textLayoutSettingsButton1.setError(getString(R.string.profile_settings_error_exception));
                    return;
                }
            } else {
                sharedPreferencesEditor.putInt(SHARED_BUTTON_1, 250);
            }

            String stringButton2 = editTextSettingsButton2.getText().toString().trim();
            if (!stringButton2.equals("")) {
                try {
                    int button2 = Integer.parseInt(stringButton2);
                    if (button2 <= 0) {
                        textLayoutSettingsButton2.setError(getString(R.string.profile_settings_error_negative));
                        return;
                    }
                    sharedPreferencesEditor.putInt(SHARED_BUTTON_2, button2);
                    textLayoutSettingsButton2.setError(null);
                } catch (NumberFormatException e) {
                    textLayoutSettingsButton2.setError(getString(R.string.profile_settings_error_exception));
                    return;
                }
            } else {
                sharedPreferencesEditor.putInt(SHARED_BUTTON_2, 500);
            }

            String stringButton3 = editTextSettingsButton3.getText().toString().trim();
            if (!stringButton3.equals("")) {
                try {
                    int button3 = Integer.parseInt(stringButton3);
                    if (button3 <= 0) {
                        textLayoutSettingsButton3.setError(getString(R.string.profile_settings_error_negative));
                        return;
                    }
                    sharedPreferencesEditor.putInt(SHARED_BUTTON_3, button3);
                    textLayoutSettingsButton3.setError(null);
                } catch (NumberFormatException e) {
                    textLayoutSettingsButton3.setError(getString(R.string.profile_settings_error_exception));
                    return;
                }
            } else {
                sharedPreferencesEditor.putInt(SHARED_BUTTON_3, 100);
            }

            String stringButton4 = editTextSettingsButton4.getText().toString().trim();
            if (!stringButton4.equals("")) {
                try {
                    int button4 = Integer.parseInt(stringButton4);
                    if (button4 <= 0) {
                        textLayoutSettingsButton4.setError(getString(R.string.profile_settings_error_negative));
                        return;
                    }
                    sharedPreferencesEditor.putInt(SHARED_BUTTON_4, button4);
                    textLayoutSettingsButton4.setError(null);
                } catch (NumberFormatException e) {
                    textLayoutSettingsButton4.setError(getString(R.string.profile_settings_error_exception));
                    return;
                }
            } else {
                sharedPreferencesEditor.putInt(SHARED_BUTTON_4, 750);
            }

            sharedPreferencesEditor.apply();

            if (isFirstStartUp) {
                Intent returnIntent = new Intent(this, MainActivity.class);
                startActivity(returnIntent);
            }
        });
    }
}