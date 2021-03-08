package fi.metropolia.christro.juo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries;
    private static final String TAG = "fi.metropolia.christro.juo.History";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);

        JuoViewModel juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        getData(juoViewModel);

        if(barEntries == null) {
            Log.d(TAG, "onCreateCharts: empty bar chart");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.options_menu_intake_list) {
            Toast.makeText(this, "swipe to delete record", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(History.this, IntakeListview.class);
            startActivity(intent);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    public void getData(JuoViewModel juoViewModel){
        barEntries = new ArrayList<BarEntry>();
        Date date = new Date();
        String sDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        Log.d(TAG, "getData: "+sDate);

       LiveData<String> data= juoViewModel.getLatestIntake();


        juoViewModel.getAllIntakeInputs().observe(this, new Observer<List<IntakeEntity>>() {
            @Override
            public void onChanged(List<IntakeEntity> intakeEntities) {

                for(IntakeEntity intake : intakeEntities){
                    if(intake.getDate().equals(sDate)) {
                        Log.d(TAG, "onChanged: " + intake.getTime() + "," + intake.getAmount());
                        String intakeTime = intake.getTime();
                        char[] hour = new char[3];
                        intakeTime.getChars(0, 2, hour, 0);
                        String sHour = String.valueOf(hour);
                        float x = Float.valueOf(sHour);
                        Log.d(TAG, "onChanged: x value is" + x);
                        float y = intake.getAmount();
                        barEntries.add(new BarEntry(x, y));
                    }
                }
                Log.d(TAG, "onChanged: barentries"+barEntries);
                barDataSet = new BarDataSet(barEntries, "Day Data set");
                barChart = findViewById(R.id.barChartDay);
                barData = new BarData(barDataSet);
                barChart.setData(barData);
                barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            }

        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(History.this, MainActivity.class);
        startActivity(intent);

    }

}