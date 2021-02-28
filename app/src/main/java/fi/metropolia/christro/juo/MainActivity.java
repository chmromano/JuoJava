package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCE_FILE = "fi.metropolia.christro.juo";
    public static final String CUSTOM_BUTTONS_LIST_KEY = "fi.metropolia.christro.juo.custom_buttons_list_key";

    private JuoRepository repository;

    private CircularProgressBar circularProgressBar;

    private TextView textView;

    private ArrayList<Integer> customButtonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customButtonList = loadButtonList();

        repository = new JuoRepository(this.getApplication());

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
            repository.insertIntake(new IntakeEntity(400));
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            repository.insertIntake(new IntakeEntity(250));
        });
    }

    private ArrayList<Integer> loadButtonList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sharedPreferences.getString(CUSTOM_BUTTONS_LIST_KEY, null);

        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();

        if (json == null) {
            ArrayList<Integer> arrayList = new ArrayList<>();

            arrayList.add(250);
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