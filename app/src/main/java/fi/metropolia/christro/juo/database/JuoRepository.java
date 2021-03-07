package fi.metropolia.christro.juo.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

class JuoRepository {
    private JuoDao juoDao;

    private LiveData<List<IntakeEntity>> allIntakeInputs;
    private LiveData<Integer> dailyTotal;
    private LiveData<String> latestIntake;
    private int historicalDailyTotal;


    JuoRepository(Application application) {
        JuoDatabase db = JuoDatabase.getDatabase(application);
        juoDao = db.juoDao();

        allIntakeInputs = juoDao.getAllIntakes();
        dailyTotal = juoDao.getDailyTotal(new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(Calendar.getInstance().getTime()));
        latestIntake = juoDao.getLatestIntake();
    }

    int getGetHistoricalDailyTotal(String date) {
        return historicalDailyTotal;
    }

    LiveData<List<IntakeEntity>> getAllIntakeInputs() {
        return allIntakeInputs;
    }

    LiveData<Integer> getDailyTotal() {
        return dailyTotal;
    }

    LiveData<String> getLatestIntake() {
        return latestIntake;
    }

    void insertMood(MoodEntity moodEntity) {
        JuoDatabase.databaseWriteExecutor.execute(() -> juoDao.insertMood(moodEntity));
    }

    void insertIntake(IntakeEntity intakeEntity) {
        JuoDatabase.databaseWriteExecutor.execute(() -> juoDao.insertIntake(intakeEntity));
    }

    void updateIntake(IntakeEntity intakeEntity) {
        JuoDatabase.databaseWriteExecutor.execute(() -> juoDao.updateIntake(intakeEntity));
    }

    void deleteIntake(IntakeEntity intakeEntity) {
        JuoDatabase.databaseWriteExecutor.execute(() -> juoDao.deleteIntake(intakeEntity));
    }

    void deleteAllIntakes() {
        JuoDatabase.databaseWriteExecutor.execute(() -> juoDao.deleteAllIntakes());
    }
}
