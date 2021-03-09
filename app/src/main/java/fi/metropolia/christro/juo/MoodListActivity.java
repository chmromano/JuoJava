package fi.metropolia.christro.juo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import fi.metropolia.christro.juo.database.JuoViewModel;

/**
 * MoodList activity of the application. Display all moods in a RecyclerView.
 *
 * @author Christopher Mohan Romano
 * @author Itale Tabksmane
 * @version 1.0
 */
//Itale Tabaksmane - Implemented navigation menu and all related methods.
public class MoodListActivity extends AppCompatActivity {

    /**
     * onCreate() method creates the activity.
     *
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState(Bundle).
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_list);

        JuoViewModel juoViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(JuoViewModel.class);

        //Initialise RecyclerView and set custom adapter.
        RecyclerView recyclerViewMoodList = findViewById(R.id.recyclerViewMoodList);
        recyclerViewMoodList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMoodList.setHasFixedSize(false);
        MoodListAdapter moodListAdapter = new MoodListAdapter();
        recyclerViewMoodList.setAdapter(moodListAdapter);
        juoViewModel.getAllMoodInputs().observe(this, moodListAdapter::setMoodList);

        /*
        This code is used to implement the navigation menu.
        https://www.youtube.com/watch?v=HwYENW0RyY4
        https://www.youtube.com/watch?v=fGcMLu1GJEc
        https://www.youtube.com/watch?v=zYVEMCiDcmY
        https://www.youtube.com/watch?v=bjYstsO1PgI
        https://www.youtube.com/watch?v=lt6xbth-yQo
         */
        DrawerLayout drawerLayoutMoodListActivity = findViewById(R.id.drawerLayoutMoodList);
        NavigationView navigationViewMoodList = findViewById(R.id.navigationViewMoodList);
        Toolbar toolbarMoodList = findViewById(R.id.toolbarMoodList);
        setSupportActionBar(toolbarMoodList);
        if (savedInstanceState == null) {
            navigationViewMoodList.setCheckedItem(R.id.nav_mood);
        }
        // Without this statement many devices will not show the menu as clickable
        navigationViewMoodList.bringToFront();
        // making a menu icon click open the side navigation drawer
        ImageButton menuButton = findViewById(R.id.buttonNavigationMenu);
        menuButton.setOnClickListener(view -> drawerLayoutMoodListActivity.openDrawer(GravityCompat.START));
        // using animation whenever the menu opens, by swiping or clicking
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayoutMoodListActivity, toolbarMoodList,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutMoodListActivity.addDrawerListener(toggle);
        toggle.syncState();

        // creates an intent for the appropriate activity by matching with item ID
        navigationViewMoodList.setNavigationItemSelectedListener(item -> {
            Intent intent = null;

            if (item.getItemId() == R.id.nav_home) {
                intent = new Intent(MoodListActivity.this, MainActivity.class);
            } else if (item.getItemId() == R.id.nav_home) {
                intent = new Intent(MoodListActivity.this, MainActivity.class);
            } else if (item.getItemId() == R.id.nav_history) {
                intent = new Intent(MoodListActivity.this, History.class);
            } else if (item.getItemId() == R.id.nav_location) {
                intent = new Intent(MoodListActivity.this, LocationActivity.class);
            } else if (item.getItemId() == R.id.nav_about) {
                intent = new Intent(MoodListActivity.this, AboutActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
            }

            drawerLayoutMoodListActivity.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * Method overrides normal onBackPressed() in case the drawer menu is open.
     */
    @Override
    public void onBackPressed() {
        if (((DrawerLayout) findViewById(R.id.drawerLayoutMoodList)).isDrawerOpen(GravityCompat.START)) {
            //means the drawer is open
            ((DrawerLayout) findViewById(R.id.drawerLayoutMoodList)).closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}