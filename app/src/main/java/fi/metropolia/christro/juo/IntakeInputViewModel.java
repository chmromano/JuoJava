package fi.metropolia.christro.juo;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class IntakeInputViewModel extends AndroidViewModel {

    private IntakeInputRepository repository;

    private final LiveData<List<IntakeInput>> allIntakes;

    private LiveData<Integer> dailyTotal;

    public IntakeInputViewModel(Application application) {
        super(application);
        repository = new IntakeInputRepository(application);
        allIntakes = repository.getAllIntakeInputs();
        dailyTotal = repository.getDailyTotal();
    }

    LiveData<Integer> getDailyTotal() {
        return dailyTotal;
    }

    public void insert(IntakeInput intakeInput) {
        repository.insertIntake(intakeInput);
    }

    public void update(IntakeInput intakeInput) {
        repository.updateIntake(intakeInput);
    }

    public void delete(IntakeInput intakeInput) {
        repository.deleteIntake(intakeInput);
    }

    public void deleteAllIntakes() {
        repository.deleteAllIntakes();
    }

    LiveData<List<IntakeInput>> getAllIntakeInputs() {
        return allIntakes;
    }
}
