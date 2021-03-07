package fi.metropolia.christro.juo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;


public class History extends AppCompatActivity {
    BarChart barChart;
    BarData barData;
    public BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries;
    ArrayList<BarEntry> barEntriesList;
    ViewFlipper vf;
    private static final String TAG = "fi.metropolia.christro.juo.History";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        //int[] charts = new int[]{R.id.barChartDay};//R.id.barChartMonth,R.id.barChartYear};
        //for(int chart: charts) {
            barEntriesList = getData();
            barDataSet = new BarDataSet(barEntriesList, "day Data set");
            barChart = findViewById(R.id.barChartDay);
            barData = new BarData(barDataSet);
            barChart.setData(barData);
            barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        //}
        if(barEntriesList == null) {
            Log.d(TAG, "onCreateCharts: empty bar chart");
        }else {
            //flipperChart();
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

    public void flipperChart(){
        vf.setFlipInterval(4000);
        vf.setAutoStart(true);

        vf.setInAnimation(this, android.R.anim.slide_in_left);
        vf.setOutAnimation(this, android.R.anim.slide_out_right);

    }

    public ArrayList<BarEntry> getData(){
        barEntries = new ArrayList();
        JuoViewModel juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        juoViewModel.getAllIntakeInputs().observe(this, new Observer<List<IntakeEntity>>() {
            @Override
            public void onChanged(List<IntakeEntity> intakeEntities) {
                for(IntakeEntity intake : intakeEntities){
                    Log.d(TAG, "onChanged: "+intake.getTime()+","+intake.getAmount());
                    String intakeTime = intake.getTime();
                    char[] x = new char[5];
                    intakeTime.replace(":",".").getChars(0,4,x,0);
                    int y = intake.getAmount();
                    barEntries.add(new BarEntry(Float.parseFloat(String.valueOf(x)), Float.parseFloat(String.valueOf(y))));
                }
            }
        });
        return barEntries;
    }
}