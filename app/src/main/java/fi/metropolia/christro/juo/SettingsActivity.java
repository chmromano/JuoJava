package fi.metropolia.christro.juo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_NAME = "fi.metropolia.christro.juo.SHARED_USERNAME";
    public static final String SHARED_GOAL = "fi.metropolia.christro.juo.SHARED_GOAL";
    public static final String SHARED_AGE = "fi.metropolia.christro.juo.SHARED_AGE";
    public static final String SHARED_BUTTON_TOP_START = "fi.metropolia.christro.juo.SHARED_BUTTON_1";
    public static final String SHARED_BUTTON_TOP_END = "fi.metropolia.christro.juo.SHARED_BUTTON_2";
    public static final String SHARED_BUTTON_BOTTOM_START = "fi.metropolia.christro.juo.SHARED_BUTTON_3";
    public static final String SHARED_BUTTON_BOTTOM_END = "fi.metropolia.christro.juo.SHARED_BUTTON_4";
    public static final String SHARED_GENDER = "fi.metropolia.christro.juo.SHARED_GENDER";

    private static final String[] GENDER = new String[]{"Female", "Male", "N/A"};

    private TextInputEditText editTextName;
    private TextInputEditText editTextAge;
    private TextInputEditText editTextGoal;
    private TextInputEditText editTextSettingsButtonTopStart;
    private TextInputEditText editTextSettingsButtonTopEnd;
    private TextInputEditText editTextSettingsButtonBottomStart;
    private TextInputEditText editTextSettingsButtonBottomEnd;

    private TextInputLayout textLayoutAge;
    private TextInputLayout textLayoutGoal;
    private TextInputLayout textLayoutSettingsButtonTopStart;
    private TextInputLayout textLayoutSettingsButtonTopEnd;
    private TextInputLayout textLayoutSettingsButtonBottomStart;
    private TextInputLayout textLayoutSettingsButtonBottomEnd;

    private AutoCompleteTextView dropdownMenuGender;

    private SharedPreferences sharedPreferences;

    private Button buttonSaveProfileSettings;
    private boolean isFirstStartUp;

    private DrawerLayout drawerLayoutSettings;
    private Toolbar toolbarSettings;
    private NavigationView navigationViewSettings;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initialiseAll();
        updateUI(isFirstStartUp);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, GENDER);
        dropdownMenuGender.setAdapter(adapter);
        dropdownMenuGender.setOnClickListener((view) -> dropdownMenuGender.showDropDown());

        buttonSaveProfileSettings.setOnClickListener((view) -> {
            Toast.makeText(SettingsActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
            saveSettings();
        });

        //This code is used to implement the navigation bar.
        setSupportActionBar(toolbarSettings);
        if (savedInstanceState == null) {
            navigationViewSettings.setCheckedItem(R.id.nav_settings);
        }
        navigationViewSettings.bringToFront();

        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> drawerLayoutSettings.openDrawer(GravityCompat.START));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutSettings,
                toolbarSettings, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutSettings.addDrawerListener(toggle);
        toggle.syncState();

        navigationViewSettings.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    intent = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_history:
                    intent = new Intent(SettingsActivity.this, History.class);
                    startActivity(intent);
                    break;
                case R.id.nav_mood:
                    intent = new Intent(SettingsActivity.this, MoodActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_location:
                    intent = new Intent(SettingsActivity.this, LocationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_settings:
                    break;
                case R.id.nav_about:
                    intent = new Intent(SettingsActivity.this, AboutActivity.class);
                    startActivity(intent);
                    break;
            }
            drawerLayoutSettings.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(isFirstStartUp);
    }

    private void saveSettings(){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String username = Objects.requireNonNull(editTextName.getText()).toString();
        editor.putString(SHARED_NAME, username);

        String gender = dropdownMenuGender.getText().toString();
        editor.putString(SHARED_GENDER, gender);

        String stringAge = Objects.requireNonNull(editTextAge.getText()).toString().trim();
        int age = 20;
        if (!stringAge.equals("")) {
            try {
                age = Integer.parseInt(stringAge);
                if (age < 0) {
                    textLayoutAge.setError(getString(R.string.settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_AGE, age);
                textLayoutAge.setError(null);
            } catch (Exception e) {
                textLayoutAge.setError(getString(R.string.settings_error_exception));
                return;
            }
        }

        String stringGoal = Objects.requireNonNull(editTextGoal.getText()).toString().trim();
        if (!stringGoal.equals("")) {
            try {
                int goal = Integer.parseInt(stringGoal);
                if (goal <= 0) {
                    textLayoutGoal.setError(getString(R.string.settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_GOAL, goal);
                textLayoutGoal.setError(null);
            } catch (NumberFormatException e) {
                textLayoutGoal.setError(getString(R.string.settings_error_exception));
                return;
            }
        } else {
            if (age >= 18) {
                if (gender.toLowerCase().equals("male")) {
                    editor.putInt(SHARED_GOAL, 3700);
                } else if (gender.toLowerCase().equals(("female"))) {
                    editor.putInt(SHARED_GOAL, 2700);
                } else {
                    editor.putInt(SHARED_GOAL, 3200);
                }
            } else {
                if (gender.toLowerCase().equals("male")) {
                    editor.putInt(SHARED_GOAL, 3300);
                } else if (gender.toLowerCase().equals("female")) {
                    editor.putInt(SHARED_GOAL, 2300);
                } else {
                    editor.putInt(SHARED_GOAL, 2800);
                }
            }
        }

        String stringButton1 = Objects.requireNonNull(editTextSettingsButtonTopStart.getText()).toString().trim();
        if (!stringButton1.equals("")) {
            try {
                int button1 = Integer.parseInt(stringButton1);
                if (button1 <= 0) {
                    textLayoutSettingsButtonTopStart.setError(getString(R.string
                            .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_TOP_START, button1);
                textLayoutSettingsButtonTopStart.setError(null);
            } catch (NumberFormatException e) {
                textLayoutSettingsButtonTopStart.setError(getString(R.string
                        .settings_error_exception));
                return;
            }
        } else {
            editor.putInt(SHARED_BUTTON_TOP_START, 250);
        }

        String stringButton2 = Objects.requireNonNull(editTextSettingsButtonTopEnd.getText()).toString().trim();
        if (!stringButton2.equals("")) {
            try {
                int button2 = Integer.parseInt(stringButton2);
                if (button2 <= 0) {
                    textLayoutSettingsButtonTopEnd.setError(getString(R.string
                            .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_TOP_END, button2);
                textLayoutSettingsButtonTopEnd.setError(null);
            } catch (NumberFormatException e) {
                textLayoutSettingsButtonTopEnd.setError(getString(R.string
                        .settings_error_exception));
                return;
            }
        } else {
            editor.putInt(SHARED_BUTTON_TOP_END, 500);
        }

        String stringButton3 = Objects.requireNonNull(editTextSettingsButtonBottomStart.getText()).toString().trim();
        if (!stringButton3.equals("")) {
            try {
                int button3 = Integer.parseInt(stringButton3);
                if (button3 <= 0) {
                    textLayoutSettingsButtonBottomStart.setError(getString(R.string
                            .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_BOTTOM_START, button3);
                textLayoutSettingsButtonBottomStart.setError(null);
            } catch (NumberFormatException e) {
                textLayoutSettingsButtonBottomStart.setError(getString(R.string
                        .settings_error_exception));
                return;
            }
        } else {
            editor.putInt(SHARED_BUTTON_BOTTOM_START, 100);
        }

        String stringButton4 = Objects.requireNonNull(editTextSettingsButtonBottomEnd.getText()).toString().trim();
        if (!stringButton4.equals("")) {
            try {
                int button4 = Integer.parseInt(stringButton4);
                if (button4 <= 0) {
                    textLayoutSettingsButtonBottomEnd.setError(getString(R.string
                            .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_BOTTOM_END, button4);
                textLayoutSettingsButtonBottomEnd.setError(null);
            } catch (NumberFormatException e) {
                textLayoutSettingsButtonBottomEnd.setError(getString(R.string
                        .settings_error_exception));
                return;
            }
        } else {
            editor.putInt(SHARED_BUTTON_BOTTOM_END, 750);
        }

        editor.apply();

        if (isFirstStartUp) {
            isFirstStartUp = false;
            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);
        }
    }

    private void updateUI(boolean isFirstStartupVariable) {
        if (isFirstStartupVariable) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.settings_activity_dialog_title))
                    .setMessage(getString(R.string.settings_activity_dialog_content))
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getString(R.string.dialog_ok), (dialog, which) -> dialog.cancel())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            editTextName.setText(sharedPreferences.getString(SHARED_NAME, ""));
            editTextAge.setText(String.valueOf(sharedPreferences.getInt(SHARED_AGE, 0)));
            editTextGoal.setText(String.valueOf(sharedPreferences.getInt(SHARED_GOAL, 0)));
            editTextSettingsButtonTopStart.setText(String.valueOf(sharedPreferences
                    .getInt(SHARED_BUTTON_TOP_START, 0)));
            editTextSettingsButtonTopEnd.setText(String.valueOf(sharedPreferences
                    .getInt(SHARED_BUTTON_TOP_END, 0)));
            editTextSettingsButtonBottomStart.setText(String.valueOf(sharedPreferences
                    .getInt(SHARED_BUTTON_BOTTOM_START, 0)));
            editTextSettingsButtonBottomEnd.setText(String.valueOf(sharedPreferences
                    .getInt(SHARED_BUTTON_BOTTOM_END, 0)));
            dropdownMenuGender.setText(sharedPreferences.getString(SHARED_GENDER, ""));
        }
    }

    private void initialiseAll() {
        sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);

        buttonSaveProfileSettings = findViewById(R.id.buttonSaveProfileSettings);

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextGoal = findViewById(R.id.editTextGoal);
        editTextSettingsButtonTopStart = findViewById(R.id.editTextSettingsButtonTopStart);
        editTextSettingsButtonTopEnd = findViewById(R.id.editTextSettingsButtonTopEnd);
        editTextSettingsButtonBottomStart = findViewById(R.id.editTextSettingsButtonBottomStart);
        editTextSettingsButtonBottomEnd = findViewById(R.id.editTextSettingsButtonBottomEnd);

        dropdownMenuGender = findViewById(R.id.dropdownMenuGender);

        textLayoutAge = findViewById(R.id.textLayoutAge);
        textLayoutGoal = findViewById(R.id.textLayoutGoal);
        textLayoutSettingsButtonTopStart = findViewById(R.id.textLayoutSettingsButtonTopStart);
        textLayoutSettingsButtonTopEnd = findViewById(R.id.textLayoutSettingsButtonTopEnd);
        textLayoutSettingsButtonBottomStart = findViewById(R.id.textLayoutSettingsButtonBottomStart);
        textLayoutSettingsButtonBottomEnd = findViewById(R.id.textLayoutSettingsButtonBottomEnd);
        //Navigation views
        drawerLayoutSettings = findViewById(R.id.drawerLayoutSettings);
        navigationViewSettings = findViewById(R.id.navigationViewSettings);
        toolbarSettings = findViewById(R.id.toolbarSettings);

        Intent intent = getIntent();
        isFirstStartUp = intent.getBooleanExtra(MainActivity.EXTRA_IS_FIRST_START_UP, false);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutSettings.isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            drawerLayoutSettings.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}