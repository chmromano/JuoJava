package fi.metropolia.christro.juo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

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

    @Query("SELECT * FROM intakes_table ORDER BY year DESC, month DESC, day_of_month DESC, hour DESC, minute DESC")
    LiveData<List<IntakeInput>> getAllIntakes();

    @Query("SELECT SUM(amount) FROM intakes_table WHERE year = :year AND month = :month AND day_of_month = :dayOfMonth")
    LiveData<Integer> getDailyTotal(int year, int month, int dayOfMonth);
}