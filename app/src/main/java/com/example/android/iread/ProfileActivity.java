package com.example.android.iread;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.iread.data.bookContract;
import com.example.android.iread.data.bookdbHelper;

import org.w3c.dom.Text;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {

    String userName = "";
    String favAuthor = "";
    String favQuote = "";
    int id = 1;
    int id2 = 1;
    int id3 = 1;
    Cursor cursord;
    int x ;
    public static bookdbHelper mdbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mdbhelper = new bookdbHelper(this);
        final SQLiteDatabase db = mdbhelper.getWritableDatabase();

        final TextView favauthorid = (TextView) findViewById(R.id.favAuthorid);
        final Button favauthor = (Button) findViewById(R.id.favauthor);

        //save or edit the users favourite author
        favauthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchBook = (EditText) findViewById(R.id.favAuthor);

                favAuthor = searchBook.getText().toString().trim();

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) favauthorid.getLayoutParams();
                if(id == 0) {

                    params.weight = 4.005f;
                    ContentValues values = new ContentValues();
                    values.put(bookContract.profileEntry.COLUMN_FAV_AUTHOR, favAuthor);
                    String[] selectionArgs1 = {Integer.toString(1)};
                    db.update(bookContract.profileEntry.TABLE_NAME, values, bookContract.profileEntry._ID + "= ?", selectionArgs1);
                    Intent intent4 = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(intent4);
                    id = 1;

                } else{
                    params.weight = 0;
                    favauthorid.setText("");
                    searchBook.setVisibility(View.VISIBLE);
                    favauthor.setText("Save");
                    id = 0;
                }


            }
    });


        //save or edit the users name
        final TextView usernameid = (TextView) findViewById(R.id.usernameid);
        final Button username = (Button) findViewById(R.id.username);
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchBook2 = (EditText) findViewById(R.id.userName);

                userName = searchBook2.getText().toString().trim();
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) usernameid.getLayoutParams();
                if(id2 == 0) {

                    params2.weight = 4.005f;
                    ContentValues values = new ContentValues();
                    values.put(bookContract.profileEntry.COLUMN_NAME, userName);
                    String[] selectionArgs1 = {Integer.toString(1)};
                    db.update(bookContract.profileEntry.TABLE_NAME, values, bookContract.profileEntry._ID + "= ?", selectionArgs1);
                    Intent intent4 = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(intent4);
                    id2 = 1;
                } else{
                    params2.weight = 0;
                    usernameid.setText("");
                    searchBook2.setVisibility(View.VISIBLE);
                    username.setText("Save");
                    id2 = 0;
                }

            }
        });

        // save or edit the users favourite quote
        final TextView favquoteid = (TextView) findViewById(R.id.favquoteid);
        final Button favquote = (Button) findViewById(R.id.favquote);
        favquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText searchBook3 = (EditText) findViewById(R.id.favQuote);
                favQuote = searchBook3.getText().toString().trim();
                LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) favquoteid.getLayoutParams();
                if(id3 == 0) {
                    params4.weight = 4.005f;
                    ContentValues values = new ContentValues();
                    values.put(bookContract.profileEntry.COLUMN_FAV_QUOTE, favQuote);
                    String[] selectionArgs1 = {Integer.toString(1)};
                    db.update(bookContract.profileEntry.TABLE_NAME, values, bookContract.profileEntry._ID + "= ?", selectionArgs1);
                    Intent intent4 = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(intent4);
                    id3 = 1;

                } else{
                   // LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) favquoteid.getLayoutParams();
                    params4.weight = 0;
                    favquoteid.setText("");
                    searchBook3.setVisibility(View.VISIBLE);
                    favquote.setText("Save");
                    id3 = 0;
                }

            }
        });
         displayDatabaseInfo();
    }



    // this helper function fills COLUMN_NAME , COLUMN_FAV_AUTHOR, COLUMN_FAV_QUOTE with empty string
    public void insert(){
        SQLiteDatabase db = mdbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(bookContract.profileEntry.COLUMN_NAME, "");
        values.put(bookContract.profileEntry.COLUMN_FAV_AUTHOR, "");
        values.put(bookContract.profileEntry.COLUMN_FAV_QUOTE, "");

        long newRowId = db.insert(bookContract.profileEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed(){
        Intent intent4 = new Intent(ProfileActivity.this, SettingsActivity.class);
        startActivity(intent4);
    }

    //display the user's saved name , favourite author and favourite quote
    private void displayDatabaseInfo() {
        SQLiteDatabase db = mdbhelper.getReadableDatabase();
        String[] projection = {
                bookContract.profileEntry._ID,
                bookContract.profileEntry.COLUMN_NAME,
                bookContract.profileEntry.COLUMN_FAV_AUTHOR,
                bookContract.profileEntry.COLUMN_FAV_QUOTE};

        String[] selectionArgs1 = {Integer.toString(1)};

        //get name , fav author and fav quote in a cursor
        cursord = db.query(
                bookContract.profileEntry.TABLE_NAME,
                projection,// The columns to return for each row
                bookContract.profileEntry._ID + "= ?", // Selection criteria
                selectionArgs1, // Selection criteria
                null,
                null,
                null);


        int nameindex = cursord.getColumnIndex(bookContract.profileEntry.COLUMN_NAME);
        int authorindex = cursord.getColumnIndex(bookContract.profileEntry.COLUMN_FAV_AUTHOR);
        int quoteindex = cursord.getColumnIndex(bookContract.profileEntry.COLUMN_FAV_QUOTE);
        x = cursord.getCount();
        if(x == 0){
            insert();
            Intent intent4 = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intent4);
        } else {
            cursord.moveToFirst();

            // set the name area with the name stored in the database
            String myname = cursord.getString(nameindex);
            final TextView usernameid = (TextView) findViewById(R.id.usernameid);
            final Button username = (Button) findViewById(R.id.username);
            EditText searchBook2 = (EditText) findViewById(R.id.userName);
            searchBook2.setVisibility(View.GONE);
            usernameid.setText(myname);
            username.setText("Edit");

            // set the favauthor area with the fav author stored in the database
            String myauthor = cursord.getString(authorindex);
            final TextView favauthorid = (TextView) findViewById(R.id.favAuthorid);
            final Button favauthor = (Button) findViewById(R.id.favauthor);
            EditText searchBook = (EditText) findViewById(R.id.favAuthor);
            searchBook.setVisibility(View.GONE);
            favauthorid.setText(myauthor);
            favauthor.setText("Edit");

            // set the favquote area with the fav quote stored in the database
            String myquote = cursord.getString(quoteindex);
            final TextView favquoteid = (TextView) findViewById(R.id.favquoteid);
            final Button favquote = (Button) findViewById(R.id.favquote);
            EditText searchBook3 = (EditText) findViewById(R.id.favQuote);
            searchBook3.setVisibility(View.GONE);
            favquoteid.setText(myquote);
            favquote.setText("Edit");
            cursord.close();
        }

    }

}

