package com.example.bbcnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AlertDialog.Builder builder;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        //initiate toolbar/drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        builder = new AlertDialog.Builder(this);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        //sets to home frag when opening the app
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                    HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home_nav);
        }
    }

    //Closes drawer on back press
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //navigation drawer item selection
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavoriteFragment()).commit();
                break;
            //alert dialog
            case R.id.help_item:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.help_text))
                        .setMessage(getString(R.string.how_to) + " " +getString(R.string.how_to2) + " " +
                                getString(R.string.how_to3))
                        .setNegativeButton("Ok", null)
                        .show();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                 //       new HelpFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}