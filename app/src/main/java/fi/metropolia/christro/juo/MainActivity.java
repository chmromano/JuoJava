package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity{

    private IntakeInputViewModel intakeInputViewModel;

    private IntakeInputRepository repository;

    private CircularProgressBar circularProgressBar;

    private TextView textView;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private static final String TAG = "Selected Menu Item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //navigation
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //item = navigationView.getCheckedItem();
                Intent intent;
                Context context = getApplicationContext();
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this,"Home",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_mood:
                        Toast.makeText(MainActivity.this,"Mood",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, MoodActivity2.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_history:
                        Toast.makeText(MainActivity.this,"Charts",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, History.class);
                        Log.d(TAG, "onNavigationItemSelected: history");
                        startActivity(intent);
                        break;

                    case R.id.nav_about:
                        Toast.makeText(MainActivity.this,"About",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, About.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_help:
                        Toast.makeText(MainActivity.this,"Help",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this, Help.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_profile:
                        Toast.makeText(MainActivity.this,"Profile",Toast.LENGTH_SHORT).show();
                        //intent = new Intent(context,Profile.class);
                        break;

                    case R.id.nav_settings:
                        Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_SHORT).show();
                        //intent = new Intent(context, Settings.class);
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });





        ImageButton menuButton = findViewById(R.id.menu_icon);
        menuButton.setOnClickListener(view -> {
            drawer.openDrawer(GravityCompat.START);
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ////
        repository = new IntakeInputRepository(this.getApplication());

        textView = findViewById(R.id.intakeText);
        circularProgressBar = findViewById(R.id.circularProgressBar);

        intakeInputViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(IntakeInputViewModel.class);

        final Observer<Integer> dailyTotalObserver = newTotal -> {
            // Update the UI, in this case, a TextView.
            textView.setText(String.valueOf(newTotal));

            if(newTotal != null) {
                circularProgressBar.setProgressWithAnimation(newTotal, (long) 300);
            }
        };

        intakeInputViewModel.getDailyTotal().observe(this, dailyTotalObserver);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(view -> {
            repository.insert(new IntakeInput(400));
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            repository.insert(new IntakeInput(250));
        });
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            //means the drawer is open
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

}