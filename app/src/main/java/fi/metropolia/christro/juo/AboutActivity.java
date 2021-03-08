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
 *
 * @author Christopher Mohan Romano
 * @author Itale Tabaksmane
 * @version 1.0
 */
//Itale Tabaksmane - Implemented navigation menu and all related methods.
public class AboutActivity extends AppCompatActivity {

    private TextView textViewAbout;

    private DrawerLayout drawerLayoutAboutActivity;
    private Toolbar toolbarAbout;
    private NavigationView navigationViewAbout;

    /**
     * onCreate() method creates the activity.
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textViewAbout = findViewById(R.id.textViewAbout);
        textViewAbout.setMovementMethod(new ScrollingMovementMethod());

        //This code is used to implement the navigation menu.
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
            Intent intent;
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

    /**
     * Method overrides normal onBackPressed() in case the drawer menu is open.
     */
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