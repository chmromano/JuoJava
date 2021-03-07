package fi.metropolia.christro.juo.database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

//Guide used: https://developer.android.com/codelabs/android-room-with-a-view#9

/**
 * ViewModel class. The ViewModel acts as a gateway between the database data and the UI. This way
 * data is persisted through configuration changes
 * @author Christopher Mohan Romano
 * @version 1.0
 */
public class JuoViewModel extends AndroidViewModel {
    private JuoRepository repository;
    private final LiveData<List<IntakeEntity>> allIntakes;
    private LiveData<Integer> dailyTotal;
    private LiveData<String> latestIntake;
    private LiveData<List<MoodEntity>> allMoods;

    /**
     * Construction for the ViewModel. Initialises the repository and the all LiveData.
     * @param application The given application
     */
    public JuoViewModel(Application application) {
        super(application);
        repository = new JuoRepository(application);

        allIntakes = repository.getAllIntakeInputs();
        dailyTotal = repository.getDailyTotal();
        latestIntake = repository.getLatestIntake();
        allMoods = repository.getAllMoodInputs();
    }

    /**
     * Wrapper for repository getDailyTotal() method.
     * @return LiveData Integer of daily total.
     */
    public LiveData<Integer> getDailyTotal() {
        return dailyTotal;
    }

    /**
     * Wrapper for repository insertIntake() method.
     * @param intakeEntity The IntakeEntity to insert in the database.
     */
    public void insertIntake(IntakeEntity intakeEntity) {
        repository.insertIntake(intakeEntity);
    }

    /**
     * Wrapper for repository insertMood() method.
     * @param moodEntity
     */
    public void insertMood(MoodEntity moodEntity){
        repository.insertMood(moodEntity);
    }

    /**
     * Wrapper for repository getAllMoodInputs() method.
     * @return LiveData List containing all MoodEntities.
     */
    public LiveData<List<MoodEntity>> getAllMoodInputs() {
        return allMoods;
    }

    /**
     * Wrapper for repository deleteIntake() method.
     * @param intakeEntity The IntakeEntity to delete.
     */
    public void deleteIntake(IntakeEntity intakeEntity) {
        repository.deleteIntake(intakeEntity);
    }

    /**
     * Wrapper for repository deleteAllIntakes() method.
     */
    public void deleteAllIntakes() {
        repository.deleteAllIntakes();
    }

    /**
     * Wrapper for repository getLatestIntake() method.
     * @return LiveData String containing the date of the latest intake.
     */
    public LiveData<String> getLatestIntake(){
        return latestIntake;
    }

    /**
     * Wrapper for repository getAllIntakeInputs() method.
     * @return LiveData List containing all IntakeEntities.
     */
    public LiveData<List<IntakeEntity>> getAllIntakeInputs() {
        return allIntakes;
    }
}
