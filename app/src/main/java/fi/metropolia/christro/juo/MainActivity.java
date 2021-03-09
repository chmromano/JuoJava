package fi.metropolia.christro.juo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;

/**
 * Main activity of the application.
 * <p>
 * Taranath Pokhrel - Implemented functionality to get current weather, and method to hide keyboard.
 * Itale Tabaksmane - Implemented navigation menu and all related methods.
 *
 * @author Christopher Mohan Romano
 * @author Taranath Pokhrel
 * @author Itale Tabaksmane
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * String with the API url
     */
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=35be7f414814f513a3bdf6ce70e1fcec&q=";

    /**
     * Name of the preference file.
     */
    public static final String PREFERENCE_FILE = "fi.metropolia.christro.juo";
    /**
     * Name of boolean extra stating if the app is being started for the first time.
     */
    public static final String EXTRA_IS_FIRST_START_UP = "fi.metropolia.christro.juo.EXTRA_IS_FIRST_START_UP";

    /**
     * Integer containing hydration goal.
     */
    private int hydrationGoal;
    /**
     * Integer containing extra goal based on temperature.
     */
    private int extraHydrationGoal;
    /**
     * ViewModel used to access database methods.
     */
    private JuoViewModel juoViewModel;

    /**
     * onCreate() method creates the activity.
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateUI();

        juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        //Code used to observe if LiveData changes. If it does update UI.
        final Observer<Integer> dailyTotalObserver = newTotal -> {
            // Update the UI, in this case, a TextView.
            if (newTotal != null) {
                ((TextView) findViewById(R.id.textViewIntake)).setText(getString(R.string.main_activity_intake, newTotal,
                        hydrationGoal + extraHydrationGoal));
                //Using CircularProgressBar dependency: https://github.com/lopspower/CircularProgressBar
                ((CircularProgressBar) findViewById(R.id.circularProgressBar)).setProgressWithAnimation(newTotal, (long) 300);
            } else {
                ((TextView) findViewById(R.id.textViewIntake)).setText(getString(R.string.main_activity_intake, 0, hydrationGoal));
                ((CircularProgressBar) findViewById(R.id.circularProgressBar)).setProgress(0);
            }
        };
        juoViewModel.getDailyTotal().observe(this, dailyTotalObserver);

        //ClickListeners for intake buttons.
        IntakeButtonClick intakeButtonClick = new IntakeButtonClick();
        findViewById(R.id.mainActivityButtonTopStart).setOnClickListener(intakeButtonClick);
        findViewById(R.id.mainActivityButtonTopEnd).setOnClickListener(intakeButtonClick);
        findViewById(R.id.mainActivityButtonBottomStart).setOnClickListener(intakeButtonClick);
        findViewById(R.id.mainActivityButtonBottomEnd).setOnClickListener(intakeButtonClick);

        //Go to activity to input mood.
        findViewById(R.id.buttonMoodInput).setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, MoodActivity.class);
            startActivity(intent);
        });

        //What to do when enter is pressed in custom input TextView.
        findViewById(R.id.editTextCustomInput).setOnKeyListener(this::onKeyEnter);

        /*
        This code is used to implement the navigation menu.
        https://www.youtube.com/watch?v=HwYENW0RyY4
        https://www.youtube.com/watch?v=fGcMLu1GJEc
        https://www.youtube.com/watch?v=zYVEMCiDcmY
        https://www.youtube.com/watch?v=bjYstsO1PgI
        https://www.youtube.com/watch?v=lt6xbth-yQo
         */
        DrawerLayout drawerLayoutMainActivity = findViewById(R.id.drawerLayoutMainActivity);
        NavigationView navigationViewMain = findViewById(R.id.navigationViewMain);
        Toolbar toolbarMain = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbarMain);
        if (savedInstanceState == null) {
            navigationViewMain.setCheckedItem(R.id.nav_home);
        }

        // Without this statement many devices will not show the menu as clickable
        navigationViewMain.bringToFront();
        // making a menu icon click open the side navigation drawer
        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> drawerLayoutMainActivity.openDrawer(GravityCompat.START));
        // using animation whenever the menu opens, by swiping or clicking
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutMainActivity,
                toolbarMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutMainActivity.addDrawerListener(toggle);
        toggle.syncState();

        // creates an intent for the appropriate activity by matching with item ID
        navigationViewMain.setNavigationItemSelectedListener(item -> {

            Intent intent = null;

            if (item.getItemId() == R.id.nav_history) {
                intent = new Intent(MainActivity.this, HistoryActivity.class);
            } else if (item.getItemId() == R.id.nav_mood) {
                intent = new Intent(MainActivity.this, MoodListActivity.class);
            } else if (item.getItemId() == R.id.nav_location) {
                intent = new Intent(MainActivity.this, LocationActivity.class);
            } else if (item.getItemId() == R.id.nav_settings) {
                intent = new Intent(MainActivity.this, SettingsActivity.class);
            } else if (item.getItemId() == R.id.nav_about) {
                intent = new Intent(MainActivity.this, AboutActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
            }

            drawerLayoutMainActivity.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * Method to validate custom input, add it to the database, close the keyboard, and lose focus
     * when the enter key is pressed.
     *
     * @param v       The view on which to operate.
     * @param keyCode The code of the pressed key.
     * @param event   The event (key was pressed).
     * @return Returns a true if the event happened, otherwise false.
     */
    private boolean onKeyEnter(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            String stringCustomInput = Objects.requireNonNull(((TextView)
                    findViewById(R.id.editTextCustomInput)).getText()).toString();

            int customInput = 0;
            try {
                customInput = Integer.parseInt(stringCustomInput);
                if (customInput <= 0) {
                    ((TextInputLayout) findViewById(R.id.textLayoutCustomInput)).setError(" ");
                    ((TextInputLayout) findViewById(R.id.textLayoutCustomInput)).setErrorIconDrawable(0);
                    return false;
                }
                ((TextInputLayout) findViewById(R.id.textLayoutCustomInput)).setError(null);
            } catch (Exception e) {
                ((TextInputLayout) findViewById(R.id.textLayoutCustomInput)).setError(" ");
                ((TextInputLayout) findViewById(R.id.textLayoutCustomInput)).setErrorIconDrawable(0);
            }
            juoViewModel.insertIntake(new IntakeEntity(customInput));
            ((TextInputEditText) findViewById(R.id.editTextCustomInput)).setText("");
            MainActivity.this.hideSoftKeyboard(v);
            findViewById(R.id.editTextCustomInput).clearFocus();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            findViewById(R.id.editTextCustomInput).clearFocus();
            return true;
        }
        return false;
    }

    /**
     * onResume() method to update the UI and get the weather.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        getWeather();
    }

    /**
     * Method to load the hydration goal from SharedPreferences. Hydration goal has default value
     * on first start-up. If value in SharedPreferences is default value shows a dialog with a
     * welcome message and brings you to settings.
     *
     * @return An integer containing the hydration goal.
     */
    private int loadHydrationGoal() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE,
                Activity.MODE_PRIVATE);

        int sharedGoal = sharedPreferences.getInt(SettingsActivity.SHARED_GOAL, 999999);

        if (sharedGoal == 999999) {
            //https://material.io/components/dialogs/android
            new MaterialAlertDialogBuilder(this)
                    .setMessage(getString(R.string.main_activity_dialog_content))
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getString(R.string.dialog_ok), (dialog, which) -> {
                        dialog.cancel();
                        Intent isFirstStartupIntent = new Intent(this, SettingsActivity.class);
                        isFirstStartupIntent.putExtra(EXTRA_IS_FIRST_START_UP, true);
                        startActivity(isFirstStartupIntent);
                    })
                    .show();
            return 999999;
        }

        return sharedGoal;
    }

    /**
     * Method to get weather. Loads location from SharedPreferences. Uses OpenWeatherMap API.
     */
    //https://www.youtube.com/watch?v=tdx9ReYGIoE
    private void getWeather() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE,
                Activity.MODE_PRIVATE);

        String location = sharedPreferences.getString(LocationActivity.SHARED_LOCATION, null);

        String weatherUrl;

        if (location != null) {
            weatherUrl = API_URL + location.replaceAll("\\s+", "+");
        } else {
            weatherUrl = API_URL + null;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, weatherUrl, response -> {
            try {
                JSONObject responseObject = new JSONObject(response);
                //find country
                JSONObject jsonMain = responseObject.getJSONObject("main");
                JSONArray jsonWeather = responseObject.getJSONArray("weather");
                JSONObject jsonWeatherId = jsonWeather.getJSONObject(0);

                String humidity = jsonMain.getString("humidity");

                String stringTemperature = jsonMain.getString("temp");
                double temperature = 0;
                try {
                    temperature = Double.parseDouble(stringTemperature);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                String stringWeatherId = jsonWeatherId.getString("id");
                int weatherId = 0;
                try {
                    weatherId = Integer.parseInt(stringWeatherId);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                updateWeatherUI(location, temperature, humidity, weatherId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> updateWeatherUI());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Method to update the weather UI (text and icon) when weather request is unsuccessful.
     */
    private void updateWeatherUI() {
        ((TextView) findViewById(R.id.textViewTemperature)).setText(getString(R.string.not_available));
        ((TextView) findViewById(R.id.textViewCity)).setText((getString(R.string.location_not_found)));
        ((TextView) findViewById(R.id.textViewTemperature)).setText(getString(R.string.not_available));
    }

    /**
     * Method to update the weather UI (text and icon) when weather request is successful.
     * Automatically sets an extra hydration goal if temperature is high.
     *
     * @param location    String containing the name of the location.
     * @param temperature Double containing the temperature value.
     * @param humidity    String containing humidity value.
     * @param weatherId   Integer containing the weather ID.
     */
    private void updateWeatherUI(String location, double temperature, String humidity, int weatherId) {

        if (temperature > 40f) {
            extraHydrationGoal = 2000;
        } else if (temperature > 35f) {
            extraHydrationGoal = 1500;
        } else if (temperature > 30f) {
            extraHydrationGoal = 1000;
        } else if (temperature > 24f) {
            extraHydrationGoal = 500;
        }

        if (extraHydrationGoal > 0) {
            ((TextView) findViewById(R.id.textViewExtraIntake)).setText(getString(R.string.main_activity_extra_intake, extraHydrationGoal));
        }

        ((CircularProgressBar) findViewById(R.id.circularProgressBar))
                .setProgressMax((float) hydrationGoal + extraHydrationGoal);

        ((TextView) findViewById(R.id.textViewCity)).setText(location.replaceAll("\\+", " "));
        ((TextView) findViewById(R.id.textViewTemperature)).setText(getString(R.string.text_view_temperature,
                temperature, humidity));

        TextView textViewWeatherIcon = findViewById(R.id.textViewWeatherIcon);
        if (weatherId >= 200 && weatherId <= 232) {
            textViewWeatherIcon.setText(getString(R.string.thunderstorm));
        } else if (weatherId >= 300 && weatherId <= 321) {
            textViewWeatherIcon.setText(getString(R.string.drizzle));
        } else if (weatherId >= 500 && weatherId <= 531) {
            textViewWeatherIcon.setText(getString(R.string.rain));
        } else if (weatherId >= 600 && weatherId <= 622) {
            textViewWeatherIcon.setText(getString(R.string.snow));
        } else if (weatherId >= 700 && weatherId <= 781) {
            textViewWeatherIcon.setText(getString(R.string.fog));
        } else if (weatherId == 800) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if (hour < 6 || hour > 18) {
                textViewWeatherIcon.setText(getString(R.string.clear_night));
            } else {
                textViewWeatherIcon.setText(getString(R.string.clear_day));
            }
        } else if (weatherId >= 801 && weatherId <= 804) {
            textViewWeatherIcon.setText(getString(R.string.clouds));
        } else {
            textViewWeatherIcon.setText(getString(R.string.not_available));
        }
    }

    /**
     * Method to update the UI.
     */
    private void updateUI() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE,
                Activity.MODE_PRIVATE);

        hydrationGoal = loadHydrationGoal();

        ((CircularProgressBar) findViewById(R.id.circularProgressBar))
                .setProgressMax((float) hydrationGoal);

        int sharedButtonTopStart = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_TOP_START, 250);
        ((Button) findViewById(R.id.mainActivityButtonTopStart)).setText(String.valueOf(sharedButtonTopStart));

        int sharedButtonTopEnd = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_TOP_END, 500);
        ((Button) findViewById(R.id.mainActivityButtonTopEnd)).setText(String.valueOf(sharedButtonTopEnd));

        int sharedButtonBottomStart = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_BOTTOM_START, 100);
        ((Button) findViewById(R.id.mainActivityButtonBottomStart)).setText(String.valueOf(sharedButtonBottomStart));

        int sharedButtonBottomEnd = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_BOTTOM_END, 750);
        ((Button) findViewById(R.id.mainActivityButtonBottomEnd)).setText(String.valueOf(sharedButtonBottomEnd));
    }

    /**
     * Custom private OnClickListener class for intake buttons.
     *
     * @author Christopher Mohan Romano
     * @version 1.0
     */
    private class IntakeButtonClick implements View.OnClickListener {
        /**
         * Custom OnClick method. Parses button labels to integer and inputs to database as intake
         * amount. All labels are already checked for exceptions in settings activity.
         *
         * @param view Parameter containing the pressed view.
         */
        @Override
        public void onClick(View view) {
            int intake = 0;
            if (view.getId() == R.id.mainActivityButtonTopStart) {
                intake = Integer.parseInt(((Button) findViewById(R.id.mainActivityButtonTopStart))
                        .getText().toString());
            } else if (view.getId() == R.id.mainActivityButtonTopEnd) {
                intake = Integer.parseInt(((Button) findViewById(R.id.mainActivityButtonTopEnd))
                        .getText().toString());
            } else if (view.getId() == R.id.mainActivityButtonBottomStart) {
                intake = Integer.parseInt(((Button) findViewById(R.id.mainActivityButtonBottomStart))
                        .getText().toString());
            } else if (view.getId() == R.id.mainActivityButtonBottomEnd) {
                intake = Integer.parseInt(((Button) findViewById(R.id.mainActivityButtonBottomEnd))
                        .getText().toString());
            }
            juoViewModel.insertIntake(new IntakeEntity(intake));
        }
    }

    /**
     * Method to hide the keyboard.
     *
     * @param view Parameter containing the given view.
     */
    private void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Method overrides normal onBackPressed() in case the drawer menu is open.
     */
    @Override
    public void onBackPressed() {
        if (((DrawerLayout) findViewById(R.id.drawerLayoutMainActivity)).isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            ((DrawerLayout) findViewById(R.id.drawerLayoutMainActivity)).closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}