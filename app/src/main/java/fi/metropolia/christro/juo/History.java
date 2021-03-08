package fi.metropolia.christro.juo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;
import fi.metropolia.christro.juo.database.MoodEntity;


public class History extends AppCompatActivity {

    //MPAndroid chart library: https://github.com/PhilJay/MPAndroidChart
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries;
    private static final String TAG = "fi.metropolia.christro.juo.History";
    private DrawerLayout drawerLayoutHistory;
    private NavigationView navigationViewHistory;
    private Toolbar toolbarHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Hooking widgets to local variables
        toolbarHistory = findViewById(R.id.toolbarHistory);
        navigationViewHistory = findViewById(R.id.navigationViewHistory);
        drawerLayoutHistory = findViewById(R.id.drawerLayoutHistory);

        setSupportActionBar(toolbarHistory);

        // Calling the instance of JuoViewModel
        JuoViewModel juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);
        // This function constructs the bar char using live data about water intakes
        getData(juoViewModel);

        // A safety measure for no records of intakes
        if (barEntries == null) {
            Log.d(TAG, "onCreateCharts: empty bar chart");
        }

        ImageButton buttonHistoryList = findViewById(R.id.buttonHistoryList);
        buttonHistoryList.setOnClickListener((view) -> {
            Intent intent = new Intent(History.this, IntakeListview.class);
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
        setSupportActionBar(toolbarHistory);
        if (savedInstanceState == null) {
            navigationViewHistory.setCheckedItem(R.id.nav_history);
        }

        // Without this statement many devices will not show the menu as clickable
        navigationViewHistory.bringToFront();
        // making a menu icon click open the side navigation drawer
        ImageButton buttonNavigationMenu = findViewById(R.id.buttonNavigationMenu);
        buttonNavigationMenu.setOnClickListener(view -> drawerLayoutHistory.openDrawer(GravityCompat.START));
        // using animation whenever the menu opens, by swiping or clicking
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutHistory, toolbarHistory,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutHistory.addDrawerListener(toggle);
        toggle.syncState();

        // creates an intent for the appropriate activity by matching with item ID
        navigationViewHistory.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    intent = new Intent(History.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_history:
                    break;
                case R.id.nav_mood:
                    intent = new Intent(History.this, MoodListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_location:
                    intent = new Intent(History.this, LocationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_settings:
                    intent = new Intent(History.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_about:
                    intent = new Intent(History.this, AboutActivity.class);
                    startActivity(intent);
                    break;
            }
            drawerLayoutHistory.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    public void getData(JuoViewModel juoViewModel) {
        barEntries = new ArrayList<BarEntry>();
        Date date = new Date();
        String sDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        Log.d(TAG, "getData: " + sDate);

       LiveData<String> data= juoViewModel.getLatestIntake();

        // Using the observe method to get the live data from the database
        juoViewModel.getAllIntakeInputs().observe(this, new Observer<List<IntakeEntity>>() {
            @Override
            // The chart will update every time a change in records will occur
            public void onChanged(List<IntakeEntity> intakeEntities) {
                for (IntakeEntity intake : intakeEntities) {
                    // Taking only the intakes from today
                    if (intake.getDate().equals(sDate)) {
                        Log.d(TAG, "onChanged: " + intake.getTime() + "," + intake.getAmount());
                        // data manipulation for the chart
                        String intakeTime = intake.getTime();
                        char[] hour = new char[3];
                        intakeTime.getChars(0, 2, hour, 0);
                        String sHour = String.valueOf(hour);
                        //
                        float x = Float.valueOf(sHour);
                        Log.d(TAG, "onChanged: x value is" + x);
                        float y = intake.getAmount();
                        barEntries.add(new BarEntry(x, y));
                    }
                }
                Log.d(TAG, "onChanged: barentries" + barEntries);
                barDataSet = new BarDataSet(barEntries, "Day Data set");
                barChart = findViewById(R.id.barChartDay);
                barData = new BarData(barDataSet);
                barChart.setData(barData);
                barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            }

        });

    }

    /**
     * Method overrides normal onBackPressed() in case the drawer menu is open.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayoutHistory.isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            drawerLayoutHistory.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(History.this,MainActivity.class);
            startActivity(intent);
        }
    }

}