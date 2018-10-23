package com.example.android.iread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_main:
                    Intent intent1 = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_settings:
                    return true;
                case R.id.navigation_favourite:
                    Intent intent3 = new Intent(SettingsActivity.this, FavouriteActivity.class);
                    startActivity(intent3);
                    return true;
                case R.id.navigation_library:
                    Intent intent2 = new Intent(SettingsActivity.this, LibraryActivity.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_settings);


        //intent to profile settings
        Button profileSettings = (Button) findViewById(R.id.profileSettings);
        profileSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //intent to about settings
        Button aboutSettings = (Button) findViewById(R.id.aboutSettings);
        aboutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        //intent to mail
        Button suggestAfriend = (Button) findViewById(R.id.suggestAFriend);
        suggestAfriend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Download -> \"iRead app\" ");
                intent.putExtra(Intent.EXTRA_TEXT, "Hey, \n Download this great app for reading books");
                Intent chooser = Intent.createChooser(intent, "Send Email");
                startActivity(chooser);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent4 = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent4);
    }
}



