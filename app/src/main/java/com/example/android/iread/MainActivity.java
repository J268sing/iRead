package com.example.android.iread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //declare string arrays to use them for generating random words related to each category
    public ArrayList<String> mostPopularList = new ArrayList<String>();
    public ArrayList<String> fictionList = new ArrayList<String>();
    public ArrayList<String> businessList = new ArrayList<String>();
    public ArrayList<String> scienceList = new ArrayList<String>();
    public ArrayList<String> poetryList = new ArrayList<String>();
    private BottomNavigationView navigation;

    // a variable to check the availability of network connection
    int networkAvailable = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Search:
                    if(R.id.Search != 0){
                        Log.d("prabh" , "  ----  ");
                    }
                    return true;
                case R.id.navigation_main:
                    return true;
                case R.id.navigation_settings:
                    Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_favourite:
                    Intent intent3 = new Intent (MainActivity.this, FavouriteActivity.class);
                    startActivity(intent3);
                    navigation.getMenu().findItem(R.id.navigation_favourite).setChecked(true);
                    return true;
                case R.id.navigation_library:
                    Intent intent4 = new Intent(MainActivity.this, LibraryActivity.class);
                    startActivity(intent4);
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Button allCategoriesHome = (Button) findViewById(R.id.allCategoriesHome);
        listInitializer();
        allCategoriesHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, allCategoriesActivity.class);
                startActivity(intent);
            }
        });

        Button mostPopular = (Button) findViewById(R.id.mostPopular);
        mostPopular.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                networkAvailable = checknetwork();
                if(networkAvailable == 1) {
                    Random rand = new Random();
                    int arbitrary = rand.nextInt(11);
                    Random rand2 = new Random();
                    int arbitrary2 = rand2.nextInt(1);
                    ShowbooksActivity.subject = arbitrary2;
                    searchActivity.searchedBook = mostPopularList.get(arbitrary);
                    Intent intent = new Intent(MainActivity.this, ShowbooksActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Open Google", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button fiction = (Button) findViewById(R.id.fiction);
        fiction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                networkAvailable = checknetwork();
                if(networkAvailable == 1) {
                    Random rand = new Random();
                    int arbitrary = rand.nextInt(10);
                    Random rand2 = new Random();
                    int arbitrary2 = rand2.nextInt(1);
                    ShowbooksActivity.subject = arbitrary2;
                    searchActivity.searchedBook = fictionList.get(arbitrary);
                    Intent intent = new Intent(MainActivity.this, ShowbooksActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Open Google", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button business = (Button) findViewById(R.id.businessBooks);
        business.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                networkAvailable = checknetwork();
                if(networkAvailable == 1) {
                    Random rand = new Random();
                    int arbitrary = rand.nextInt(12);
                    Random rand2 = new Random();
                    int arbitrary2 = rand2.nextInt(1);
                    ShowbooksActivity.subject = arbitrary2;
                    searchActivity.searchedBook = businessList.get(arbitrary);
                    Intent intent = new Intent(MainActivity.this, ShowbooksActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Open Google", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button poetry = (Button) findViewById(R.id.poetry);
        poetry.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                networkAvailable = checknetwork();
                if(networkAvailable == 1) {
                    Random rand = new Random();
                    int arbitrary = rand.nextInt(9);
                    Random rand2 = new Random();
                    int arbitrary2 = rand2.nextInt(1);
                    ShowbooksActivity.subject = arbitrary2;
                    searchActivity.searchedBook = poetryList.get(arbitrary);
                    Intent intent = new Intent(MainActivity.this, ShowbooksActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Open Google", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button science = (Button) findViewById(R.id.science);
        science.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                networkAvailable = checknetwork();
                if(networkAvailable == 1) {
                    Random rand = new Random();
                    int arbitrary = rand.nextInt(9);
                    Random rand2 = new Random();
                    int arbitrary2 = rand2.nextInt(1);
                    ShowbooksActivity.subject = arbitrary2;
                    searchActivity.searchedBook = scienceList.get(arbitrary);
                    Intent intent = new Intent(MainActivity.this, ShowbooksActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Open Google", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_search, menu);
        MenuItem item = menu.findItem(R.id.Search);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener ()
                                        {
                                            public boolean onMenuItemClick(MenuItem item) {
                                                searchActivity.subjectOrAuthor = 0;
                                                Intent intent = new Intent(MainActivity.this, searchActivity.class);
                                                startActivity(intent);
                                                return true;
                                            }}
        );
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    // a helper function to check if internet is available
    public int checknetwork(){
        ConnectivityManager checkConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo checkNetwork = checkConnectivity.getActiveNetworkInfo();
        if (checkNetwork == null || !checkNetwork.isConnected()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            return 1;
        }
    }

    // a helper function to initialize all the lists with synonyms of each category
    public void listInitializer(){
        mostPopularList.add("attractive");
        mostPopularList.add("famous");
        mostPopularList.add("prominent");
        mostPopularList.add("suitable");
        mostPopularList.add("trendy");
        mostPopularList.add("prevailing");
        mostPopularList.add("crowdpleasing");
        mostPopularList.add("indemand");
        mostPopularList.add("liked");
        mostPopularList.add("mostselling");
        mostPopularList.add("mostpopular");
        mostPopularList.add("mostpraised");

        fictionList.add("drama");
        fictionList.add("storytelling");
        fictionList.add("imagination");
        fictionList.add("tale");
        fictionList.add("storytelling");
        fictionList.add("narrative");
        fictionList.add("novel");
        fictionList.add("imagination");
        fictionList.add("myth");
        fictionList.add("fiction");

        businessList.add("fiction");
        businessList.add("entrepreneur");
        businessList.add("trade");
        businessList.add("stockmarket");
        businessList.add("banking");
        businessList.add("wealth");
        businessList.add("investment");
        businessList.add("estate");
        businessList.add("stocks");
        businessList.add("money");
        businessList.add("sales");
        businessList.add("business");

        scienceList.add("Astronomy");
        scienceList.add("Physics");
        scienceList.add("Chemistry");
        scienceList.add("Science");
        scienceList.add("rockets");
        scienceList.add("math");
        scienceList.add("atoms");
        scienceList.add("universe");
        scienceList.add("science");

        poetryList.add("sonnetpoems");
        poetryList.add("johnkeatspoems");
        poetryList.add("haikupoems");
        poetryList.add("limerickpoems");
        poetryList.add("williamshakespearepoems");
        poetryList.add("concretepoetry");
        poetryList.add("poems");
        poetryList.add("famouspoems");
        poetryList.add("bestpoems");
    }
}
