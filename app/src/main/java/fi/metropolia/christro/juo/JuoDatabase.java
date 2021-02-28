package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;
import android.util.Log;

//Create database.
@Database(entities = {IntakeEntity.class, MoodEntity.class}, version = 1, exportSchema = false)
public abstract class JuoDatabase extends RoomDatabase {

    //Database file name.
    public static final String DATABASE_NAME = "juo_database";

    //Database is a singleton.
    private static volatile JuoDatabase INSTANCE;

    //Create an ExecutorService to handle database operation on background threads. UI operation
    //won't be affected.
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //Return database instance. If there is no database yet creates one.
    static JuoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (JuoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            JuoDatabase.class, DATABASE_NAME)
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract JuoDao juoDao();




    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                JuoDao dao = INSTANCE.juoDao();
                dao.deleteAllIntakes();

                IntakeEntity intakeEntity = new IntakeEntity(250);
                //dao.insert(intakeInput);
                Log.d("DATABASE_TESTER", "250 inserted.");
                intakeEntity = new IntakeEntity(500);
                //dao.insert(intakeInput);
                Log.d("DATABASE_TESTER", "500 inserted.");

            });
        }
    };
}
