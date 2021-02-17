package fi.metropolia.christro.juo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = IntakeInput.class, version = 1)
public abstract class IntakeInputDatabase extends RoomDatabase {

    public static final String DATABASE_FILE_NAME = "intake_input_database";

    private static IntakeInputDatabase instance;

    public abstract IntakeInputDao intakeInputDao();

    public static synchronized IntakeInputDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    IntakeInputDatabase.class, DATABASE_FILE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private IntakeInputDao intakeInputDao;

        private PopulateDbAsyncTask(IntakeInputDatabase db) {
            intakeInputDao = db.intakeInputDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            intakeInputDao.insert(new IntakeInput((250)));
            intakeInputDao.insert(new IntakeInput((400)));
            intakeInputDao.insert(new IntakeInput((125)));
            intakeInputDao.insert(new IntakeInput((100)));
            return null;
        }
    }
}
