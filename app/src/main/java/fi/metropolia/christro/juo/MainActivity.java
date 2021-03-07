package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.util.Log;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;

/**
 * Main activity of the application.
 * @author Christopher Mohan Romano
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=35be7f414814f513a3bdf6ce70e1fcec&q=";
    public static final String PREFERENCE_FILE = "fi.metropolia.christro.juo";
    public static final String EXTRA_IS_FIRST_START_UP = "fi.metropolia.christro.juo.EXTRA_IS_FIRST_START_UP";
    private int hydrationGoal;

    //Intake views
    private CircularProgressBar circularProgressBar;
    private TextView textViewIntake;
    private TextView textViewExtraIntake;
    //Weather views
    private TextView textViewTemperature;
    private TextView textViewHumidity;
    private TextView textViewCity;
    private TextView textViewWeatherIcon;
    //Button views
    private Button mainActivityButtonTopStart;
    private Button mainActivityButtonTopEnd;
    private Button mainActivityButtonBottomStart;
    private Button mainActivityButtonBottomEnd;
    //Custom input views
    private TextInputEditText editTextCustomInput;
    private TextInputLayout textLayoutLCustomInput;
    //ViewModel
    private JuoViewModel juoViewModel;
    //SharedPreferences
    private SharedPreferences sharedPreferences;


    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private static final String TAG = "Selected Menu Item";


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseAll();
        updateUI();


        //navigation
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(savedInstanceState == null){
            navigationView.setCheckedItem(R.id.nav_home);
        }
        navigationView.bringToFront();

        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> {
            drawer.openDrawer(GravityCompat.START);
        });



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: "+item.getItemId());
                //item = navigationView.getCheckedItem();
                Intent intent;
                //Context context = getApplicationContext();
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this,"Home",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_mood:
                        Toast.makeText(MainActivity.this,"Mood",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, MoodActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_history:
                        Toast.makeText(MainActivity.this,"Charts",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, History.class);
                        Log.d(TAG, "onNavigationItemSelected: history");
                        startActivity(intent);
                        break;

                    case R.id.nav_about:
                        Toast.makeText(MainActivity.this,"About",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, About.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_help:
                        Toast.makeText(MainActivity.this,"Help",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, Help.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        Toast.makeText(MainActivity.this,"Profile",Toast.LENGTH_SHORT).show();
                        //intent = new Intent(context,Profile.class);
                        break;

                    case R.id.nav_settings:
                        Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_SHORT).show();
                        //intent = new Intent(context, Settings.class);
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        final Observer<Integer> dailyTotalObserver = newTotal -> {
            // Update the UI, in this case, a TextView.
            if (newTotal != null) {
                textViewIntake.setText(getString(R.string.main_activity_intake, newTotal, hydrationGoal));
                circularProgressBar.setProgressWithAnimation(newTotal, (long) 300);
            } else {
                textViewIntake.setText(getString(R.string.main_activity_intake, 0, hydrationGoal));
                circularProgressBar.setProgress(0);
            }
        };

        juoViewModel.getDailyTotal().observe(this, dailyTotalObserver);

        textViewWeatherIcon.setOnClickListener((view) -> getWeather());

        IntakeButtonClick intakeButtonClick = new IntakeButtonClick();
        mainActivityButtonTopStart.setOnClickListener(intakeButtonClick);
        mainActivityButtonTopEnd.setOnClickListener(intakeButtonClick);
        mainActivityButtonBottomStart.setOnClickListener(intakeButtonClick);
        mainActivityButtonBottomEnd.setOnClickListener(intakeButtonClick);

        Intent returnIntent = new Intent(this, LocationActivity.class);
        findViewById(R.id.buttonMoodInput).setOnClickListener((view) -> startActivity(returnIntent));

        editTextCustomInput.setOnKeyListener((v, keyCode, event) -> onKeyEnter(v, keyCode, event));
    }

    /**
     * Method to validate custom input, add it to the database, and close the keyboard and lose focus.
     * @param v The given view.
     * @param keyCode The code of the pressed key.
     * @param event The event (key was pressed).
     * @return Returns a boolean.
     */

    private boolean onKeyEnter(View v, int keyCode, KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            String stringCustomInput = editTextCustomInput.getText().toString();
            int customInput = 0;
            try {
                customInput = Integer.parseInt(stringCustomInput);
                if( customInput <= 0) {
                    textLayoutLCustomInput.setError(" ");
                    textLayoutLCustomInput.setErrorIconDrawable(0);
                    return false;
                }
                textLayoutLCustomInput.setError(null);
            } catch (Exception e) {
                textLayoutLCustomInput.setError(" ");
                textLayoutLCustomInput.setErrorIconDrawable(0);
            }
            juoViewModel.insertIntake(new IntakeEntity(customInput));
            MainActivity.this.hideSoftKeyboard(v);
            editTextCustomInput.clearFocus();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            editTextCustomInput.clearFocus();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        getWeather();
    }

    private int loadHydrationGoal() {
        int sharedGoal = sharedPreferences.getInt(SettingsActivity.SHARED_GOAL, 999999);

        if (sharedGoal == 999999) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.main_activity_dialog_title))
                    .setMessage(getString(R.string.main_activity_dialog_content))
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getString(R.string.dialog_ok), (dialog, which) -> {
                        dialog.cancel();
                        Intent intent = new Intent(this, SettingsActivity.class);
                        intent.putExtra(EXTRA_IS_FIRST_START_UP, true);
                        startActivity(intent);
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return 999999;
        }

        return sharedGoal;
    }

    /**
     * Method to get the weather.
     */
    public void getWeather() {
        String location = sharedPreferences.getString(LocationActivity.SHARED_LOCATION, null);
        String weatherUrl = API_URL + location;

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
        textViewWeatherIcon.setText(getString(R.string.not_available));
        textViewCity.setText((getString(R.string.location_not_found)));
        textViewTemperature.setText(getString(R.string.not_available));
    }

    /**
     * Method to update the weather UI (text and icon) when weather request is successful.
     * @param location String containing the name of the location.
     * @param temperature Double containing the temperature value.
     * @param humidity String containing humidity value.
     * @param weatherId Integer containing the weather ID.
     */
    private void updateWeatherUI(String location, double temperature, String humidity, int weatherId) {
        if (temperature > 24f) {
            textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 100));
        } else if (temperature > 30f) {
            textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 250));
        } else if (temperature > 35f) {
            textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 500));
        } else if (temperature > 40f) {
            textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 1000));
        }

        textViewCity.setText(location.replaceAll("\\+", " "));
        textViewTemperature.setText(getString(R.string.text_view_temperature,
                temperature, humidity));

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
        hydrationGoal = loadHydrationGoal();

        circularProgressBar.setProgressMax((float) hydrationGoal);

        int sharedButtonTopStart = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_TOP_START, 250);
        mainActivityButtonTopStart.setText(String.valueOf(sharedButtonTopStart));

        int sharedButtonTopEnd = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_TOP_END, 500);
        mainActivityButtonTopEnd.setText(String.valueOf(sharedButtonTopEnd));

        int sharedButtonBottomStart = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_BOTTOM_START, 100);
        mainActivityButtonBottomStart.setText(String.valueOf(sharedButtonBottomStart));

        int sharedButtonBottomEnd = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_BOTTOM_END, 750);
        mainActivityButtonBottomEnd.setText(String.valueOf(sharedButtonBottomEnd));
    }

    /**
     * Method to initialise all views needed by the activity.
     */
    private void initialiseAll() {
        //Intake view
        textViewIntake = findViewById(R.id.intakeText);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        textViewExtraIntake = findViewById(R.id.textViewExtraIntake);
        //Weather views
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewWeatherIcon = findViewById(R.id.textViewWeatherIcon);
        textViewCity = findViewById(R.id.textViewCity);
        //Button views
        mainActivityButtonTopStart = findViewById(R.id.mainActivityButtonTopStart);
        mainActivityButtonTopEnd = findViewById(R.id.mainActivityButtonTopEnd);
        mainActivityButtonBottomStart = findViewById(R.id.mainActivityButtonBottomStart);
        mainActivityButtonBottomEnd = findViewById(R.id.mainActivityButtonBottomEnd);
        //Custom input views
        editTextCustomInput = findViewById(R.id.editTextCustomInput);
        textLayoutLCustomInput = findViewById(R.id.textLayoutCustomInput);
        //ViewModel
        juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);
        //SharedPreferences
        sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);
    }

    /**
     * Custom private OnClickListener class for intake buttons.
     * @author Christopher Mohan Romano
     * @version 1.0
     */
    private class IntakeButtonClick implements View.OnClickListener {
        /**
         * Custom OnClick method.
         * @param view Parameter containing the pressed view.
         */
        @Override
        public void onClick(View view) {
            int intake = 0;
            if (view.getId() == R.id.mainActivityButtonTopStart) {
                intake = Integer.parseInt(mainActivityButtonTopStart.getText().toString());
            } else if (view.getId() == R.id.mainActivityButtonTopEnd) {
                intake = Integer.parseInt(mainActivityButtonTopEnd.getText().toString());
            } else if (view.getId() == R.id.mainActivityButtonBottomStart) {
                intake = Integer.parseInt(mainActivityButtonBottomStart.getText().toString());
            } else if (view.getId() == R.id.mainActivityButtonBottomEnd) {
                intake = Integer.parseInt(mainActivityButtonBottomEnd.getText().toString());
            }
            juoViewModel.insertIntake(new IntakeEntity(intake));
        }
    }

    /**
     * Method to hide the keyboard.
     * @param view Parameter containing the given view.
     */
    private void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            //means the drawer is open
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}