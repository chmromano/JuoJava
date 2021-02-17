package fi.metropolia.christro.juo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IntakeInputDao {

    @Insert
    void insert(IntakeInput intakeInput);

    @Update
    void update(IntakeInput intakeInput);

    @Delete
    void delete(IntakeInput intakeInput);

    @Query("DELETE FROM intakes_table")
    void deleteAllIntakes();

    @Query("SELECT * FROM intakes_table ORDER BY year DESC, month DESC, dayOfMonth DESC, hour DESC, minute DESC")
    LiveData<List<IntakeInput>> getAllIntakes();
}
