package fi.metropolia.christro.juo;

import android.os.Bundle;
//import android.support.wearable.activity.WearableActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

public class History extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //mTextView = (TextView) findViewById(R.id.text);
        ViewPager viewPager = findViewById(R.id.chart_slider);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
        // Enables Always-on
    }
}