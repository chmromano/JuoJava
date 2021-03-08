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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import fi.metropolia.christro.juo.database.IntakeEntity;

/**
 * Location activity of the application.
 *
 * @author Taranath Pokhrel
 * @author Itale Tabaksmane
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

        //When enter is pressed
        editTextLocation.setOnKeyListener((v, keyCode, event) -> onKeyEnter(v, keyCode, event));

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

        /*
        This code is used to implement the navigation menu.
        https://www.youtube.com/watch?v=HwYENW0RyY4
        https://www.youtube.com/watch?v=fGcMLu1GJEc
        https://www.youtube.com/watch?v=zYVEMCiDcmY
        https://www.youtube.com/watch?v=bjYstsO1PgI
        https://www.youtube.com/watch?v=lt6xbth-yQo
         */
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
        if (drawerLayoutLocation.isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            drawerLayoutLocation.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}