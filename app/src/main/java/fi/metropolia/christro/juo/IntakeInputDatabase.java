package fi.metropolia.christro.juo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                IntakeInputDao dao = INSTANCE.intakeInputDao();
                dao.deleteAllIntakes();

                IntakeInput intakeInput = new IntakeInput(250);
                dao.insert(intakeInput);
                Log.d("DATABASE_TESTER", "250 inserted.");
                intakeInput = new IntakeInput(500);
                dao.insert(intakeInput);
                Log.d("DATABASE_TESTER", "500 inserted.");

            });
        }
    };
}
