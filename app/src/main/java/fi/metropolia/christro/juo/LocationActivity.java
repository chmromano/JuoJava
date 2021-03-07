package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    public static final String SHARED_LOCATION = "fi.metropolia.christro.juo.SHARED_LOCATION";

    private TextInputEditText editTextLocation;
    private ImageButton imageButtonLocation;
    private Button buttonLocationCancel;
    private Button buttonLocationSubmit;
    private SharedPreferences sharedPreferences;

    String location;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initialiseAll();
        updateUI();

        imageButtonLocation.setOnClickListener((view) -> {
            location = getCurrentLocation();
            editTextLocation.setText(location);
        });

        buttonLocationCancel.setOnClickListener((view) -> startActivity(intent));

        buttonLocationSubmit.setOnClickListener((view) -> {
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            location = editTextLocation.getText().toString();
            sharedPreferencesEditor.putString(SHARED_LOCATION, location);
            sharedPreferencesEditor.apply();
            startActivity(intent);
        });
    }

    private String getCurrentLocation() {
        Location gpsLocation = null;
        Location networkLocation = null;
        Location finalLocation;
        double longitude;
        double latitude;
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

    public String getLocationByCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 10);
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

    private void updateUI() {
        location = sharedPreferences.getString(SHARED_LOCATION, "No location set");
        editTextLocation.setText(location);
    }

    private void initialiseAll() {
        editTextLocation = findViewById(R.id.editTextLocation);
        imageButtonLocation = findViewById(R.id.imageButtonLocation);
        buttonLocationCancel = findViewById(R.id.buttonLocationCancel);
        buttonLocationSubmit = findViewById(R.id.buttonLocationSubmit);
        sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);
        intent = new Intent(this, MainActivity.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location not granted", Toast.LENGTH_SHORT).show();
            }
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