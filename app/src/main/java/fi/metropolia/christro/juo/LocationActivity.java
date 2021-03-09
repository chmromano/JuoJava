package fi.metropolia.christro.juo;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Location activity of the application.
 * <p>
 * Itale Tabaksmane - Implemented navigation menu and all related methods.
 *
 * @author Taranath Pokhrel
 * @author Itale Tabaksmane
 * @version 1.0
 */
/*
We understand that implements at class level is not ideal and that implementing a method would have
been better, but we knew too late and couldn't refactor and test all functionality on time, so have
to turn in with this implementation.
 */
public class LocationActivity extends AppCompatActivity implements LocationListener {

    public static final String SHARED_LOCATION = "fi.metropolia.christro.juo.SHARED_LOCATION";

    private String location;

    /**
     * onCreate() method creates the activity.
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        updateUI();

        //When enter is pressed
        findViewById(R.id.editTextLocation).setOnKeyListener(this::onKeyEnter);

        findViewById(R.id.imageButtonLocation).setOnClickListener((view) -> {
            location = getCurrentLocation();
            ((TextInputEditText) findViewById(R.id.editTextLocation)).setText(location);
        });

        findViewById(R.id.buttonLocationCancel).setOnClickListener((view) -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.buttonLocationSubmit).setOnClickListener((view) -> {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            location = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.editTextLocation)).getText()).toString();
            sharedPreferencesEditor.putString(SHARED_LOCATION, location);
            sharedPreferencesEditor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        /*
        This code is used to implement the navigation menu.
        https://www.youtube.com/watch?v=HwYENW0RyY4
        https://www.youtube.com/watch?v=fGcMLu1GJEc
        https://www.youtube.com/watch?v=zYVEMCiDcmY
        https://www.youtube.com/watch?v=bjYstsO1PgI
        https://www.youtube.com/watch?v=lt6xbth-yQo
         */
        DrawerLayout drawerLayoutLocation = findViewById(R.id.drawerLayoutLocation);
        Toolbar toolbarLocation = findViewById(R.id.toolbarLocation);
        NavigationView navigationViewLocation = findViewById(R.id.navigationViewLocation);
        setSupportActionBar(toolbarLocation);
        if (savedInstanceState == null) {
            navigationViewLocation.setCheckedItem(R.id.nav_location);
        }

        // Without this statement many devices will not show the menu as clickable
        navigationViewLocation.bringToFront();
        // making a menu icon click open the side navigation drawer
        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> drawerLayoutLocation.openDrawer(GravityCompat.START));
        // using animation whenever the menu opens, by swiping or clicking
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutLocation,
                toolbarLocation, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutLocation.addDrawerListener(toggle);
        toggle.syncState();

        // creates an intent for the appropriate activity by matching with item ID
        navigationViewLocation.setNavigationItemSelectedListener(item -> {
            Intent intent = null;

            if (item.getItemId() == R.id.nav_home) {
                intent = new Intent(LocationActivity.this, MainActivity.class);
            } else if (item.getItemId() == R.id.nav_history) {
                intent = new Intent(LocationActivity.this, HistoryActivity.class);
            } else if (item.getItemId() == R.id.nav_mood) {
                intent = new Intent(LocationActivity.this, MoodListActivity.class);
            } else if (item.getItemId() == R.id.nav_settings) {
                intent = new Intent(LocationActivity.this, SettingsActivity.class);
            } else if (item.getItemId() == R.id.nav_about) {
                intent = new Intent(LocationActivity.this, AboutActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
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
    //https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
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
        //https://stackoverflow.com/questions/43862079/how-to-get-city-name-using-latitude-and-longitude-in-android
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 10);
            Log.d("LOCATION", String.valueOf(addresses));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //https://stackoverflow.com/questions/8661857/android-reverse-geocoding-getlocality-returns-often-null
        if (addresses != null && addresses.size() > 0) {
            for (Address cityName : addresses) {
                if (cityName.getLocality() != null && cityName.getLocality().length() > 0) {
                    return cityName.getLocality();
                } else if (cityName.getSubLocality() != null && cityName.getSubLocality().length() > 0) {
                    return cityName.getSubLocality();
                }
            }
        }
        return null;
    }

    /**
     * Updates the UI.
     */
    private void updateUI() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);
        location = sharedPreferences.getString(SHARED_LOCATION, "No location set");
        ((TextInputEditText) findViewById(R.id.editTextLocation)).setText(location);
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
            hideSoftKeyboard(v);
            return true;
        }
        return false;
    }

    /**
     * Method to hide the keyboard.
     *
     * @param view Parameter containing the given view.
     */
    private void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        if (((DrawerLayout) findViewById(R.id.drawerLayoutMainActivity)).isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            ((DrawerLayout) findViewById(R.id.drawerLayoutMainActivity)).closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}