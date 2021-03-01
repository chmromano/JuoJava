package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ProfileSettingsActivity extends AppCompatActivity {

    private ArrayList<Integer> weightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        weightList = initialiseWeightList();

        ArrayAdapter<Integer> spinnerWeightAdapter = new ArrayAdapter<Integer>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                weightList
        );

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(spinnerWeightAdapter);
    }

    public ArrayList<Integer> initialiseWeightList(){
        ArrayList<Integer> initList = new ArrayList<Integer>();
        for(int i =1; i < 500; i++){
            initList.add(i);
        }
        return initList;
    }
}