package fi.metropolia.christro.juo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;

public class IntakeListview extends AppCompatActivity {
    private JuoViewModel viewModel;
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
        viewModel.getAllIntakeInputs().observe(this, new Observer<List<IntakeEntity>>() {
            @Override
            // Every change in the records will trigger it
            public void onChanged(List<IntakeEntity> intakeEntities) {
                intakeListAdapter.setIntakeList(intakeEntities);
            }
        });

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
                Toast.makeText(IntakeListview.this,"Record deleted",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(IntakeListview.this,History.class);
        startActivity(intent);
    }
}