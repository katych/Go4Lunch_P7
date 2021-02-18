package com.example.go4lunch.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.core.view.GravityCompat.START;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;


    @Override
    public int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        configureDrawerLayout();
        configureNavigationView();
        configureBottomNavigation();
        this.configureToolBar(getString(R.string.I_m_hungry));
    }

    /**
     * config drawerLayout
     */
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /**
     * Configuration Navigation View
     */
    public void configureNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.drawer_yourLunch:
                Toast.makeText(this, "Lunch ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.drawer_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_LONG).show();
                break;
            case R.id.drawer_logOut:
                Toast.makeText(this, "logout ", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    public void configureBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mapView:
                    Toast.makeText(this, "mapView ", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.listView:
                    Toast.makeText(this, "listView ", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.workmates:
                    Toast.makeText(this, "workmates ", Toast.LENGTH_SHORT).show();
                  break;
            }
            // Closes the DrawerNavigationView when the user click on an item
            if (this.drawerLayout.isDrawerOpen(START)) {
                this.drawerLayout.closeDrawer(START);
            }
            return true;
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creates a MenuInflater to add the menu xml file into the Toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        configureDrawerLayout();
        return true;
    }
}