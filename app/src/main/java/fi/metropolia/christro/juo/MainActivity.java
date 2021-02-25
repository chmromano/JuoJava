package fi.metropolia.christro.juo;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity {

    private IntakeInputViewModel intakeInputViewModel;

    private IntakeInputRepository repository;

    private CircularProgressBar circularProgressBar;

    private TextView textView;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //navigation
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ////
        repository = new IntakeInputRepository(this.getApplication());

        textView = findViewById(R.id.intakeText);
        circularProgressBar = findViewById(R.id.circularProgressBar);

        intakeInputViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(IntakeInputViewModel.class);

        final Observer<Integer> dailyTotalObserver = newTotal -> {
            // Update the UI, in this case, a TextView.
            textView.setText(String.valueOf(newTotal));

            if(newTotal != null) {
                circularProgressBar.setProgressWithAnimation(newTotal, (long) 300);
            }
        };

        intakeInputViewModel.getDailyTotal().observe(this, dailyTotalObserver);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(view -> {
            repository.insert(new IntakeInput(400));
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(view -> {
            repository.insert(new IntakeInput(250));
        });
    }
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            //means the drawer is open
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}