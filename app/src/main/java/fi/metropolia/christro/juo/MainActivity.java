package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=35be7f414814f513a3bdf6ce70e1fcec&q=";

    public static final String PREFERENCE_FILE = "fi.metropolia.christro.juo";
    public static final String SHARED_LOCATION = "fi.metropolia.christro.juo.SHARED_LOCATION";

    public static final String EXTRA_IS_FIRST_START_UP = "fi.metropolia.christro.juo.EXTRA_IS_FIRST_START_UP";

    private int hydrationGoal;

    private double latitude;
    private double longitude;

    private String weatherUrl;

    //Intake views
    private CircularProgressBar circularProgressBar;
    private TextView textViewIntake;
    //Weather views
    private TextView textViewTemperature;
    private TextView textViewHumidity;
    private TextView textViewWeatherIcon;
    private TextView textViewCity;
    //Button views
    private Button mainActivityButtonTopStart;
    private Button mainActivityButtonTopEnd;
    private Button mainActivityButtonBottomStart;
    private Button mainActivityButtonBottomEnd;

    //ViewModel
    private JuoViewModel juoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseViews();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);

        hydrationGoal = loadHydrationGoal();

        int sharedButtonTopStart = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_TOP_START, 250);
        int sharedButtonTopEnd = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_TOP_END, 500);
        int sharedButtonBottomStart = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_BOTTOM_START, 100);
        int sharedButtonBottomEnd = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_BOTTOM_END, 750);

        mainActivityButtonTopStart.setText(String.valueOf(sharedButtonTopStart));
        mainActivityButtonTopEnd.setText(String.valueOf(sharedButtonTopEnd));
        mainActivityButtonBottomStart.setText(String.valueOf(sharedButtonBottomStart));
        mainActivityButtonBottomEnd.setText(String.valueOf(sharedButtonBottomEnd));

        circularProgressBar.setProgressMax((float) hydrationGoal);

        final Observer<Integer> dailyTotalObserver = newTotal -> {
            // Update the UI, in this case, a TextView.
            if (newTotal != null) {
                textViewIntake.setText(String.valueOf(newTotal));
            } else {
                textViewIntake.setText(String.valueOf(0));
            }

            if (newTotal != null) {
                circularProgressBar.setProgressWithAnimation(newTotal, (long) 300);
            } else {
                circularProgressBar.setProgress(0);
            }
        };

        juoViewModel.getDailyTotal().observe(this, dailyTotalObserver);

        textViewWeatherIcon.setOnClickListener((view) -> {

            getWeather(getLocationByCoordinates(latitude, longitude));
        });

        IntakeButtonClick intakeButtonClick = new IntakeButtonClick();
        mainActivityButtonTopStart.setOnClickListener(intakeButtonClick);
        mainActivityButtonTopEnd.setOnClickListener(intakeButtonClick);
        mainActivityButtonBottomStart.setOnClickListener(intakeButtonClick);
        mainActivityButtonBottomEnd.setOnClickListener(intakeButtonClick);
    }

    private int loadHydrationGoal() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);

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

    public String getLocationByCoordinates(double latitude1, double longitude1) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude1, longitude1, 10);
            Log.d("LOCATION", String.valueOf(addresses));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            for (Address cityName : addresses) {
                if (cityName.getLocality() != null && cityName.getLocality().length() > 0) {
                    return cityName.getLocality().replaceAll("\\s+", "+");
                } else if (cityName.getSubLocality() != null && cityName.getSubLocality().length() > 0) {
                    return cityName.getSubLocality().replaceAll("\\s+", "+");
                }
            }
        }
        return null;
    }

    private String getCurrentLocation() {
        Location gpsLocation = null;
        Location networkLocation = null;
        Location finalLocation;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);

        try {
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gpsLocation != null) {
            finalLocation = gpsLocation;
            latitude = finalLocation.getLatitude();
            longitude = finalLocation.getLongitude();
        } else if (networkLocation != null) {
            finalLocation = networkLocation;
            latitude = finalLocation.getLatitude();
            longitude = finalLocation.getLongitude();
        } else {
            return null;
        }

        return getLocationByCoordinates(latitude, longitude);
    }

    public void getWeather(String location) {
        if (location == null) {
            return;
        }

        weatherUrl = API_URL + location;

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Location Granted", Toast.LENGTH_SHORT).show();
                getWeather(getLocationByCoordinates(longitude, latitude));

            } else {
                Toast.makeText(MainActivity.this, "Location not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialiseViews() {
        //Intake view
        textViewIntake = findViewById(R.id.intakeText);
        circularProgressBar = findViewById(R.id.circularProgressBar);
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
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}