package fi.metropolia.christro.juo;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class IntakeInputViewModel extends AndroidViewModel {

    private IntakeInputRepository repository;

    private final LiveData<List<IntakeInput>> allIntakes;

    public IntakeInputViewModel(Application application) {
        super(application);
        repository = new IntakeInputRepository(application);
        allIntakes = repository.getAllIntakeInputs();
    }

    public void insert(IntakeInput intakeInput) {
        repository.insert(intakeInput);
    }

    public void update(IntakeInput intakeInput) {
        repository.update(intakeInput);
    }

    public void delete(IntakeInput intakeInput) {
        repository.delete(intakeInput);
    }

    public void deleteAllIntakes() {
        repository.deleteAllIntakes();
    }

    public LiveData<List<IntakeInput>> getAllIntakes() {
        return allIntakes;
    }
}
