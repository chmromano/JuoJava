package fi.metropolia.christro.juo.database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class JuoViewModel extends AndroidViewModel {

    private JuoRepository repository;

    private final LiveData<List<IntakeEntity>> allIntakes;

    private LiveData<Integer> dailyTotal;

    public JuoViewModel(Application application) {
        super(application);
        repository = new JuoRepository(application);
        allIntakes = repository.getAllIntakeInputs();
        dailyTotal = repository.getDailyTotal();
    }

    public LiveData<Integer> getDailyTotal() {
        return dailyTotal;
    }

    public void insertIntake(IntakeEntity intakeEntity) {
        repository.insertIntake(intakeEntity);
    }

    public void insertMood(MoodEntity moodEntity){
        repository.insertMood(moodEntity);
    }

    public void updateIntake(IntakeEntity intakeEntity) {
        repository.updateIntake(intakeEntity);
    }

    public void deleteIntake(IntakeEntity intakeEntity) {
        repository.deleteIntake(intakeEntity);
    }

    public void deleteAllIntakes() {
        repository.deleteAllIntakes();
    }

    public IntakeEntity getLatestIntake(){
        return repository.getLatestIntake();
    }

    public LiveData<List<IntakeEntity>> getAllIntakeInputs() {
        return allIntakes;
    }

    public int getHistoricalDailyTotal(String date) {
        return repository.getGetHistoricalDailyTotal(date);
    }
}
