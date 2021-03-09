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
 * <p>
 * Taranath Pokhrel - Implemented notification service.
 * Itale Tabaksmane - Implemented navigation menu and all related methods.
 *
 * @author Christopher Mohan Romano
 * @author Taranath Pokhrel
 * @author Itale Tabaksmane
 * @version 1.0
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * Shared preferences key containing user's name.
     */
    public static final String SHARED_NAME = "fi.metropolia.christro.juo.SHARED_NAME";
    /**
     * Shared preferences key containing hydration goal.
     */
    public static final String SHARED_GOAL = "fi.metropolia.christro.juo.SHARED_GOAL";
    /**
     * Shared preferences key containing user's age.
     */
    public static final String SHARED_AGE = "fi.metropolia.christro.juo.SHARED_AGE";
    /**
     * Shared preferences key containing button top start amount.
     */
    public static final String SHARED_BUTTON_TOP_START = "fi.metropolia.christro.juo.SHARED_BUTTON_1";
    /**
     * Shared preferences key containing button top end amount.
     */
    public static final String SHARED_BUTTON_TOP_END = "fi.metropolia.christro.juo.SHARED_BUTTON_2";
    /**
     * Shared preferences key containing button bottom start amount.
     */
    public static final String SHARED_BUTTON_BOTTOM_START = "fi.metropolia.christro.juo.SHARED_BUTTON_3";
    /**
     * Shared preferences key containing button bottom end amount.
     */
    public static final String SHARED_BUTTON_BOTTOM_END = "fi.metropolia.christro.juo.SHARED_BUTTON_4";
    /**
     * Shared preferences key containing user's gender.
     */
    public static final String SHARED_GENDER = "fi.metropolia.christro.juo.SHARED_GENDER";

    /**
     * Array of strings containing some preset genders to populate dropdown menu.
     */
    private static final String[] GENDERS = new String[]{"Female", "Male", "N/A"};

    /**
     * Boolean stating if the app is started for the first time.
     */
    private boolean isFirstStartUp;

    /**
     * onCreate() method creates the activity.
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        updateUI(isFirstStartUp);

        /*
        Adapter for gender dropdown menu.
        https://www.tutorialspoint.com/how-to-set-adapter-to-auto-complete-text-view
        */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, GENDERS);

        ((AutoCompleteTextView) findViewById(R.id.dropdownMenuGender)).setAdapter(adapter);

        findViewById(R.id.dropdownMenuGender).setOnClickListener((view) ->
                ((AutoCompleteTextView) findViewById(R.id.dropdownMenuGender)).showDropDown());

        findViewById(R.id.buttonSaveSettings).setOnClickListener((view) -> {
            Toast.makeText(SettingsActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
            saveSettings();
        });

        //Turn on notifications.
        findViewById(R.id.imageButtonNotifications).setOnClickListener((view) -> {
            setNotification();
            Calendar calendar = Calendar.getInstance();
            Intent intent = new Intent(SettingsActivity.this, Receiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this,
                    100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    90 * 60 * 1000, pendingIntent);
        });

        /*
        This code is used to implement the navigation menu.
        https://www.youtube.com/watch?v=HwYENW0RyY4
        https://www.youtube.com/watch?v=fGcMLu1GJEc
        https://www.youtube.com/watch?v=zYVEMCiDcmY
        https://www.youtube.com/watch?v=bjYstsO1PgI
        https://www.youtube.com/watch?v=lt6xbth-yQo
         */
        DrawerLayout drawerLayoutSettings = findViewById(R.id.drawerLayoutSettings);
        NavigationView navigationViewSettings = findViewById(R.id.navigationViewSettings);
        Toolbar toolbarSettings = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbarSettings);
        if (savedInstanceState == null) {
            navigationViewSettings.setCheckedItem(R.id.nav_settings);
        }

        // Without this statement many devices will not show the menu as clickable
        navigationViewSettings.bringToFront();
        // making a menu icon click open the side navigation drawer
        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> drawerLayoutSettings.openDrawer(GravityCompat.START));
        // using animation whenever the menu opens, by swiping or clicking
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutSettings,
                toolbarSettings, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutSettings.addDrawerListener(toggle);
        toggle.syncState();

        // creates an intent for the appropriate activity by matching with item ID
        navigationViewSettings.setNavigationItemSelectedListener(item -> {
            Intent intent = null;

            if (item.getItemId() == R.id.nav_home) {
                intent = new Intent(SettingsActivity.this, MainActivity.class);
            } else if (item.getItemId() == R.id.nav_history) {
                intent = new Intent(SettingsActivity.this, History.class);
            } else if (item.getItemId() == R.id.nav_mood) {
                intent = new Intent(SettingsActivity.this, MoodListActivity.class);
            } else if (item.getItemId() == R.id.nav_location) {
                intent = new Intent(SettingsActivity.this, LocationActivity.class);
            } else if (item.getItemId() == R.id.nav_about) {
                intent = new Intent(SettingsActivity.this, AboutActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
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
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE,
                Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Get username
        String username = Objects.requireNonNull(((TextInputEditText)
                findViewById(R.id.editTextName)).getText()).toString();

        editor.putString(SHARED_NAME, username);

        //Get gender
        String gender = ((AutoCompleteTextView) findViewById(R.id.dropdownMenuGender)).getText().toString();

        editor.putString(SHARED_GENDER, gender);

        //Get age and parse to int. Automatically validate input and set errors if any.
        String stringAge = Objects.requireNonNull(((TextInputEditText)
                findViewById(R.id.editTextAge)).getText()).toString().trim();
        int age = 20;
        if (!stringAge.equals("")) {
            try {
                age = Integer.parseInt(stringAge);
                if (age < 0) {
                    ((TextInputLayout) findViewById(R.id.textLayoutAge))
                            .setError(getString(R.string.settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_AGE, age);
                ((TextInputLayout) findViewById(R.id.textLayoutAge)).setError(null);
            } catch (Exception e) {
                ((TextInputLayout) findViewById(R.id.textLayoutAge))
                        .setError(getString(R.string.settings_error_exception));
                return;
            }
        }

        //Get goal and parse to int. Automatically validate input and set errors if any.
        String stringGoal = Objects.requireNonNull(((TextInputEditText)
                findViewById(R.id.editTextGoal)).getText()).toString().trim();
        if (!stringGoal.equals("")) {
            try {
                int goal = Integer.parseInt(stringGoal);
                if (goal <= 0) {
                    ((TextInputLayout) findViewById(R.id.textLayoutGoal))
                            .setError(getString(R.string.settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_GOAL, goal);
                ((TextInputLayout) findViewById(R.id.textLayoutGoal)).setError(null);
            } catch (NumberFormatException e) {
                ((TextInputLayout) findViewById(R.id.textLayoutGoal))
                        .setError(getString(R.string.settings_error_exception));
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
        String stringButtonTopStart = Objects.requireNonNull(((TextInputEditText)
                findViewById(R.id.editTextSettingsButtonTopStart)).getText()).toString().trim();
        if (!stringButtonTopStart.equals("")) {
            try {
                int buttonTopStart = Integer.parseInt(stringButtonTopStart);
                if (buttonTopStart <= 0) {
                    ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonTopStart))
                            .setError(getString(R.string
                                    .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_TOP_START, buttonTopStart);
                ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonTopStart)).setError(null);
            } catch (NumberFormatException e) {
                ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonTopStart))
                        .setError(getString(R.string
                                .settings_error_exception));
                return;
            }
        } else {
            editor.putInt(SHARED_BUTTON_TOP_START, 250);
        }

        //Get buttonTopEnd and parse to int. Automatically validate input and set errors if any.
        String stringButtonTopEnd = Objects.requireNonNull(((TextInputEditText)
                findViewById(R.id.editTextSettingsButtonTopEnd)).getText()).toString().trim();
        if (!stringButtonTopEnd.equals("")) {
            try {
                int buttonTopEnd = Integer.parseInt(stringButtonTopEnd);
                if (buttonTopEnd <= 0) {
                    ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonTopEnd))
                            .setError(getString(R.string
                                    .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_TOP_END, buttonTopEnd);
                ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonTopEnd)).setError(null);
            } catch (NumberFormatException e) {
                ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonTopEnd))
                        .setError(getString(R.string
                                .settings_error_exception));
                return;
            }
        } else {
            editor.putInt(SHARED_BUTTON_TOP_END, 500);
        }

        //Get buttonBottomStart and parse to int. Automatically validate input and set errors if any.
        String stringButtonBottomStart = Objects.requireNonNull(((TextInputEditText)
                findViewById(R.id.editTextSettingsButtonBottomStart)).getText()).toString().trim();
        if (!stringButtonBottomStart.equals("")) {
            try {
                int buttonBottomStart = Integer.parseInt(stringButtonBottomStart);
                if (buttonBottomStart <= 0) {
                    ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonBottomStart))
                            .setError(getString(R.string
                                    .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_BOTTOM_START, buttonBottomStart);
                ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonBottomStart)).setError(null);
            } catch (NumberFormatException e) {
                ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonBottomStart))
                        .setError(getString(R.string
                                .settings_error_exception));
                return;
            }
        } else {
            editor.putInt(SHARED_BUTTON_BOTTOM_START, 100);
        }

        //Get buttonBottomEnd and parse to int. Automatically validate input and set errors if any.
        String stringButtonBottomEnd = Objects.requireNonNull(((TextInputEditText)
                findViewById(R.id.editTextSettingsButtonBottomEnd)).getText()).toString().trim();
        if (!stringButtonBottomEnd.equals("")) {
            try {
                int buttonBottomEnd = Integer.parseInt(stringButtonBottomEnd);
                if (buttonBottomEnd <= 0) {
                    ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonBottomEnd))
                            .setError(getString(R.string
                                    .settings_error_negative));
                    return;
                }
                editor.putInt(SHARED_BUTTON_BOTTOM_END, buttonBottomEnd);
                ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonBottomEnd)).setError(null);
            } catch (NumberFormatException e) {
                ((TextInputLayout) findViewById(R.id.textLayoutSettingsButtonBottomEnd))
                        .setError(getString(R.string
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
            //https://material.io/components/dialogs/android
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.settings_activity_dialog_title))
                    .setMessage(getString(R.string.settings_activity_dialog_content))
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getString(R.string.dialog_ok), (dialog, which) -> dialog.cancel())
                    .show();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE,
                    Activity.MODE_PRIVATE);

            ((TextInputEditText) findViewById(R.id.editTextName)).setText(sharedPreferences
                    .getString(SHARED_NAME, ""));

            ((TextInputEditText) findViewById(R.id.editTextAge)).setText(String
                    .valueOf(sharedPreferences.getInt(SHARED_AGE, 0)));

            ((TextInputEditText) findViewById(R.id.editTextGoal)).setText(String
                    .valueOf(sharedPreferences.getInt(SHARED_GOAL, 0)));

            ((TextInputEditText) findViewById(R.id.editTextSettingsButtonTopStart)).setText(String
                    .valueOf(sharedPreferences.getInt(SHARED_BUTTON_TOP_START, 0)));

            ((TextInputEditText) findViewById(R.id.editTextSettingsButtonTopEnd)).setText(String
                    .valueOf(sharedPreferences.getInt(SHARED_BUTTON_TOP_END, 0)));

            ((TextInputEditText) findViewById(R.id.editTextSettingsButtonBottomStart)).setText(String
                    .valueOf(sharedPreferences.getInt(SHARED_BUTTON_BOTTOM_START, 0)));

            ((TextInputEditText) findViewById(R.id.editTextSettingsButtonBottomEnd)).setText(String
                    .valueOf(sharedPreferences.getInt(SHARED_BUTTON_BOTTOM_END, 0)));

            ((AutoCompleteTextView) findViewById(R.id.dropdownMenuGender)).setText(sharedPreferences
                    .getString(SHARED_GENDER, ""));
        }
    }

    /**
     * Check whether user is using android Oreo or higher. Notification channel classes not
     * available at lower levels.
     */
    //https://www.youtube.com/watch?v=tTbd1Mfi-Sk&list=PLrnPJCHvNZuCN52QwGu7YTSLIMrjCF0gM
    private void setNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("notification",
                    "Notification", NotificationManager.IMPORTANCE_HIGH);

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
        if (((DrawerLayout) findViewById(R.id.drawerLayoutSettings)).isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            ((DrawerLayout) findViewById(R.id.drawerLayoutSettings)).closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}