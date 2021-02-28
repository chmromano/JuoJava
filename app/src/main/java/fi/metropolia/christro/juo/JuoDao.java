package fi.metropolia.christro.juo;

import androidx.core.util.Pair;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import androidx.lifecycle.LiveData;

//Data access object. Basically "methods" but for the database. We use the DAOs described below to
//manipulate and access our database.
@Dao
public interface JuoDao {

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

    @Query("SELECT SUM(amount) FROM intakes_table WHERE date LIKE DATE()")
    LiveData<Integer> getDailyTotal();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMood(MoodEntity moodEntity);

    //@Query("SELECT mood_table.mood AS first, SUM(intakes_table.amount) AS second FROM intakes_table " +
    //        "INNER JOIN mood_table ON intakes_table.date LIKE mood_table.date GROUP BY intakes_table.date")
    //List<Pair<Integer, Integer>> getGraph();
}