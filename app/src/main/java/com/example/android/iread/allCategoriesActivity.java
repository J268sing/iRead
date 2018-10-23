package com.example.android.iread;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class allCategoriesActivity extends AppCompatActivity {
    //declare string arrays to use them for generating random words related to each category
    public ArrayList<String> allCategories      = new ArrayList<String>();
    public ArrayList<String> adventureList      = new ArrayList<String>();
    public ArrayList<String> biographiesList    = new ArrayList<String>();
    public ArrayList<String> comicsList         = new ArrayList<String>();
    public ArrayList<String> detectivesList     = new ArrayList<String>();
    public ArrayList<String> fantasyList        = new ArrayList<String>();
    public ArrayList<String> fictionList        = new ArrayList<String>();
    public ArrayList<String> healthList         = new ArrayList<String>();
    public ArrayList<String> historicalList     = new ArrayList<String>();
    public ArrayList<String> horrorList         = new ArrayList<String>();
    public ArrayList<String> humorList          = new ArrayList<String>();
    public ArrayList<String> loveList           = new ArrayList<String>();
    public ArrayList<String> poetryList         = new ArrayList<String>();
    public ArrayList<String>  religionList      = new ArrayList<String>();
    public ArrayList<String> scienceList        = new ArrayList<String>();
    public ArrayList<String> sciencefictionList = new ArrayList<String>();
    // a variable to check the availability of network connection
    int networkAvailable = 1;
    // a list to contain list of each category
    public    ArrayList<ArrayList<String>> listoflist = new ArrayList<ArrayList<String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);
        //add each category to allCategories list
        allCategories.add("Adventure");
        allCategories.add("Biographies");
        allCategories.add("Comics");
        allCategories.add("Detectives");
        allCategories.add("Fantasy");
        allCategories.add("Fiction");
        allCategories.add("Health");
        allCategories.add("Historical");
        allCategories.add("Horror");
        allCategories.add("Humor");
        allCategories.add("Love");
        allCategories.add("Poetry");
        allCategories.add("Religion");
        allCategories.add("Science");
        allCategories.add("Science Fiction");

        // Get a reference to the ListView, and attach the adapter to the listView.
        final ListView listView = (ListView) findViewById(R.id.allCategories);
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, allCategories);
        listView.setAdapter(adapter);
        initializeList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                networkAvailable = checknetwork();
                if(networkAvailable == 1) {
                    int positionSelected = position;
                    updateui(positionSelected);
                }
            }

        });

        Button subjectAuthor = (Button) findViewById(R.id.subjectAuthor);
        subjectAuthor.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 searchActivity.subjectOrAuthor = 2;
                                                 Toast.makeText(getApplicationContext(), "Type the author whose books you want to search." , Toast.LENGTH_SHORT).show();
                                                 Intent intent = new Intent(allCategoriesActivity.this, searchActivity.class);
                                                 startActivity(intent);
                                             }
                                         }
        );
        final Button searchCategory = (Button) findViewById(R.id.searchCategory);

        searchCategory.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 searchActivity.subjectOrAuthor = 1;
                                                 Toast.makeText(getApplicationContext(), "Type the category you want to search for." , Toast.LENGTH_SHORT).show();
                                                 Intent intent = new Intent(allCategoriesActivity.this, searchActivity.class);
                                                 startActivity(intent);
                                             }
                                         }
        );
    }

    // a helper function to show the results of selecting a particular category
    public void updateui(int positonSelected) {
        if (positonSelected == 1) {
            ShowbooksActivity.subject = 0;
            Random rand = new Random();
            int arbitrary = rand.nextInt(listoflist.get(positonSelected).size());
            searchActivity.searchedBook = listoflist.get(positonSelected).get(arbitrary);
            Intent intent = new Intent(allCategoriesActivity.this, ShowbooksActivity.class);
            startActivity(intent);
        } else {
            Random rand = new Random();
            int arbitrary = rand.nextInt(listoflist.get(positonSelected).size());
            searchActivity.searchedBook = listoflist.get(positonSelected).get(arbitrary);
            Random rand2 = new Random();
            int arbitrary2 = rand2.nextInt(1);
            ShowbooksActivity.subject = arbitrary2;
            Intent intent = new Intent(allCategoriesActivity.this, ShowbooksActivity.class);
            startActivity(intent);
        }
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
    public void initializeList(){
        adventureList.add("adventure");
        adventureList.add("trip");
        adventureList.add("scene");
        adventureList.add("camping");
        adventureList.add("vacation trip");
        adventureList.add("harry potter");
        adventureList.add("adventures");
        adventureList.add("movies");

        biographiesList.add("biography");
        biographiesList.add("autobiography");
        biographiesList.add("entrepreneur biography");
        biographiesList.add("inspirational biography");
        biographiesList.add("motivating biography");
        biographiesList.add("scientist biography");
        biographiesList.add("players biography");


        comicsList.add("comics");
        comicsList.add("funny");
        comicsList.add("cartoon");
        comicsList.add("clown");
        comicsList.add("humorist");
        comicsList.add("animation");
        comicsList.add("mockery");
        comicsList.add("animated movie");

        detectivesList.add("suspensebook");
        detectivesList.add("confusion");
        detectivesList.add("doubt");
        detectivesList.add("thriller");
        detectivesList.add("perplexity");
        detectivesList.add("anxiety");
        detectivesList.add("tension");
        detectivesList.add("insecurity");
        detectivesList.add("detective");
        detectivesList.add("sherlockholmes");
        detectivesList.add("dilemma");

        fantasyList.add("delusion");
        fantasyList.add("fantasy");
        fantasyList.add("nightmare");
        fantasyList.add("daydream");
        fantasyList.add("hallucination");
        fantasyList.add("delusion");
        fantasyList.add("envision");
        fantasyList.add("creativity");
        fantasyList.add("fancy");

        fictionList.add("drama");
        fictionList.add("story telling");
        fictionList.add("imagination");
        fictionList.add("tale");
        fictionList.add("story telling");
        fictionList.add("narrative");
        fictionList.add("novel");
        fictionList.add("imagination");
        fictionList.add("myth");
        fictionList.add("fiction");

        healthList.add("fitness");
        healthList.add("health");
        healthList.add("strength");
        healthList.add("energy");
        healthList.add("gym");
        healthList.add("exercise");
        healthList.add("physicaltraining");
        healthList.add("diet");
        healthList.add("muscledevelopment");

        historicalList.add("history");
        historicalList.add("ancient wars");
        historicalList.add("king empires");
        historicalList.add("ancient history");
        historicalList.add("europe history");
        historicalList.add("UK history");
        historicalList.add("indian history");
        historicalList.add("american history");
        historicalList.add("southasian history");

        horrorList.add("horrorstories");
        horrorList.add("scarymovies");
        horrorList.add("devilstories");
        horrorList.add("frighteningstories");
        horrorList.add("horrormovies");
        horrorList.add("deathstories");
        horrorList.add("hauntedstories");
        horrorList.add("horror");

        humorList.add("funny");
        humorList.add("laughingbooks");
        humorList.add("jokes");
        humorList.add("jokesbooks");
        humorList.add("funny");
        humorList.add("prank");
        humorList.add("gag");
        humorList.add("pun");
        humorList.add("wisecrack");

        loveList.add("lovestories");
        loveList.add("lust");
        loveList.add("romantic");
        loveList.add("emotion");
        loveList.add("girlfriend");
        loveList.add("relationship");
        loveList.add("fondness");
        loveList.add("friendship");
        loveList.add("affection");
        loveList.add("dating");

        poetryList.add("sonnetpoems");
        poetryList.add("johnkeatspoems");
        poetryList.add("haikupoems");
        poetryList.add("limerickpoems");
        poetryList.add("williamshakespearepoems");
        poetryList.add("concretepoetry");
        poetryList.add("poems");
        poetryList.add("famouspoems");
        poetryList.add("bestpoems");

        religionList.add("Sikhism");
        religionList.add("Christianity");
        religionList.add("Muslim");
        religionList.add("Hinduism");
        religionList.add("Buddhism");
        religionList.add("religion");
        religionList.add("God");

        scienceList.add("Astronomy");
        scienceList.add("Physics");
        scienceList.add("Chemistry");
        scienceList.add("Science");
        scienceList.add("rockets");
        scienceList.add("math");
        scienceList.add("atoms");
        scienceList.add("universe");
        scienceList.add("science");

        sciencefictionList.add("sciencefiction");
        sciencefictionList.add("futurism");
        sciencefictionList.add("space");
        sciencefictionList.add("spaceopera");
        sciencefictionList.add("spacefiction");
        sciencefictionList.add("sci-fi");
        sciencefictionList.add("spaceodyssey");

        listoflist.add(adventureList);
        listoflist.add(biographiesList);
        listoflist.add(comicsList);
        listoflist.add(detectivesList);
        listoflist.add(fantasyList);
        listoflist.add(fictionList);
        listoflist.add(healthList);
        listoflist.add(historicalList);
        listoflist.add(horrorList);
        listoflist.add(humorList);
        listoflist.add(loveList);
        listoflist.add(poetryList);
        listoflist.add(religionList);
        listoflist.add(scienceList);
        listoflist.add(sciencefictionList);
    }
}
