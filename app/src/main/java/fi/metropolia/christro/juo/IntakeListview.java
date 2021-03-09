package fi.metropolia.christro.juo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fi.metropolia.christro.juo.database.JuoViewModel;

/**
 * IntakeListView activity. Calls the adapter for the list then inserts live data of all the intakes.
 * Allows swiping for deleting records
 *
 * @author Itale Tabaksmane
 * @version 1.0
 */
public class IntakeListview extends AppCompatActivity {
    /**
     * JuoViewModel to use the exiting instance of the database
     */
    private JuoViewModel viewModel;

    /**
     * onCreate() - creates the activity
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_listview);
        // This sets the list of records under a flexible view without the limitation on data
        RecyclerView recyclerView = findViewById(R.id.recycler_view_intake_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        //
        // Setting the adapter for the list of records
        IntakeListAdapter intakeListAdapter = new IntakeListAdapter();
        recyclerView.setAdapter(intakeListAdapter);
        // Calling the JuoViewModel instance
        viewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);
        // Using the live data inputs from the user
        // Every change in the records will trigger it
        viewModel.getAllIntakeInputs().observe(this, intakeListAdapter::setIntakeList);

        // This function allows swiping to delete the record
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.deleteIntake(intakeListAdapter.getIntakeAt(viewHolder.getAdapterPosition()));
                Toast.makeText(IntakeListview.this, "Record deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    /**
     * Overrides onBackPressed()
     * uses the method to return to the history activity
     * when the user presses the 'back' button on their phone
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(IntakeListview.this, History.class);
        startActivity(intent);
    }
}