package fi.metropolia.christro.juo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * About activity of the application.
 * <p>
 * Itale Tabaksmane - Implemented navigation menu and all related methods.
 *
 * @author Christopher Mohan Romano
 * @author Itale Tabaksmane
 * @version 1.0
 */
public class AboutActivity extends AppCompatActivity {

    /**
     * onCreate() method creates the activity.
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ((TextView) findViewById(R.id.textViewAbout)).setMovementMethod(new ScrollingMovementMethod());

        /*
        This code is used to implement the navigation menu.
        https://www.youtube.com/watch?v=HwYENW0RyY4
        https://www.youtube.com/watch?v=fGcMLu1GJEc
        https://www.youtube.com/watch?v=zYVEMCiDcmY
        https://www.youtube.com/watch?v=bjYstsO1PgI
        https://www.youtube.com/watch?v=lt6xbth-yQo
         */
        DrawerLayout drawerLayoutAboutActivity = findViewById(R.id.drawerLayoutAbout);
        NavigationView navigationViewAbout = findViewById(R.id.navigationViewAbout);
        Toolbar toolbarAbout = findViewById(R.id.toolbarAbout);
        setSupportActionBar(toolbarAbout);
        if (savedInstanceState == null) {
            navigationViewAbout.setCheckedItem(R.id.nav_about);
        }

        // Without this statement many devices will not show the menu as clickable
        navigationViewAbout.bringToFront();
        // making a menu icon click open the side navigation drawer
        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> drawerLayoutAboutActivity.openDrawer(GravityCompat.START));
        // using animation whenever the menu opens, by swiping or clicking
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutAboutActivity,
                toolbarAbout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutAboutActivity.addDrawerListener(toggle);
        toggle.syncState();

        // creates an intent for the appropriate activity by matching with item ID
        navigationViewAbout.setNavigationItemSelectedListener(item -> {

            Intent intent = null;

            if (item.getItemId() == R.id.nav_home) {
                intent = new Intent(AboutActivity.this, MainActivity.class);
            } else if (item.getItemId() == R.id.nav_history) {
                intent = new Intent(AboutActivity.this, History.class);
            } else if (item.getItemId() == R.id.nav_mood) {
                intent = new Intent(AboutActivity.this, MoodListActivity.class);
            } else if (item.getItemId() == R.id.nav_location) {
                intent = new Intent(AboutActivity.this, LocationActivity.class);
            } else if (item.getItemId() == R.id.nav_settings) {
                intent = new Intent(AboutActivity.this, SettingsActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
            }

            drawerLayoutAboutActivity.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * Method overrides normal onBackPressed() in case the drawer menu is open.
     */
    @Override
    public void onBackPressed() {
        if (((DrawerLayout) findViewById(R.id.drawerLayoutAbout)).isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            ((DrawerLayout) findViewById(R.id.drawerLayoutAbout)).closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}