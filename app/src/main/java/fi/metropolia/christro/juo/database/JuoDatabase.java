package fi.metropolia.christro.juo.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

//Guide used: https://developer.android.com/codelabs/android-room-with-a-view#7

/**
 * Database with a table for IntakeEntity and a table for MoodEntity. Database is abstract class
 * that extends RoomDatabase. Database migrations are not planned so export schema is set to false.
 * @author Christopher Mohan Romano
 * @version 1.0
 */
@Database(entities = {IntakeEntity.class, MoodEntity.class}, version = 1, exportSchema = false)
public abstract class JuoDatabase extends RoomDatabase {

    //Expose DAOs to the database.
    public abstract JuoDao juoDao();
    //Database file name.
    public static final String DATABASE_NAME = "juo_database";
    //Database as a singleton.
    private static volatile JuoDatabase juoDatabaseInstance;
    //Create an ExecutorService to handle database operation on background threads.This way UI
    //operations won't be affected.
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Method returns the database singleton. If there is no database yet a database is created.
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
