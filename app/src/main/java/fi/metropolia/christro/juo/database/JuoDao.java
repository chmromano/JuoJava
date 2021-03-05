package fi.metropolia.christro.juo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * Represents a data access object (if a method operates on an object, then a DAO operates on a
 * database).
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

    @Update
    void updateIntake(IntakeEntity intakeEntity);

    @Delete
    void deleteIntake(IntakeEntity intakeEntity);

    @Query("DELETE FROM intakes_table")
    void deleteAllIntakes();

    @Query("SELECT * FROM intakes_table ORDER BY date DESC")
    LiveData<List<IntakeEntity>> getAllIntakes();

    @Query("SELECT SUM(amount) FROM intakes_table WHERE date LIKE :date || '%'")
    LiveData<Integer> getDailyTotal(String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMood(MoodEntity moodEntity);

    @Query("SELECT mood FROM mood_table WHERE date LIKE :date || '%'")
    int getDailyMood(String date);

    @Query("SELECT SUM(amount) FROM intakes_table WHERE date LIKE :date || '%'")
    int getHistoricDailyTotal(String date);

    @Query("SELECT * FROM intakes_table ORDER BY date DESC, time DESC LIMIT 1")
    IntakeEntity getLatestIntake();
}