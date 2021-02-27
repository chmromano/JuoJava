package fi.metropolia.christro.juo;

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
public interface IntakeInputDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(IntakeInput intakeInput);

    @Update
    void update(IntakeInput intakeInput);

    @Delete
    void delete(IntakeInput intakeInput);

    @Query("DELETE FROM intakes_table")
    void deleteAllIntakes();

    @Query("SELECT * FROM intakes_table ORDER BY date DESC")
    LiveData<List<IntakeInput>> getAllIntakes();

    @Query("SELECT SUM(amount) FROM intakes_table WHERE date LIKE :date || '%'")
    LiveData<Integer> getDailyTotal(String date);
}