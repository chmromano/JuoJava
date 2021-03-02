package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCE_FILE = "fi.metropolia.christro.juo";
    public static final String EXTRA_IS_FIRST_START_UP = "fi.metropolia.christro.juo.EXTRA_IS_FIRST_START_UP";

    private CircularProgressBar circularProgressBar;

    private TextView textView;

    private int hydrationGoal;

    private Button mainActivityButton1;
    private Button mainActivityButton2;
    private Button mainActivityButton3;
    private Button mainActivityButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hydrationGoal = loadHydrationGoal();


        //TESTING REMOVE LATER
        findViewById(R.id.imageButton).setOnClickListener((view) -> {
            Intent testIntent = new Intent(this, SettingsActivity.class);
            startActivity(testIntent);
        });
        //TESTING REMOVE LATER



        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);

        int sharedButton1 = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_1, 250);
        int sharedButton2 = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_2, 500);
        int sharedButton3 = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_3, 100);
        int sharedButton4 = sharedPreferences.getInt(SettingsActivity.SHARED_BUTTON_4, 700);

        mainActivityButton1 = findViewById(R.id.mainActivityButton1);
        mainActivityButton2 = findViewById(R.id.mainActivityButton2);
        mainActivityButton3 = findViewById(R.id.mainActivityButton3);
        mainActivityButton4 = findViewById(R.id.mainActivityButton4);

        mainActivityButton1.setText(String.valueOf(sharedButton1));
        mainActivityButton2.setText(String.valueOf(sharedButton2));
        mainActivityButton3.setText(String.valueOf(sharedButton3));
        mainActivityButton4.setText(String.valueOf(sharedButton4));

        textView = findViewById(R.id.intakeText);

        circularProgressBar = findViewById(R.id.circularProgressBar);

        circularProgressBar.setProgressMax((float) hydrationGoal);

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

        mainActivityButton1.setOnClickListener(view -> {
            int intake = Integer.parseInt(mainActivityButton1.getText().toString());
            juoViewModel.insertIntake(new IntakeEntity(intake));
        });

        mainActivityButton2.setOnClickListener(view -> {
            int intake = Integer.parseInt(mainActivityButton2.getText().toString());
            juoViewModel.insertIntake(new IntakeEntity(intake));
        });

        mainActivityButton3.setOnClickListener(view -> {
            int intake = Integer.parseInt(mainActivityButton3.getText().toString());
            juoViewModel.insertIntake(new IntakeEntity(intake));
        });

        mainActivityButton4.setOnClickListener(view -> {
            int intake = Integer.parseInt(mainActivityButton4.getText().toString());
            juoViewModel.insertIntake(new IntakeEntity(intake));
        });
    }

    private int loadHydrationGoal() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);

        int sharedGoal = sharedPreferences.getInt(SettingsActivity.SHARED_GOAL, 999999);

        if (sharedGoal == 999999) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.main_activity_dialog_title))
                    .setMessage(getString(R.string.main_activity_dialog_content))
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getString(R.string.dialog_ok), (dialog, which) -> {
                        dialog.cancel();
                        Intent intent = new Intent(this, SettingsActivity.class);
                        intent.putExtra(EXTRA_IS_FIRST_START_UP, true);
                        startActivity(intent);
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return 999999;
        }

        return sharedGoal;
    }







/*    private ArrayList<Integer> loadButtonList() {
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
    }*/
}