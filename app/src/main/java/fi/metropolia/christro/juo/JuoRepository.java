package fi.metropolia.christro.juo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

class JuoRepository {
    private JuoDao juoDao;

    private LiveData<List<IntakeEntity>> allIntakeInputs;
    private LiveData<Integer> dailyTotal;

    JuoRepository(Application application) {
        JuoDatabase db = JuoDatabase.getDatabase(application);
        juoDao = db.juoDao();

        allIntakeInputs = juoDao.getAllIntakes();
        dailyTotal = juoDao.getDailyTotal();
    }

    LiveData<List<IntakeEntity>> getAllIntakeInputs() {
        return allIntakeInputs;
    }

    LiveData<Integer> getDailyTotal() {
        /*JuoDatabase.databaseWriteExecutor.execute(() -> {
            dailyTotal = juoDao.getDailyTotal();
        });*/

        return dailyTotal;
    }

    void insertIntake(IntakeEntity intakeEntity) {
        JuoDatabase.databaseWriteExecutor.execute(() -> {
            juoDao.insertIntake(intakeEntity);
        });
    }

    void updateIntake(IntakeEntity intakeEntity) {
        JuoDatabase.databaseWriteExecutor.execute(() -> {
            juoDao.updateIntake(intakeEntity);
        });
    }

    void deleteIntake(IntakeEntity intakeEntity) {
        JuoDatabase.databaseWriteExecutor.execute(() -> {
            juoDao.deleteIntake(intakeEntity);
        });
    }

    void deleteAllIntakes() {
        JuoDatabase.databaseWriteExecutor.execute(() -> {
            juoDao.deleteAllIntakes();
        });
    }
}
