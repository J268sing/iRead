package com.example.android.iread;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.iread.data.bookContract;
import com.example.android.iread.data.bookdbHelper;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class LibraryActivity extends AppCompatActivity   {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    Intent intent1 = new Intent(LibraryActivity.this, MainActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_settings:
                    Intent intent2 = new Intent(LibraryActivity.this, SettingsActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_favourite:
                    Intent intent3 = new Intent(LibraryActivity.this, FavouriteActivity.class);
                    startActivity(intent3);
                    return true;
                case R.id.navigation_library:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_library);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_library);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerLibrary);

        //Create an adapter that knows which fragment should be shown on each page
        PagerAdapter viewPagerAdapter_library = new viewPagerAdapter_library(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(viewPagerAdapter_library);
        //Inflate the layout for this fragment
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete) {
            showDeleteConfirmationDialog();
        } else if (id == R.id.authorSort) {
           myeBooksFragment.sortconstant = 1;
            Intent intent = new Intent(LibraryActivity.this, LibraryActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Sorted by Authors Name", Toast.LENGTH_SHORT).show();
        } else {
            myeBooksFragment.sortconstant = 2;
            Intent intent = new Intent(LibraryActivity.this, LibraryActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Sorted by Book Name", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    //Alert dialog to delete all the rows from the bookContract.bookEntry.TABLE_NAME table
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("All Books Will Be Deleted?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for (int i = 0; i < myeBooksFragment.myeBooksList.size(); i++) {
                    myeBooksFragment.myeBooksList.remove(i);
                }
                for (int j = 0; j < currentReadsFragment.currentReadsList.size(); j++) {
                    currentReadsFragment.currentReadsList.remove(j);
                }
                for (int l = 0; l < recentlyAddedFragment.recentlyAddedList.size(); l++) {
                    recentlyAddedFragment.recentlyAddedList.remove(l);
                }
                for (int l = 0; l < FavouriteActivity.eBookFavouriteList.size(); l++) {
                    FavouriteActivity.eBookFavouriteList.remove(l);
                }
                myeBooksFragment.deleteall();
                Intent intent = new Intent(LibraryActivity.this, LibraryActivity.class);
                startActivity(intent);
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    @Override
    public void onBackPressed() {
        Intent intent4 = new Intent(LibraryActivity.this, MainActivity.class);
        startActivity(intent4);
    }

    @Override
    protected  void onStart(){
        super.onStart();
    }
}