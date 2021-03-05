package fi.metropolia.christro.juo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;

public class IntakeListview extends AppCompatActivity {
    private JuoViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_listview);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_intake_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        IntakeListAdapter intakeListAdapter = new IntakeListAdapter();
        recyclerView.setAdapter(intakeListAdapter);

        viewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);
        viewModel.getAllIntakeInputs().observe(this, new Observer<List<IntakeEntity>>() {
            @Override
            public void onChanged(List<IntakeEntity> intakeEntities) {
                intakeListAdapter.setIntakeList(intakeEntities);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.deleteIntake(intakeListAdapter.getIntakeAt(viewHolder.getAdapterPosition()));
                Toast.makeText(IntakeListview.this,"record deleted",Toast.LENGTH_SHORT);
            }
        }).attachToRecyclerView(recyclerView);
    }
}