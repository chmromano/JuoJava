package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocalWeatherActivity extends AppCompatActivity implements LocationListener {

    String url;
    Button btnGo;
    TextView temperature, humidity, pressure, windSpeed, city;
    EditText locationInput;
    double latitude;
    double longitude;

    private static final String TAG = "WEATHER";

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=35be7f414814f513a3bdf6ce70e1fcec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localweather);

        btnGo = findViewById(R.id.btnGo);
        temperature = findViewById(R.id.txtTemperature);
        humidity = findViewById(R.id.txtHumidity);
        pressure = findViewById(R.id.txtPressure);
        windSpeed = findViewById(R.id.txtWind);
        locationInput = findViewById(R.id.edtTxtLocation);
        city = findViewById(R.id.city);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        latitude = bestLocation.getLatitude();
        longitude = bestLocation.getLongitude();
        Log.d(TAG, "lon " + longitude + " lat " + latitude);
        getWeather(getLocationByCordinates(latitude, longitude));

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Btn clicked");

                hideSoftKeyboard(LocalWeatherActivity.this);
                Editable location = locationInput.getText();

                locationInput.clearFocus();

                getWeather(location.toString());

                Log.d(TAG, "current location is" + location.toString());

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "lon " + longitude + " lat " + latitude);
         //getWeather(getLocationByCordinates(latitude,longitude));
    }

    public String getLocationByCordinates(double lat, double lng) {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
            Log.d("LOCATION", String.valueOf(addresses));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            String locality = addresses.get(0).getLocality();
            Log.d("LOCATION", locality);
            return locality;
        }
        return null;

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LocalWeatherActivity.this, "Location Granted", Toast.LENGTH_SHORT).show();
                //  getWeather(getLocationByCordinates(longitude,latitude));

            } else {
                Toast.makeText(LocalWeatherActivity.this, "Location not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void getWeather(String location) {

        url = API_URL + "&q=" + location;

        Log.d(TAG, "URL " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //callling api

                Log.d(TAG, "RESPONSE " + response);
                try {

                    JSONObject responseObject = new JSONObject(response);
                    //find country
                    JSONObject main = responseObject.getJSONObject("main");

                    String temp = main.getString("temp");
                    String humidityValue = main.getString("humidity");
                    String pressureValue = main.getString("pressure");

                    String windSpeedValue = responseObject.getJSONObject("wind").getString("speed");
                    city.setText(location);
                    temperature.setText("Temperature " + temp + " \u2103");
                    humidity.setText("Humidity " + humidityValue + " %");
                    pressure.setText("Pressure " + pressureValue + " hpa");
                    windSpeed.setText("Wind " + windSpeedValue + " m/s");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(LocalWeatherActivity.this);
        requestQueue.add(stringRequest);

    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


}