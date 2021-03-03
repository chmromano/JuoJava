package fi.metropolia.christro.juo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
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
        int[] charts = new int[]{R.id.barChartDay};//R.id.barChartMonth,R.id.barChartYear};
        for(int chart: charts) {
            barEntriesList = getData();
            barDataSet = new BarDataSet(barEntriesList, chart+" Data set");
            barChart = findViewById(chart);
            barData = new BarData(barDataSet);
            barChart.setData(barData);
            barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        }
        if(barEntriesList == null) {
            Log.d(TAG, "onCreateCharts: empty bar chart");
        }else {
            flipperChart();
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
        LiveData<List<IntakeEntity>> intakeEntityList = juoViewModel.getAllIntakeInputs();
        List<IntakeEntity> entriesList = intakeEntityList.getValue();
        if(entriesList.isEmpty()){
            Log.d(TAG, "entry list is empty ");
            barEntries.add(new BarEntry(0, 0));
            return barEntries;
        }else {
            for (IntakeEntity entry : entriesList) {
                //set a constant number of groups
                float x = 1;
                int y = entry.getAmount();
                barEntries.add(new BarEntry(x, y));
            }
            return barEntries;
        }
    }





}