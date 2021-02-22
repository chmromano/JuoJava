package fi.metropolia.christro.juo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class History extends AppCompatActivity {

    ViewFlipper vf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        vf = findViewById(R.id.chart_slider);
        flipperChart();
    }

    public void flipperChart(){
        vf.setFlipInterval(4000);
        vf.setAutoStart(true);

        vf.setInAnimation(this, android.R.anim.slide_in_left);
        vf.setOutAnimation(this, android.R.anim.slide_out_right);

    }
}