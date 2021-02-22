package fi.metropolia.christro.juo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;


public class History extends AppCompatActivity {
    BarChart barChart;
    BarData barData;
    public BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries;
    ViewFlipper vf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        vf = findViewById(R.id.chart_slider);

        barDataSet = new BarDataSet(getData(),"Day Data set");
        barChart = findViewById(R.id.barChartDay);
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        flipperChart();
    }

    public void flipperChart(){
        vf.setFlipInterval(4000);
        vf.setAutoStart(true);

        vf.setInAnimation(this, android.R.anim.slide_in_left);
        vf.setOutAnimation(this, android.R.anim.slide_out_right);

    }

    public ArrayList<BarEntry> getData(){
        barEntries = new ArrayList();
        barEntries.add(new BarEntry(1f,2));
        barEntries.add(new BarEntry(3f,5));
        barEntries.add(new BarEntry(10f,1));
        barEntries.add(new BarEntry(6f,7));
        barEntries.add(new BarEntry(4f,12));
        return barEntries;
    }





}