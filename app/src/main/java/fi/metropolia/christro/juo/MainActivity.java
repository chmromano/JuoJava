package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=35be7f414814f513a3bdf6ce70e1fcec&q=";
    public static final String PREFERENCE_FILE = "fi.metropolia.christro.juo";
    public static final String EXTRA_IS_FIRST_START_UP = "fi.metropolia.christro.juo.EXTRA_IS_FIRST_START_UP";
    private int hydrationGoal;
    private String location;

    //Intake views
    private CircularProgressBar circularProgressBar;
    private TextView textViewIntake;
    private TextView textViewExtraIntake;
    //Weather views
    private TextView textViewTemperature;
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
    //Weather singleton
    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseAll();
        updateUI();

        final Observer<Integer> dailyTotalObserver = newTotal -> {
            // Update the UI, in this case, a TextView.
            if (newTotal != null) {
                textViewIntake.setText(getString(R.string.main_activity_intake, newTotal, hydrationGoal));
            } else {
                textViewIntake.setText(getString(R.string.main_activity_intake, 0, 0));
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
            weather.getWeather(location, this);
            updateWeatherUI(weather);
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

    private void updateWeatherUI(Weather weather) {
        if (weather.getHumidity() == null) {
            textViewCity.setText(getString(R.string.location_not_found));
            return;
        }


        if (weather.getTemperature() > 24f) {
            textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 100));
        } else if (weather.getTemperature() > 30f) {
            textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 250));
        } else if (weather.getTemperature() > 35f) {
            textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 500));
        } else if (weather.getTemperature() > 40f) {
            textViewExtraIntake.setText(getString(R.string.main_activity_extra_intake, 1000));
        }

        location = sharedPreferences.getString(LocationActivity.SHARED_LOCATION, null);
        textViewCity.setText(location.replaceAll("\\+", " "));
        textViewTemperature.setText(getString(R.string.text_view_temperature,
                weather.getTemperature(), weather.getHumidity()));

        if (weather.getWeatherId() >= 200 && weather.getWeatherId() <= 232) {
            textViewWeatherIcon.setText(getString(R.string.thunderstorm));
        } else if (weather.getWeatherId() >= 300 && weather.getWeatherId() <= 321) {
            textViewWeatherIcon.setText(getString(R.string.drizzle));
        } else if (weather.getWeatherId() >= 500 && weather.getWeatherId() <= 531) {
            textViewWeatherIcon.setText(getString(R.string.rain));
        } else if (weather.getWeatherId() >= 600 && weather.getWeatherId() <= 622) {
            textViewWeatherIcon.setText(getString(R.string.snow));
        } else if (weather.getWeatherId() >= 700 && weather.getWeatherId() <= 781) {
            textViewWeatherIcon.setText(getString(R.string.fog));
        } else if (weather.getWeatherId() == 800) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if (hour < 6 || hour > 18) {
                textViewWeatherIcon.setText(getString(R.string.clear_night));
            } else {
                textViewWeatherIcon.setText(getString(R.string.clear_day));
            }
        } else if (weather.getWeatherId() >= 801 && weather.getWeatherId() <= 804) {
            textViewWeatherIcon.setText(getString(R.string.clouds));
        } else {
            textViewWeatherIcon.setText(getString(R.string.not_available));
        }
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
        //Weather singleton
        weather = Weather.getInstance();
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
}