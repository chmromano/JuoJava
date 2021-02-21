package fi.metropolia.christro.juo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListIntakeInputActivity extends AppCompatActivity {

    private IntakeInputViewModel intakeInputViewModel;

    public static final int NEW_INTAKE_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_intake_input);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final IntakeInputListAdapter adapter = new IntakeInputListAdapter(new IntakeInputListAdapter.IntakeInputDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        intakeInputViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(IntakeInputViewModel.class);

        intakeInputViewModel.getAllIntakeInputs().observe(this, intakeInputs -> adapter.submitList(intakeInputs));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(ListIntakeInputActivity.this, NewInputActivity.class);
            startActivityForResult(intent, NEW_INTAKE_ACTIVITY_REQUEST_CODE);
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_INTAKE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            IntakeInput intakeInput = new IntakeInput(data.getIntExtra(NewInputActivity.EXTRA_REPLY, 0));
            intakeInputViewModel.insert(intakeInput);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}