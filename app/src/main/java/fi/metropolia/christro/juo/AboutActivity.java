package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private static final String TAG = "Selected Menu Item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textViewAbout = findViewById(R.id.textViewAbout);


        textViewAbout.setMovementMethod(new ScrollingMovementMethod());

        //navigation
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationViewAbout);
        toolbar = findViewById(R.id.toolbarAbout);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
        }
        navigationView.bringToFront();

        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> {
            drawer.openDrawer(GravityCompat.START);
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: " + item.getItemId());
                //item = navigationView.getCheckedItem();
                Intent intent;
                //Context context = getApplicationContext();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        intent = new Intent(AboutActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_mood:
                        intent = new Intent(AboutActivity.this, MoodActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_history:
                        intent = new Intent(AboutActivity.this, History.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_about:
                        break;

                    case R.id.nav_location:
                        intent = new Intent(AboutActivity.this, LocationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        Toast.makeText(AboutActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        //intent = new Intent(context,Profile.class);
                        break;

                    case R.id.nav_settings:
                        intent = new Intent(AboutActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}