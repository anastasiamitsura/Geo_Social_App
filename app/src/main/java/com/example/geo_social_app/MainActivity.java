package com.example.geo_social_app;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.geo_social_app.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding.navV.setOnItemSelectedListener(itemSelectListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerrrr, new Main_Fragment()).commit();
    }


    private NavigationBarView.OnItemSelectedListener itemSelectListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment=null;
            switch (item.getItemId()){
                case R.id.home_bt:
                    selectedFragment = new Main_Fragment();
                    break;
                case R.id.ways_bt:
                    selectedFragment = new Ways_Fragment();
                    break;
                case R.id.create_bt:
                    selectedFragment = new Create_Way_Fragment();
                    break;
                case R.id.search_bt:
                    selectedFragment = new Search_Fragment();
                    break;
                case R.id.profile_bt:
                    selectedFragment = new Profile_Fragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.containerrrr, selectedFragment).commit();
            return true;
        }
    };
}