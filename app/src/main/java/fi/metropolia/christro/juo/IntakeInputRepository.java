package fi.metropolia.christro.juo;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

class IntakeInputRepository {
    private IntakeInputDao intakeInputDao;
    private LiveData<List<IntakeInput>> allIntakeInputs;

    IntakeInputRepository(Application application) {
        IntakeInputDatabase dp = IntakeInputDatabase.getDatabase(application);
        intakeInputDao = dp.intakeInputDao();
        allIntakeInputs = intakeInputDao.getAllIntakes();
    }

    LiveData<List<IntakeInput>> getAllIntakeInputs() {
        return allIntakeInputs;
    }

    void insert(IntakeInput intakeInput) {
        IntakeInputDatabase.databaseWriteExecutor.execute(() -> {
            intakeInputDao.insert(intakeInput);
        });
    }

    void update(IntakeInput intakeInput) {
        IntakeInputDatabase.databaseWriteExecutor.execute(() -> {
            intakeInputDao.update(intakeInput);
        });
    }

    void delete(IntakeInput intakeInput) {
        IntakeInputDatabase.databaseWriteExecutor.execute(() -> {
            intakeInputDao.update(intakeInput);
        });
    }

    void deleteAllIntakes() {
        IntakeInputDatabase.databaseWriteExecutor.execute(() -> {
            intakeInputDao.deleteAllIntakes();
        });
    }
}
