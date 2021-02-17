package fi.metropolia.christro.juo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class ListIntakeInputActivity extends AppCompatActivity {
    private IntakeInputViewModel intakeInputViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_intake_input);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        IntakeInputAdapter adapter = new IntakeInputAdapter();
        recyclerView.setAdapter(adapter);

        intakeInputViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(IntakeInputViewModel.class);
        intakeInputViewModel.getAllIntakes().observe(this, new Observer<List<IntakeInput>>() {
            @Override
            public void onChanged(@Nullable List<IntakeInput> intakeInputs) {
                //Update recyclerView
                adapter.setIntakes(intakeInputs);
            }
        });
    }
}