package com.example.android.iread;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class searchActivity extends AppCompatActivity {
    public static String searchedBook = "";

   public static int subjectOrAuthor = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Button searchresult = (Button) findViewById(R.id.searchResult);
        searchresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchBook = (EditText) findViewById(R.id.searchBook);
                searchedBook =  searchBook.getText().toString().trim();
                Log.i("prabh"  ,   "  ----  " + searchedBook);
               searchedBook =  searchedBook.replace(" ", "%20");
               if(subjectOrAuthor == 0) {
                   ShowbooksActivity.subject = 0;
               } else if (subjectOrAuthor == 1){
                   ShowbooksActivity.subject = 1;
               } else {
                   ShowbooksActivity.subject = 2;
               }
                ConnectivityManager checkConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo checkNetwork = checkConnectivity.getActiveNetworkInfo();
                if (checkNetwork == null || !checkNetwork.isConnected()) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else if (searchActivity.searchedBook.equals("")) {
                    Toast.makeText(getApplicationContext(), "No Book Searched", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), searchedBook, Toast.LENGTH_SHORT).show();
                    Intent intent = new  Intent(searchActivity.this, ShowbooksActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
 