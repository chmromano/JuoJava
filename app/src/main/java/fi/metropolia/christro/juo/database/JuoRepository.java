package fi.metropolia.christro.juo.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//Guide used: https://developer.android.com/codelabs/android-room-with-a-view#8

/**
 * Repository class used to access database and entities. It also allows database operations
 * to be executed in the background.
 * @author Christopher Mohan Romano
 * @version 1.0
 */
class JuoRepository {
    private JuoDao juoDao;
    private LiveData<List<IntakeEntity>> allIntakeInputs;
    private LiveData<Integer> dailyTotal;
    private LiveData<String> latestIntake;
    private LiveData<List<MoodEntity>> allMoodInputs;

    /**
     * Constructor for the repository. Gets database instance and links the database to the DAO
     * with all methods to access and modify the database. All LiveData variables are initialised here.
     * @param application The given application.
     */
    JuoRepository(Application application) {
        JuoDatabase db = JuoDatabase.getDatabase(application);
        juoDao = db.juoDao();

        allIntakeInputs = juoDao.getAllIntakes();
        dailyTotal = juoDao.getDailyTotal(new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(Calendar.getInstance().getTime()));
        latestIntake = juoDao.getLatestIntake();
        allMoodInputs = juoDao.getAllMoods();
    }

    /**
     * Method implements DAO getAllIntakeInputs().
     * @return LiveData List containing all IntakeEntities.
     */
    LiveData<List<IntakeEntity>> getAllIntakeInputs() {
        return allIntakeInputs;
    }

    /**
     * Method implements DAO getDailyTotal().
     * @return LiveData Integer containing sum of daily intakes.
     */
    LiveData<Integer> getDailyTotal() {
        return dailyTotal;
    }

    /**
     * Method implements DAO getLatestIntake().
     * @return LiveData String containing date and time of latest intake.
     */
    LiveData<String> getLatestIntake() {
        return latestIntake;
    }

    /**
     * Method implements DAO getAllMoodInputs().
     * @return LiveData List containing all MoodEntities.
     */
    LiveData<List<MoodEntity>> getAllMoodInputs() {
        return allMoodInputs;
    }

    /**
     * Method implements DAO insertMood(). Operation that writes to database is executed in a
     * background thread.
     * @param moodEntity The MoodEntity to insert in the database.
     */
    void insertMood(MoodEntity moodEntity) {
        JuoDatabase.databaseWriteExecutor.execute(() -> juoDao.insertMood(moodEntity));
    }

    /**
     * Method implements DAO insertIntake(). Operation that writes to database is executed in a
     * background thread.
     * @param intakeEntity The IntakeEntity to insert in the database.
     */
    void insertIntake(IntakeEntity intakeEntity) {
        JuoDatabase.databaseWriteExecutor.execute(() -> juoDao.insertIntake(intakeEntity));
    }

    /**
     * Method implements DAO deleteIntake(). Operation that modifies the database is executed in a
     * background thread.
     * @param intakeEntity The MoodEntity to delete from the database.
     */
    void deleteIntake(IntakeEntity intakeEntity) {
        JuoDatabase.databaseWriteExecutor.execute(() -> juoDao.deleteIntake(intakeEntity));
    }

    /**
     * Method implements DAO deleteAllIntakes(). Operation that modifies the database is executed in
     * a background thread.
     */
    void deleteAllIntakes() {
        JuoDatabase.databaseWriteExecutor.execute(() -> juoDao.deleteAllIntakes());
    }
}
