package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
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

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Location activity of the application.
 *
 * @author Taranath Pokhrel
 * @version 1.0
 */
//Itale Tabaksmane - Implemented navigation menu and all related methods.
public class LocationActivity extends AppCompatActivity implements LocationListener {

    public static final String SHARED_LOCATION = "fi.metropolia.christro.juo.SHARED_LOCATION";

    private String location;
    private Intent intent;

    private TextInputEditText editTextLocation;
    private ImageButton imageButtonLocation;
    private Button buttonLocationCancel;
    private Button buttonLocationSubmit;

    private DrawerLayout drawerLayoutLocation;
    private Toolbar toolbarLocation;
    private NavigationView navigationViewLocation;

    private SharedPreferences sharedPreferences;

    /**
     * onCreate() method creates the activity.
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @SuppressLint("NonConstantResourceId")
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
            location = Objects.requireNonNull(editTextLocation.getText()).toString();
            sharedPreferencesEditor.putString(SHARED_LOCATION, location);
            sharedPreferencesEditor.apply();
            startActivity(intent);
        });

        //This code is used to implement the navigation menu.
        setSupportActionBar(toolbarLocation);
        if (savedInstanceState == null) {
            navigationViewLocation.setCheckedItem(R.id.nav_location);
        }
        navigationViewLocation.bringToFront();

        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> drawerLayoutLocation.openDrawer(GravityCompat.START));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutLocation,
                toolbarLocation, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutLocation.addDrawerListener(toggle);
        toggle.syncState();

        navigationViewLocation.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    intent = new Intent(LocationActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_history:
                    intent = new Intent(LocationActivity.this, History.class);
                    startActivity(intent);
                    break;
                case R.id.nav_mood:
                    intent = new Intent(LocationActivity.this, MoodListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_location:
                    break;
                case R.id.nav_settings:
                    intent = new Intent(LocationActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_about:
                    intent = new Intent(LocationActivity.this, AboutActivity.class);
                    startActivity(intent);
                    break;
            }
            drawerLayoutLocation.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * Gets current coordinates. Requires Google Play services.
     *
     * @return String of current location.
     */
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

    /**
     * Gets current location based on coordinates. Requires Google Play services.
     *
     * @param latitude  Double containing latitude.
     * @param longitude Double containing longitude.
     * @return City name as String or null.
     */
    public String getLocationByCoordinates(double latitude, double longitude) {
        /*
        PLEASE READ!
        Geocoder can be buggy on Android Studio emulator. If it does not work please test on a real
        device. On a real device it always worked!
         */
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

    /**
     * Updates the UI.
     */
    private void updateUI() {
        location = sharedPreferences.getString(SHARED_LOCATION, "No location set");
        editTextLocation.setText(location);
    }

    /**
     * Initialise all views.
     */
    private void initialiseAll() {
        editTextLocation = findViewById(R.id.editTextLocation);
        imageButtonLocation = findViewById(R.id.imageButtonLocation);
        buttonLocationCancel = findViewById(R.id.buttonLocationCancel);
        buttonLocationSubmit = findViewById(R.id.buttonLocationSubmit);
        sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);
        intent = new Intent(this, MainActivity.class);
        //Navigation views
        drawerLayoutLocation = findViewById(R.id.drawerLayoutLocation);
        navigationViewLocation = findViewById(R.id.navigationViewLocation);
        toolbarLocation = findViewById(R.id.toolbarLocation);
    }

    /**
     * Request permission for GPS.
     *
     * @param requestCode  Request code.
     * @param permissions  Requested permission.
     * @param grantResults Permission granted or denied.
     */
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

    /**
     * Must implement methods but never used.
     *
     * @param location Unused parameter.
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    /**
     * Must implement methods but never used.
     *
     * @param provider Unused parameter.
     */
    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    /**
     * Must implement methods but never used.
     *
     * @param provider Unused parameter.
     */
    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

    /**
     * Must implement methods but never used.
     *
     * @param hasCapture Unused parameter.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    /**
     * Method overrides normal onBackPressed() in case the drawer menu is open.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayoutLocation.isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            drawerLayoutLocation.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}