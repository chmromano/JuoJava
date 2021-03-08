package fi.metropolia.christro.juo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import androidx.lifecycle.LiveData;

//Guide used: https://developer.android.com/codelabs/android-room-with-a-view#5

/**
 * Represents a data access object (if a method operates on an object, then a DAO operates on a
 * database). Interface allows other classes to implement the DAOs desribed here.
 * @author Christopher Romano
 * @version 1.0
 */
@Dao
public interface JuoDao {

    /**
     * DAO to insert an IntakeEntity in the database. In case of a conflict the inserted object will
     * overwrite the old object.
     * @param intakeEntity An object containing the IntakeEntity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIntake(IntakeEntity intakeEntity);

    /**
     * DAO to delete an IntakeEntity from the database.
     * @param intakeEntity Object containing the IntakeEntity to be deleted.
     */
    @Delete
    void deleteIntake(IntakeEntity intakeEntity);

    /**
     * DAO to delete all IntakeEntities from the database.
     */
    @Query("DELETE FROM intakes_table")
    void deleteAllIntakes();

    /**
     * DAO to get all IntakeEntity from the database.
     * @return LiveData List of all IntakeEntities.
     */
    @Query("SELECT * FROM intakes_table ORDER BY date DESC")
    LiveData<List<IntakeEntity>> getAllIntakes();

    /**
     * DAO to get daily total from a given date. Automatically sums al intakes.
     * @param date String containing the date of the total.
     * @return LiveData Integer of sum of intakes.
     */
    @Query("SELECT SUM(amount) FROM intakes_table WHERE date LIKE :date || '%'")
    LiveData<Integer> getDailyTotal(String date);

    /**
     * DAO to get the date and time of the last inputted intake.
     * @return LiveData String containing date and time of intake.
     */
    @Query("SELECT date || ' ' || time FROM intakes_table ORDER BY date DESC, time DESC LIMIT 1")
    LiveData<String> getLatestIntake();

    /**
     * DAO to insert a MoodEntity in the database. In case of a conflict the inserted object will
     * overwrite the old object.
     * @param moodEntity An object containing the MoodEntity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMood(MoodEntity moodEntity);

    /**
     * DAO to get all MoodEntities from the database.
     * @return LiveData List of all MoodEntities.
     */
    @Query("SELECT * FROM mood_table ORDER BY date DESC")
    LiveData<List<MoodEntity>> getAllMoods();
}