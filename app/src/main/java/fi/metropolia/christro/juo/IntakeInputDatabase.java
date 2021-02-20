package fi.metropolia.christro.juo;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = IntakeInput.class, version = 1, exportSchema = false)
public abstract class IntakeInputDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "intake_input_database";

    public abstract IntakeInputDao intakeInputDao();

    private static volatile IntakeInputDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static IntakeInputDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (IntakeInputDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            IntakeInputDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
