package fi.metropolia.christro.juo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import java.util.List;

public class IntakeInputRepository {
    private IntakeInputDao intakeInputDao;
    private LiveData<List<IntakeInput>> allIntakeInputs;

    public IntakeInputRepository(Application application){
        IntakeInputDatabase database = IntakeInputDatabase.getInstance(application);
        intakeInputDao = database.intakeInputDao();
        allIntakeInputs = intakeInputDao.getAllIntakes();
    }

    public void insert(IntakeInput intakeInput) {
        new InsertIntakeInputAsyncTask(intakeInputDao).execute(intakeInput);
    }

    public void update(IntakeInput intakeInput) {
        new UpdateIntakeInputAsyncTask(intakeInputDao).execute(intakeInput);
    }

    public void delete(IntakeInput intakeInput) {
        new DeleteIntakeInputAsyncTask(intakeInputDao).execute(intakeInput);
    }

    public void deleteAllIntakes() {
        new DeleteAllIntakesAsyncTask(intakeInputDao).execute();
    }

    public LiveData<List<IntakeInput>> getAllIntakeInputs() {
        return allIntakeInputs;
    }

    private static class InsertIntakeInputAsyncTask extends AsyncTask<IntakeInput, Void, Void> {
        private IntakeInputDao intakeInputDao;

        private InsertIntakeInputAsyncTask(IntakeInputDao intakeInputDao) {
            this.intakeInputDao = intakeInputDao;
        }

        @Override
        protected Void doInBackground(IntakeInput... intakeInputs) {
            intakeInputDao.insert(intakeInputs[0]);
            return null;
        }
    }

    private static class UpdateIntakeInputAsyncTask extends AsyncTask<IntakeInput, Void, Void> {
        private IntakeInputDao intakeInputDao;

        private UpdateIntakeInputAsyncTask(IntakeInputDao intakeInputDao) {
            this.intakeInputDao = intakeInputDao;
        }

        @Override
        protected Void doInBackground(IntakeInput... intakeInputs) {
            intakeInputDao.update(intakeInputs[0]);
            return null;
        }
    }

    private static class DeleteIntakeInputAsyncTask extends AsyncTask<IntakeInput, Void, Void> {
        private IntakeInputDao intakeInputDao;

        private DeleteIntakeInputAsyncTask(IntakeInputDao intakeInputDao) {
            this.intakeInputDao = intakeInputDao;
        }

        @Override
        protected Void doInBackground(IntakeInput... intakeInputs) {
            intakeInputDao.delete(intakeInputs[0]);
            return null;
        }
    }

    private static class DeleteAllIntakesAsyncTask extends AsyncTask<Void, Void, Void> {
        private IntakeInputDao intakeInputDao;

        private DeleteAllIntakesAsyncTask(IntakeInputDao intakeInputDao) {
            this.intakeInputDao = intakeInputDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            intakeInputDao.deleteAllIntakes();
            return null;
        }
    }
}
