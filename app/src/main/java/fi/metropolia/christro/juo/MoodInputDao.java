package fi.metropolia.christro.juo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface MoodInputDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMood(MoodInput moodInput);
}
