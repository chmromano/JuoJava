package fi.metropolia.christro.juo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;

/**
 * History activity
 * <p>
 * The activity uses MPAndroidChart API,
 * it shows the user their water intakes during the day
 * intakes are grouped by hour
 *
 * @author Itale Tabaksmane
 * @version 1.0
 */
public class History extends AppCompatActivity {

    //MPAndroid chart library: https://github.com/PhilJay/MPAndroidChart
    /**
     * BarChart object to construct and edit the bar type chart
     */
    BarChart barChart;

    /**
     * BarData object to take the data from one/several data sets into the chart
     */
    BarData barData;

    /**
     * BarDataSet holds the entries array and a label for a data set,
     */
    BarDataSet barDataSet;

    /**
     * ArrayList<BarEntry> to store a list of entries made of float x, float y values
     */
    ArrayList<BarEntry> barEntries;

    /**
     * String for log context
     */
    private static final String TAG = "fi.metropolia.christro.juo.History";

    /**
     * DrawerLayout object to control the navigation drawer
     */
    private DrawerLayout drawerLayoutHistory;

    /**
     * onCreate() method creates the activity.
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Hooking widgets to local variables
        Toolbar toolbarHistory = findViewById(R.id.toolbarHistory);
        NavigationView navigationViewHistory = findViewById(R.id.navigationViewHistory);
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
            Intent intent = null;

            if (item.getItemId() == R.id.nav_home) {
                intent = new Intent(History.this, MainActivity.class);
            } else if (item.getItemId() == R.id.nav_home) {
                intent = new Intent(History.this, MainActivity.class);
            } else if (item.getItemId() == R.id.nav_mood) {
                intent = new Intent(History.this, MoodListActivity.class);
            } else if (item.getItemId() == R.id.nav_location) {
                intent = new Intent(History.this, LocationActivity.class);
            } else if (item.getItemId() == R.id.nav_about) {
                intent = new Intent(History.this, AboutActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
            }
            drawerLayoutHistory.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * getData(juoViewModel) uses the JuoViewModel instance to get live data from the database
     * and build a bar chart based on the live data.
     *
     * @param juoViewModel The ViewModel from which to get the data.
     */
    public void getData(JuoViewModel juoViewModel) {
        barEntries = new ArrayList<>();
        String sDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        Log.d(TAG, "getData: " + sDate);

        // Using the observe method to get the live data from the database
        // The chart will update every time a change in records will occur
        juoViewModel.getAllIntakeInputs().observe(this, intakeEntities -> {
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
                    float x = Float.parseFloat(sHour);
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
            Intent intent = new Intent(History.this, MainActivity.class);
            startActivity(intent);
        }
    }

}