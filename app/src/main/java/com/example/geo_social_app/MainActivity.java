package com.example.geo_social_app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.geo_social_app.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    private FirebaseListAdapter<Search_Fragment> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                // Here, no request code
                                Intent data = result.getData();
                                AuthUI.getInstance().createSignInIntentBuilder().build();
                            }
                        }
                    });
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                            "Welcome " + FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getDisplayName(),
                            Toast.LENGTH_LONG)
                    .show();
            // Load chat room contents
            displayChatMessages();
        }

        binding.navV.setOnItemSelectedListener(itemSelectListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerrrr, new Main_Fragment()).commit();
    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<Search_Fragment>(this, Search_Fragment.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Search_Fragment model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Toast.makeText(MainActivity.this, "Successfully signed in. Welcome!", Toast.LENGTH_LONG).show();
                        displayChatMessages();

                    }
                    else {
                        Toast.makeText(MainActivity.this, "We couldn't sign you in. Please try again later.", Toast.LENGTH_LONG).show();
                        // Close the app
                        finish();
                    }
                }

            });


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