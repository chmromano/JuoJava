package fi.metropolia.christro.juo;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import fi.metropolia.christro.juo.database.IntakeEntity;
import fi.metropolia.christro.juo.database.JuoViewModel;
import fi.metropolia.christro.juo.database.MoodEntity;

public class MoodListActivity extends AppCompatActivity {

    private JuoViewModel juoViewModel;

    private DrawerLayout drawerLayoutMoodListActivity;
    private Toolbar toolbarMoodList;
    private NavigationView navigationViewMoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_list);

        RecyclerView recyclerViewMoodList = findViewById(R.id.recyclerViewMoodList);
        recyclerViewMoodList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMoodList.setHasFixedSize(false);

        MoodListAdapter moodListAdapter = new MoodListAdapter();
        recyclerViewMoodList.setAdapter(moodListAdapter);

        juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);
        juoViewModel.getAllMoodInputs().observe(this, new Observer<List<MoodEntity>>() {
            @Override
            public void onChanged(List<MoodEntity> moodEntities) {
                moodListAdapter.setMoodList(moodEntities);
            }
        });

        //This code is used to implement the navigation bar.
        drawerLayoutMoodListActivity = findViewById(R.id.drawerLayoutMoodListActivity);
        navigationViewMoodList = findViewById(R.id.navigationViewMoodList);
        toolbarMoodList = findViewById(R.id.toolbarMoodList);
        setSupportActionBar(toolbarMoodList);
        if (savedInstanceState == null) {
            navigationViewMoodList.setCheckedItem(R.id.nav_mood);
        }
        navigationViewMoodList.bringToFront();

        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> drawerLayoutMoodListActivity.openDrawer(GravityCompat.START));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutMoodListActivity, toolbarMoodList,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutMoodListActivity.addDrawerListener(toggle);
        toggle.syncState();

        navigationViewMoodList.setNavigationItemSelectedListener(item -> {
            //item = navigationView.getCheckedItem();
            Intent intent;
            //Context context = getApplicationContext();
            switch (item.getItemId()) {
                case R.id.nav_home:
                    intent = new Intent(MoodListActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_history:
                    intent = new Intent(MoodListActivity.this, History.class);
                    startActivity(intent);
                    break;
                case R.id.nav_mood:
                    break;
                case R.id.nav_location:
                    intent = new Intent(MoodListActivity.this, LocationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_settings:
                    intent = new Intent(MoodListActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_about:
                    intent = new Intent(MoodListActivity.this, AboutActivity.class);
                    startActivity(intent);
                    break;
            }
            drawerLayoutMoodListActivity.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutMoodListActivity.isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            drawerLayoutMoodListActivity.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}