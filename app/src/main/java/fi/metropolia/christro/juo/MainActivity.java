package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.lang.reflect.Type;
import java.util.ArrayList;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCE_FILE = "fi.metropolia.christro.juo";
    public static final String CUSTOM_BUTTONS_LIST_KEY = "fi.metropolia.christro.juo.custom_buttons_list_key";
    public static final String HYDRATION_GOAL = "fi.metropolia.christro.juo.hydration_goal";
    public static final String EXTRA_IS_FIRST_START_UP = "fi.metropolia.christro.juo.EXTRA_IS_FIRST";

    private CircularProgressBar circularProgressBar;

    private TextView textView;

    private int hydrationGoal;

    private ArrayList<Integer> customButtonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hydrationGoal = loadHydrationGoal();

        //customButtonList = loadButtonList();

        textView = findViewById(R.id.intakeText);
        circularProgressBar = findViewById(R.id.circularProgressBar);

        JuoViewModel juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        final Observer<Integer> dailyTotalObserver = newTotal -> {
            // Update the UI, in this case, a TextView.
            if (newTotal != null) {
                textView.setText(String.valueOf(newTotal));
            } else {
                textView.setText(String.valueOf(0));
            }

            if (newTotal != null) {
                circularProgressBar.setProgressWithAnimation(newTotal, (long) 300);
            } else {
                circularProgressBar.setProgress(0);
            }
        };

        juoViewModel.getDailyTotal().observe(this, dailyTotalObserver);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(view -> {
            juoViewModel.insertIntake(new IntakeEntity(400));
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            juoViewModel.insertIntake(new IntakeEntity(250));
        });
    }

    private int loadHydrationGoal(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);

        int hydrationGoal = sharedPreferences.getInt(HYDRATION_GOAL, -1);

        if (hydrationGoal == -1) {
            Intent intent = new Intent(this, ProfileSettingsActivity.class);
            intent.putExtra(EXTRA_IS_FIRST_START_UP, true);
            startActivity(intent);
            return -1;
        }

        return hydrationGoal;
    }







    private ArrayList<Integer> loadButtonList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sharedPreferences.getString(CUSTOM_BUTTONS_LIST_KEY, null);

        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();

        if (json == null) {
            ArrayList<Integer> arrayList = new ArrayList<>();

            arrayList.add(251);
            arrayList.add(500);
            arrayList.add(100);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);
            arrayList.add(750);

            return arrayList;
        }

        return gson.fromJson(json, type);
    }
}