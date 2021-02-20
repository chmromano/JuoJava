package fi.metropolia.christro.juo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import java.util.List;

public class ListIntakeInputActivity extends AppCompatActivity {
    private IntakeInputViewModel intakeInputViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_intake_input);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final IntakeInputListAdapter adapter = new IntakeInputListAdapter(new IntakeInputListAdapter.IntakeInputDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}