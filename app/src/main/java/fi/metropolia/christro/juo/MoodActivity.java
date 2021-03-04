package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

import fi.metropolia.christro.juo.database.JuoViewModel;
import fi.metropolia.christro.juo.database.MoodEntity;

public class MoodActivity extends AppCompatActivity {

    public static final int MOOD_GOOD_ID = 3;
    public static final int MOOD_NORMAL_ID = 2;
    public static final int MOOD_BAD_ID = 1;

    private RadioGroup radioGroupMood;

    private JuoViewModel juoViewModel;

    private Button buttonMoodCancel;
    private Button buttonMoodSubmit;

    Intent intent;

    int selectedRadioButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        initialiseViews();

        intent = new Intent(this, MainActivity.class);

        buttonMoodCancel.setOnClickListener((view) -> startActivity(intent));

        buttonMoodSubmit.setOnClickListener((view) -> {
            selectedRadioButtonId = radioGroupMood.getCheckedRadioButtonId();
            if(selectedRadioButtonId == R.id.radioButtonGood){
                juoViewModel.insertMood(new MoodEntity(MOOD_GOOD_ID));
            }else if(selectedRadioButtonId == R.id.radioButtonNormal){
                juoViewModel.insertMood(new MoodEntity(MOOD_NORMAL_ID));
            }else if(selectedRadioButtonId == R.id.radioButtonBad){
                juoViewModel.insertMood(new MoodEntity(MOOD_BAD_ID));
            }
            startActivity(intent);
        });
    }

    private void initialiseViews() {
        radioGroupMood = findViewById(R.id.radioGroupMood);
        buttonMoodCancel = findViewById(R.id.buttonMoodCancel);
        buttonMoodSubmit = findViewById(R.id.buttonMoodSubmit);
        juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);
    }
}