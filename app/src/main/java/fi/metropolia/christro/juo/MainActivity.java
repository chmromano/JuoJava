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
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

public class MainActivity extends AppCompatActivity{

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
    //ViewModel
    private JuoViewModel juoViewModel;
    //SharedPreferences
    private SharedPreferences sharedPreferences;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private static final String TAG = "Selected Menu Item";

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
        ////
        //repository = new IntakeInputRepository(this.getApplication());
        //customButtonList = loadButtonList();

        //textView = findViewById(R.id.intakeText);
        circularProgressBar = findViewById(R.id.circularProgressBar);

        JuoViewModel juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        final Observer<Integer> dailyTotalObserver = newTotal -> {
            // Update the UI, in this case, a TextView.
            if (newTotal != null) {
                textViewIntake.setText(getString(R.string.main_activity_intake, newTotal, hydrationGoal));
            } else {
                textViewIntake.setText(getString(R.string.main_activity_intake,0, 0));
            }

            if (newTotal != null) {
                circularProgressBar.setProgressWithAnimation(newTotal, (long) 300);
            } else {
                circularProgressBar.setProgress(0);
            }
        };

        juoViewModel.getDailyTotal().observe(this, dailyTotalObserver);

        textViewWeatherIcon.setOnClickListener((view) -> {
            String location = sharedPreferences.getString(LocationActivity.SHARED_LOCATION, null);
            getWeather(location);
        });

        IntakeButtonClick intakeButtonClick = new IntakeButtonClick();
        mainActivityButtonTopStart.setOnClickListener(intakeButtonClick);
        mainActivityButtonTopEnd.setOnClickListener(intakeButtonClick);
        mainActivityButtonBottomStart.setOnClickListener(intakeButtonClick);
        mainActivityButtonBottomEnd.setOnClickListener(intakeButtonClick);

        Intent returnIntent = new Intent(this, LocationActivity.class);
        findViewById(R.id.buttonMoodInput).setOnClickListener((view) -> startActivity(returnIntent));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
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

    public void getWeather(String location) {

        if (location == null) {
            textViewCity.setText(getString(R.string.location_not_found));
            return;
        }
        String weatherUrl = API_URL + location;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, weatherUrl, response -> {
            try {
                JSONObject responseObject = new JSONObject(response);
                //find country
                JSONObject jsonMain = responseObject.getJSONObject("main");
                JSONArray jsonWeather = responseObject.getJSONArray("weather");
                JSONObject jsonWeatherId = jsonWeather.getJSONObject(0);

                String stringTemperature = jsonMain.getString("temp");
                String humidity = jsonMain.getString("humidity");
                String stringWeatherId = jsonWeatherId.getString("id");

                double temperature = 0.0;
                try {
                    temperature = Double.parseDouble(stringTemperature);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if(temperature > 24f){
                    textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 100));
                } else if(temperature > 30f){
                    textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 250));
                }else if(temperature > 35f){
                    textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 500));
                }else if (temperature > 40f){
                    textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 1000));
                }

                int weatherId = 0;
                try {
                    weatherId = Integer.parseInt(stringWeatherId);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                textViewCity.setText(location.replaceAll("\\+", " "));
                textViewTemperature.setText(getString(R.string.text_view_temperature, temperature));
                textViewHumidity.setText(getString(R.string.text_view_humidity, humidity));

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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void updateUI() {
        hydrationGoal = loadHydrationGoal();
        circularProgressBar.setProgressMax((float) hydrationGoal);
        int sharedButtonTopStart = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_TOP_START, 250);
        int sharedButtonTopEnd = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_TOP_END, 500);
        int sharedButtonBottomStart = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_BOTTOM_START, 100);
        int sharedButtonBottomEnd = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_BOTTOM_END, 750);
        mainActivityButtonTopStart.setText(String.valueOf(sharedButtonTopStart));
        mainActivityButtonTopEnd.setText(String.valueOf(sharedButtonTopEnd));
        mainActivityButtonBottomStart.setText(String.valueOf(sharedButtonBottomStart));
        mainActivityButtonBottomEnd.setText(String.valueOf(sharedButtonBottomEnd));
    }

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
        //ViewModel
        juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);
        //SharedPreferences
        sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);
    }

    private class IntakeButtonClick implements View.OnClickListener {

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