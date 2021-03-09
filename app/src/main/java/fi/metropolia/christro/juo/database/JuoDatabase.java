package fi.metropolia.christro.juo.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

/**
 * Database with a table for IntakeEntity and a table for MoodEntity. Database is abstract class
 * that extends RoomDatabase. Database migrations are not planned so export schema is set to false.
 *
 * @author Christopher Mohan Romano
 * @version 1.0
 */
//Guide used: https://developer.android.com/codelabs/android-room-with-a-view#7
@Database(entities = {IntakeEntity.class, MoodEntity.class}, version = 1, exportSchema = false)
public abstract class JuoDatabase extends RoomDatabase {

    /**
     * Expose DAO to the database.
     */
    public abstract JuoDao juoDao();
    /**
     * Database file name.
     */
    public static final String DATABASE_NAME = "juo_database";
    /**
     * Database instance. Singleton.
     */
    private static volatile JuoDatabase juoDatabaseInstance;
    /**
     * Number of threads to use for ExecutorService.
     */
    private static final int NUMBER_OF_THREADS = 4;
    /**
     * ExecutorService to execute database operations in the background.
     */
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Method returns the database singleton. If there is no database yet a database is created.
     *
     * @param context Context refers to the application.
     * @return Returns an instance of the database.
     */
    static JuoDatabase getDatabase(final Context context) {
        if (juoDatabaseInstance == null) {
            synchronized (JuoDatabase.class) {
                if (juoDatabaseInstance == null) {
                    juoDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            JuoDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return juoDatabaseInstance;
    }
}
