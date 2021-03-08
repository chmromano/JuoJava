package fi.metropolia.christro.juo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import java.util.Calendar;
import java.util.Objects;


/**
 * Settings activity of the application.
 *
 * @author Christopher Mohan Romano
 * @author Taranath Pokhrel
 * @author Itale Tabaksmane
 * @version 1.0
 */
/*
Taranath Pokhrel - Implemented notification service.
Itale Tabaksmane - Implemented navigation menu and all related methods.
 */
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
    private ImageButton imageButtonNotifications;

    private boolean isFirstStartUp;

    private DrawerLayout drawerLayoutSettings;
    private Toolbar toolbarSettings;
    private NavigationView navigationViewSettings;

    /**
     * onCreate() method creates the activity.
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initialiseAll();
        updateUI(isFirstStartUp);

        //Adapter for gender dropdown menu.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, GENDER);
        dropdownMenuGender.setAdapter(adapter);
        dropdownMenuGender.setOnClickListener((view) -> dropdownMenuGender.showDropDown());

        buttonSaveProfileSettings.setOnClickListener((view) -> {
            Toast.makeText(SettingsActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
            saveSettings();
        });

        //Turn on notifications.
        imageButtonNotifications.setOnClickListener(v -> {
            setNotification();
            Calendar calendar = Calendar.getInstance();
            Intent intent = new Intent(SettingsActivity.this, Receiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this,
                    100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 90 * 60 * 1000, pendingIntent);
        });

        //This code is used to implement the navigation menu.
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
                    intent = new Intent(SettingsActivity.this, MoodListActivity.class);
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

    /**
     * onResume() method to update the UI.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateUI(isFirstStartUp);
    }

    /**
     * Method to validate all settings inputted by the user and save them to shared preferences. If
     * user leaves fields blank they will be populated with defaults. If user enters an age and/or a
     * gender, an appropriate hydration goal will be selected automatically.
     */
    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Get username
        String username = Objects.requireNonNull(editTextName.getText()).toString();
        editor.putString(SHARED_NAME, username);

        //Get gender
        String gender = dropdownMenuGender.getText().toString();
        editor.putString(SHARED_GENDER, gender);

        //Get age and parse to int. Automatically validate input and set errors if any.
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

        //Get goal and parse to int. Automatically validate input and set errors if any.
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
            //If statement to automatically set a goal based on age and/or gender, or default.
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

        //Get buttonTopStart and parse to int. Automatically validate input and set errors if any.
        String stringButtonTopStart = Objects.requireNonNull(editTextSettingsButtonTopStart.getText()).toString().trim();
        if (!stringButtonTopStart.equals("")) {
            try {
                int buttonTopStart = Integer.parseInt(stringButtonTopStart);
                if (buttonTopStart <= 0) {
                    textLayoutSettingsButtonTopStart.setError(getString(R.string
                            .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_TOP_START, buttonTopStart);
                textLayoutSettingsButtonTopStart.setError(null);
            } catch (NumberFormatException e) {
                textLayoutSettingsButtonTopStart.setError(getString(R.string
                        .settings_error_exception));
                return;
            }
        } else {
            editor.putInt(SHARED_BUTTON_TOP_START, 250);
        }

        //Get buttonTopEnd and parse to int. Automatically validate input and set errors if any.
        String stringButtonTopEnd = Objects.requireNonNull(editTextSettingsButtonTopEnd.getText()).toString().trim();
        if (!stringButtonTopEnd.equals("")) {
            try {
                int buttonTopEnd = Integer.parseInt(stringButtonTopEnd);
                if (buttonTopEnd <= 0) {
                    textLayoutSettingsButtonTopEnd.setError(getString(R.string
                            .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_TOP_END, buttonTopEnd);
                textLayoutSettingsButtonTopEnd.setError(null);
            } catch (NumberFormatException e) {
                textLayoutSettingsButtonTopEnd.setError(getString(R.string
                        .settings_error_exception));
                return;
            }
        } else {
            editor.putInt(SHARED_BUTTON_TOP_END, 500);
        }

        //Get buttonBottomStart and parse to int. Automatically validate input and set errors if any.
        String stringButtonBottomStart = Objects.requireNonNull(editTextSettingsButtonBottomStart.getText()).toString().trim();
        if (!stringButtonBottomStart.equals("")) {
            try {
                int buttonBottomStart = Integer.parseInt(stringButtonBottomStart);
                if (buttonBottomStart <= 0) {
                    textLayoutSettingsButtonBottomStart.setError(getString(R.string
                            .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_BOTTOM_START, buttonBottomStart);
                textLayoutSettingsButtonBottomStart.setError(null);
            } catch (NumberFormatException e) {
                textLayoutSettingsButtonBottomStart.setError(getString(R.string
                        .settings_error_exception));
                return;
            }
        } else {
            editor.putInt(SHARED_BUTTON_BOTTOM_START, 100);
        }

        //Get buttonBottomEnd and parse to int. Automatically validate input and set errors if any.
        String stringButtonBottomEnd = Objects.requireNonNull(editTextSettingsButtonBottomEnd.getText()).toString().trim();
        if (!stringButtonBottomEnd.equals("")) {
            try {
                int buttonBottomEnd = Integer.parseInt(stringButtonBottomEnd);
                if (buttonBottomEnd <= 0) {
                    textLayoutSettingsButtonBottomEnd.setError(getString(R.string
                            .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_BOTTOM_END, buttonBottomEnd);
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

        //If is first startup return to main activity on press.
        if (isFirstStartUp) {
            isFirstStartUp = false;
            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);
        }
    }

    /**
     * Method to update the UI. If app is launched for the first time leaves everything blank,
     * otherwise automatically populates settings with what is saved in shared preferences.
     *
     * @param isFirstStartupVariable Boolean to check is app is being launched for the first time.
     */
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

    /**
     * Method to initialise all views needed by the activity.
     */
    private void initialiseAll() {
        sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);

        buttonSaveProfileSettings = findViewById(R.id.buttonSaveProfileSettings);
        imageButtonNotifications = findViewById(R.id.imageButtonNotifications);

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

    /**
     * Check whether user is using android Oreo or higher. Notification channel classes not
     * available at lower levels.
     */
    private void setNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("notification", "Notification",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("Reminds User to drink");

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(notificationChannel);
            Toast.makeText(SettingsActivity.this, "Notifications active", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SettingsActivity.this, "Notifications not supported", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method overrides normal onBackPressed() in case the drawer menu is open.
     */
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