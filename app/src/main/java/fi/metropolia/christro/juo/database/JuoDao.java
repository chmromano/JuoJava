package fi.metropolia.christro.juo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Represents a data access object (if a method operates on an object, then a DAO operates on a
 * database). Interface allows other classes to implement the DAOs described here.
 *
 * @author Christopher Romano
 * @version 1.0
 */
//Guide used: https://developer.android.com/codelabs/android-room-with-a-view#5
@Dao
public interface JuoDao {

    /**
     * DAO to insert an IntakeEntity in the database. In case of a conflict the inserted object will
     * overwrite the old object.
     *
     * @param intakeEntity An object containing the IntakeEntity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIntake(IntakeEntity intakeEntity);

    /**
     * DAO to delete an IntakeEntity from the database.
     *
     * @param intakeEntity Object containing the IntakeEntity to be deleted.
     */
    @Delete
    void deleteIntake(IntakeEntity intakeEntity);

    /**
     * DAO to get all IntakeEntity from the database.
     *
     * @return LiveData List of all IntakeEntities.
     */
    @Query("SELECT * FROM intakes_table ORDER BY date DESC")
    LiveData<List<IntakeEntity>> getAllIntakes();

    /**
     * DAO to get daily total from a given date. Automatically sums al intakes.
     *
     * @param date String containing the date of the total.
     * @return LiveData Integer of sum of intakes.
     */
    @Query("SELECT SUM(amount) FROM intakes_table WHERE date LIKE :date || '%'")
    LiveData<Integer> getDailyTotal(String date);

    /**
     * DAO to insert a MoodEntity in the database. In case of a conflict the inserted object will
     * overwrite the old object.
     *
     * @param moodEntity An object containing the MoodEntity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMood(MoodEntity moodEntity);

    /**
     * DAO to get all MoodEntities from the database.
     *
     * @return LiveData List of all MoodEntities.
     */
    @Query("SELECT * FROM mood_table ORDER BY date DESC")
    LiveData<List<MoodEntity>> getAllMoods();
}