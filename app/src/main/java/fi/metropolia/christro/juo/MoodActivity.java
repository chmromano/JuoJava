package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import fi.metropolia.christro.juo.database.JuoViewModel;
import fi.metropolia.christro.juo.database.MoodEntity;

/**
 * Mood activity of the application. Used to insert current daily mood in database.
 *
 * @author Taranath Pokhrel.
 * @version 1.0
 */
public class MoodActivity extends AppCompatActivity {

    //IDs  of moods
    public static final int MOOD_GOOD_ID = 3;
    public static final int MOOD_NORMAL_ID = 2;
    public static final int MOOD_BAD_ID = 1;

    /**
     * onCreate() method creates the activity.
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        JuoViewModel juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        Intent intent = new Intent(this, MainActivity.class);

        findViewById(R.id.imageButtonMoodBack).setOnClickListener((view) -> startActivity(intent));
        findViewById(R.id.buttonMoodCancel).setOnClickListener((view) -> startActivity(intent));

        findViewById(R.id.buttonMoodSubmit).setOnClickListener((view) -> {
            int selectedRadioButtonId = ((RadioGroup) findViewById(R.id.radioGroupMood)).getCheckedRadioButtonId();
            if (selectedRadioButtonId == R.id.radioButtonGood) {
                juoViewModel.insertMood(new MoodEntity(MOOD_GOOD_ID));
            } else if (selectedRadioButtonId == R.id.radioButtonNormal) {
                juoViewModel.insertMood(new MoodEntity(MOOD_NORMAL_ID));
            } else if (selectedRadioButtonId == R.id.radioButtonBad) {
                juoViewModel.insertMood(new MoodEntity(MOOD_BAD_ID));
            }
            startActivity(intent);
        });
    }
}