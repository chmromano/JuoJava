package fi.metropolia.christro.juo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DailyTotalViewModel extends AndroidViewModel {

    private IntakeInputRepository repository;

    private LiveData<Integer> dailyTotal;

    public DailyTotalViewModel(Application application) {
        super(application);
        repository = new IntakeInputRepository(application);
        dailyTotal = repository.getDailyTotal();
    }

    LiveData<Integer> getDailyTotal() {
        return dailyTotal;
    }
}
