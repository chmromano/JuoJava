package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=35be7f414814f513a3bdf6ce70e1fcec";

    public static final String PREFERENCE_FILE = "fi.metropolia.christro.juo";
    public static final String EXTRA_IS_FIRST_START_UP = "fi.metropolia.christro.juo.EXTRA_IS_FIRST_START_UP";

    private CircularProgressBar circularProgressBar;

    private TextView textView;

    private int hydrationGoal;

    private Button mainActivityButton1;
    private Button mainActivityButton2;
    private Button mainActivityButton3;
    private Button mainActivityButton4;

    //Views needed for weather
    private TextView textViewTemperature;
    private TextView textViewHumidity;
    private TextView textViewWeatherIcon;
    private TextView textViewCity;

    private ImageButton buttonGetLocation;

    private double latitude;
    private double longitude;

    private String weatherUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hydrationGoal = loadHydrationGoal();


        //TESTING REMOVE LATER
        findViewById(R.id.imageButton).setOnClickListener((view) -> {
            Intent testIntent = new Intent(this, SettingsActivity.class);
            startActivity(testIntent);
        });
        //TESTING REMOVE LATER


        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);

        int sharedButton1 = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_1, 250);
        int sharedButton2 = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_2, 500);
        int sharedButton3 = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_3, 100);
        int sharedButton4 = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_4, 700);

        mainActivityButton1 = findViewById(R.id.mainActivityButton1);
        mainActivityButton2 = findViewById(R.id.mainActivityButton2);
        mainActivityButton3 = findViewById(R.id.mainActivityButton3);
        mainActivityButton4 = findViewById(R.id.mainActivityButton4);

        mainActivityButton1.setText(String.valueOf(sharedButton1));
        mainActivityButton2.setText(String.valueOf(sharedButton2));
        mainActivityButton3.setText(String.valueOf(sharedButton3));
        mainActivityButton4.setText(String.valueOf(sharedButton4));

        textView = findViewById(R.id.intakeText);

        circularProgressBar = findViewById(R.id.circularProgressBar);

        circularProgressBar.setProgressMax((float) hydrationGoal);

        JuoViewModel juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        final Observer<Integer> dailyTotalObserver = newTotal -> {
            // Update the UI, in this case, a TextView.
            if (newTotal != null) {
                textView.setText(String.valueOf(newTotal));
            } else {
                textView.setText(String.valueOf(0));
            }

            if (newTotal != null) {
                circularProgressBar.setProgressWithAnimation(newTotal, (long) 300);
            } else {
                circularProgressBar.setProgress(0);
            }
        };

        juoViewModel.getDailyTotal().observe(this, dailyTotalObserver);


        //For location and weather
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewWeatherIcon = findViewById(R.id.textViewWeatherIcon);
        textViewCity = findViewById(R.id.textViewCity);

        buttonGetLocation = findViewById(R.id.buttonGetLocation);

        buttonGetLocation.setOnClickListener((view) -> {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                    .PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;

            for (String provider : providers) {
                Location location = locationManager.getLastKnownLocation(provider);
                if (location == null) {
                    continue;
                }
                if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                    //Found best last known location: %s", l);
                    bestLocation = location;
                }
            }

            latitude = bestLocation.getLatitude();
            longitude = bestLocation.getLongitude();
            getWeather(getLocationByCoordinates(latitude, longitude));
        });


        mainActivityButton1.setOnClickListener(view -> {
            int intake = Integer.parseInt(mainActivityButton1.getText().toString());
            juoViewModel.insertIntake(new IntakeEntity(intake));
        });

        mainActivityButton2.setOnClickListener(view -> {
            int intake = Integer.parseInt(mainActivityButton2.getText().toString());
            juoViewModel.insertIntake(new IntakeEntity(intake));
        });

        mainActivityButton3.setOnClickListener(view -> {
            int intake = Integer.parseInt(mainActivityButton3.getText().toString());
            juoViewModel.insertIntake(new IntakeEntity(intake));
        });

        mainActivityButton4.setOnClickListener(view -> {
            int intake = Integer.parseInt(mainActivityButton4.getText().toString());
            juoViewModel.insertIntake(new IntakeEntity(intake));
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        getWeather(getLocationByCoordinates(latitude, longitude));
    }

    public String getLocationByCoordinates(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            Log.d("LOCATION", String.valueOf(addresses));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            String locality = addresses.get(0).getLocality();
            Log.d("LOCATION", locality);
            return locality.replaceAll("\\s+","+");
        }
        return null;
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

    public void getWeather(String location) {

        weatherUrl = API_URL + "&q=" + location;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, weatherUrl, response -> {
            try {
                JSONObject responseObject = new JSONObject(response);
                //find country
                JSONObject jsonMain = responseObject.getJSONObject("main");
                JSONArray jsonWeather = responseObject.getJSONArray("weather");

                String temperature = jsonMain.getString("temp");
                String humidity = jsonMain.getString("humidity");
                String weatherId = jsonWeather.getString((int) 0);

                //for(int i = 0; i < jsonWeather.length(); i++){ if }

                textViewCity.setText(location.replaceAll("\\+"," "));
                textViewTemperature.setText("Temperature " + temperature + " \u2103");
                textViewHumidity.setText("Humidity " + humidity + " %");

                //Remove
                int unicode = 0x1F325;
                String emoji = new String(Character.toChars(unicode));

                textViewWeatherIcon.setText(emoji);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
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