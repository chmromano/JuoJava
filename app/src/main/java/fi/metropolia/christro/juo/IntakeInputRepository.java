package fi.metropolia.christro.juo;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
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
        dailyTotal = intakeInputDao.getDailyTotal(new SimpleDateFormat("yyyy-MM-dd")
                .format(Calendar.getInstance().getTime()));
    }

    LiveData<List<IntakeInput>> getAllIntakeInputs() {
        return allIntakeInputs;
    }

    LiveData<Integer> getDailyTotal() {
        IntakeInputDatabase.databaseWriteExecutor.execute(() -> {
            dailyTotal = intakeInputDao.getDailyTotal(new SimpleDateFormat("yyyy-MM-dd")
                    .format(Calendar.getInstance().getTime()));
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
