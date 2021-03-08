package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class AboutActivity extends AppCompatActivity {

    private TextView textViewAbout;

    private DrawerLayout drawerLayoutAboutActivity;
    private Toolbar toolbarAbout;
    private NavigationView navigationViewAbout;
    private static final String TAG = "Selected Menu Item";

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textViewAbout = findViewById(R.id.textViewAbout);


        textViewAbout.setMovementMethod(new ScrollingMovementMethod());

        //navigation
        drawerLayoutAboutActivity = findViewById(R.id.drawerLayoutAboutActivity);
        navigationViewAbout = findViewById(R.id.navigationViewAbout);
        toolbarAbout = findViewById(R.id.toolbarAbout);
        setSupportActionBar(toolbarAbout);
        if (savedInstanceState == null) {
            navigationViewAbout.setCheckedItem(R.id.nav_about);
        }
        navigationViewAbout.bringToFront();

        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> drawerLayoutAboutActivity.openDrawer(GravityCompat.START));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutAboutActivity, toolbarAbout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutAboutActivity.addDrawerListener(toggle);
        toggle.syncState();

        navigationViewAbout.setNavigationItemSelectedListener(item -> {
            Log.d(TAG, "onNavigationItemSelected: " + item.getItemId());
            //item = navigationView.getCheckedItem();
            Intent intent;
            //Context context = getApplicationContext();
            switch (item.getItemId()) {
                case R.id.nav_home:
                    intent = new Intent(AboutActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_history:
                    intent = new Intent(AboutActivity.this, History.class);
                    startActivity(intent);
                    break;
                case R.id.nav_mood:
                    intent = new Intent(AboutActivity.this, MoodListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_location:
                    intent = new Intent(AboutActivity.this, LocationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_settings:
                    intent = new Intent(AboutActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_about:
                    break;
            }
            drawerLayoutAboutActivity.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutAboutActivity.isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            drawerLayoutAboutActivity.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}