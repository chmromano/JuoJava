package fi.metropolia.christro.juo;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.List;

class IntakeInputRepository {
    private IntakeInputDao intakeInputDao;
    private LiveData<List<IntakeInput>> allIntakeInputs;
    private LiveData<Integer> dailyTotal;

    IntakeInputRepository(Application application) {
        IntakeInputDatabase db = IntakeInputDatabase.getDatabase(application);
        intakeInputDao = db.intakeInputDao();
        allIntakeInputs = intakeInputDao.getAllIntakes();
        dailyTotal = intakeInputDao.getDailyTotal(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    LiveData<List<IntakeInput>> getAllIntakeInputs() {
        return allIntakeInputs;
    }

    LiveData<Integer> getDailyTotal() {

        /*dailyTotal = intakeInputDao.getDailyTotal(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));*/

        IntakeInputDatabase.databaseWriteExecutor.execute(() -> {
            dailyTotal = intakeInputDao.getDailyTotal(Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        });

        return dailyTotal;
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
