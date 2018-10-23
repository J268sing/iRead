package com.example.android.iread;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.iread.data.bookContract;
import com.example.android.iread.data.bookdbHelper;

import java.io.File;
import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {
    // a GridView variable that is used to show books in a grid
    private GridView gridView;

    // a list that contains objects of list_class for the ebooksFragment
    static public ArrayList<list_class> eBookFavouriteList = new ArrayList<>();

    // a ListView variable that is used to show books in a list
    private ListView listView;

    // a variable for listadapter object
    private listadapter adapter;

    // a variable for ImageButton which when clicked changes the layout of the book to a grid view
    ImageButton convertIntoGrid;

    // a variable for ImageButton which when clicked changes the layout of the book to a list view
    ImageButton convertIntoList;

    //a variable for LineayLayout referencing to a GridView
    LinearLayout grididd2;

    // a variable for LinearLayout referencing to ListView
    LinearLayout listidd2;

    // a variable for bookdbHelper class object
    public static bookdbHelper mdbhelper;

    // a variable that is 1 when books are being shown in a ListView and 0 when in GridView
    int alreadyListView = 1;

    // a variable that is 0 when books are being shown in a ListView and 1 when in GridView
    int alreadyGridView = 0;

   // a cursor object
    Cursor cursor3;
    // a cursor object
    Cursor cursord;


     // it is 0 when a book in FavouriteActivity is clicked for the first time and > 0 when any
    //   other book has been clicked in the FavouriteActivity while the activty lifecycle has not finished
    int callhelper = 0;

    // this contains the pdfname of the book that has been clicked to open
    String filetoopen;


    // a list that contains pdfnames of the books present in the recentlyaddedFragment
    public static ArrayList<String> recentlyaddeddb = new ArrayList<>();

    // a list that contains pdfnames of the books present in the FavouriteActivity
    public static ArrayList<String> favlist = new ArrayList<>();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_main:
                    Intent intent1 = new Intent(FavouriteActivity.this, MainActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_settings:
                    Intent intent2 = new Intent(FavouriteActivity.this, SettingsActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_favourite:
                    return true;
                case R.id.navigation_library:
                    Intent intent4 = new Intent(FavouriteActivity.this, LibraryActivity.class);
                    startActivity(intent4);
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onStart() {
        super.onStart();
       // displaydatabaseAsync sync = new displaydatabaseAsync();
       // sync.execute();
        displayDatabaseInfo();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_favourite);
        mdbhelper = new bookdbHelper(getApplicationContext());
        // Inflate the layout for this fragment
        listView = (ListView) findViewById(R.id.list3);
        gridView = (GridView) findViewById(R.id.grid4);
        listidd2 = (LinearLayout) findViewById(R.id.listidd3);
        grididd2 = (LinearLayout) findViewById(R.id.grididd3);
        convertIntoGrid = (ImageButton) findViewById(R.id.turnGridView3);
        convertIntoList = (ImageButton) findViewById(R.id.turnListView3);
        convertIntoGrid.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       convertintoGrid();
                    }
                });
        convertIntoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              convertintoList();
            }
        });
    }

    // a helper function to convert the layout of books in list view
    public void convertintoList(){
        if (alreadyListView == 0) {
          //  displaydatabaseAsync sync = new displaydatabaseAsync();
            //sync.execute();
            displayDatabaseInfo();
            grididd2.setVisibility(View.GONE);
            listidd2.setVisibility(View.VISIBLE);
            alreadyListView = 1;
            alreadyGridView = 0;
        }
    }

    // a helper function to convert the layout of books in grid view
    public void convertintoGrid(){
        if (alreadyGridView == 0) {
            gridView.setNumColumns(2);
            listidd2.setVisibility(View.GONE);
            grididd2.setVisibility(View.VISIBLE);
            displayDatabaseInfoGrid();
            alreadyGridView = 1;
            alreadyListView = 0;
        }
    }

    // display the database info onto the FavouriteActivity in gridview
    public void displayDatabaseInfoGrid(){
        eBookFavouriteList.clear();
        SQLiteDatabase db = mdbhelper.getReadableDatabase();
        String[] projection = {
                bookContract.bookEntry._ID,
                bookContract.bookEntry.COLUMN_AUTHOR_NAME,
                bookContract.bookEntry.COLUMN_DESCRIPTION,
                bookContract.bookEntry.COLUMN_IMAGE_LINK,
                bookContract.bookEntry.COLUMN_PAGES,
                bookContract.bookEntry.COLUMN_TITLE,
                bookContract.bookEntry.COLUMN_PDF_NAME,
                bookContract.bookEntry.COLUMN_PDF_LINK,
                bookContract.bookEntry.COLUMN_FAV,
                bookContract.bookEntry.COLUMN_PUBLISHED_DATE};
        String selection = bookContract.bookEntry.COLUMN_FAV + "=?";
        String[] selectionArgs = {"1"};

        // cursor3 has all the rows from database that have value of COLUMN_FAV = 1
        cursor3 = db.query(bookContract.bookEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, null);

        //obtain the information about the rows from cursor3 and add it to arraylist eBookFavouriteList()
        // Then inflate the layout using list adapter
        if (cursor3.getCount() > eBookFavouriteList.size()) {
            try {
                int titleColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_TITLE);
                int pdfnameColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PDF_NAME);
                int authorColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_AUTHOR_NAME);
                int descriptionColumnindex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_DESCRIPTION);
                int pagesColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_FAV);
                int pdflinkColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PDF_LINK);
                int publisheddateColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PUBLISHED_DATE);
                int imageLinkColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_IMAGE_LINK);

                while (cursor3.moveToNext()) {
                    String title = cursor3.getString(titleColumnIndex);
                    String imagelink = cursor3.getString(imageLinkColumnIndex);
                    String pdfname = cursor3.getString(pdfnameColumnIndex);
                    String author = cursor3.getString(authorColumnIndex);
                    String publisheddate = cursor3.getString(publisheddateColumnIndex);
                    String pdflink = cursor3.getString(pdflinkColumnIndex);
                    String pages = cursor3.getString(pagesColumnIndex);
                    String description = cursor3.getString(descriptionColumnindex);
                    eBookFavouriteList.add(new list_class(title, author, imagelink, pages, publisheddate, description, pdflink, pdfname));
                }
            } finally {
                cursor3.close();
            }
        }
        // Get a reference to the ListView, and attach the adapter to the gridView.
        callhelper = 0;
        listadapter.pdfnamelist.clear();
        recentlyaddeddb.clear();
        favlist.clear();
        listadapter.favlist.clear();
        listadapter.alreadypresent.clear();
        adapter = new listadapter(FavouriteActivity.this, eBookFavouriteList);
        gridView.setAdapter(adapter);
        View emptyView = findViewById(R.id.emptyview3);
        gridView.setEmptyView(emptyView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //get the pdfname of the book clicked in currentReadsFragment and open it using intent
                filetoopen =  eBookFavouriteList.get(position).getMpdfName();
                File file = new File(Environment.getExternalStorageDirectory() + "/Download/" + filetoopen);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int position, long arg3) {
                //get the pdfname of the book clicked in FavouriteActivity show the showDeleteConfirmationDialog2 box
                showDeleteConfirmationDialog2(eBookFavouriteList.get(position).getMpdfName());
                return true;
            }
        });

    }

    // display the database info onto the currentReadsFragment in listview
    private void displayDatabaseInfo() {
        SQLiteDatabase db = mdbhelper.getReadableDatabase();

        String[] projection = {
                bookContract.bookEntry._ID,
                bookContract.bookEntry.COLUMN_AUTHOR_NAME,
                bookContract.bookEntry.COLUMN_DESCRIPTION,
                bookContract.bookEntry.COLUMN_IMAGE_LINK,
                bookContract.bookEntry.COLUMN_PAGES,
                bookContract.bookEntry.COLUMN_TITLE,
                bookContract.bookEntry.COLUMN_PDF_LINK,
                bookContract.bookEntry.COLUMN_CURRENT_READS,
                bookContract.bookEntry.COLUMN_PDF_NAME,
                bookContract.bookEntry.COLUMN_FAV,
                bookContract.bookEntry.COLUMN_PUBLISHED_DATE};


        String  selection = bookContract.bookEntry.COLUMN_FAV + "=?";
        String []  selectionArgs = {"1"};
        cursord = db.query(
                bookContract.bookEntry.TABLE_NAME,
                 projection,// The columns to return for each row
                selection,                   // Selection criteria
                selectionArgs,                   // Selection criteria
                null,
                null,
                null);



        // Get a reference to the ListView, and attach the adapter to the listView.
        callhelper = 0;
        cursoradapter.pdfnamelist.clear();
        recentlyaddeddb.clear();
        favlist.clear();
        cursoradapter.favlist.clear();
        cursoradapter.alreadypresent.clear();
        ListView booklistView = (ListView) findViewById(R.id.list3);

       final  cursoradapter adapter = new cursoradapter(getApplicationContext(), cursord);
        booklistView.setAdapter(adapter);
        View emptyView = findViewById(R.id.emptyview3);
        booklistView.setEmptyView(emptyView);
        booklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                    //get the pdfname of the book clicked in
                                                    // Favourite and  and open it using intent
                                                    favlist = adapter.getfavlist();

                                                    filetoopen =  favlist.get(position);
                                                    File file = new File(Environment.getExternalStorageDirectory() +"/Download/" + filetoopen);
                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    intent.setDataAndType(Uri.fromFile(file),"application/pdf");
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    try {
                                                        startActivity(intent);

                                                    } catch (ActivityNotFoundException e) {
                                                        Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
        );

        booklistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                //get the pdfname of the book clicked in currentReadsFragment and
                // show the showDeleteConfirmationDialog2 box
                favlist = adapter.getfavlist();
                showDeleteConfirmationDialog2(favlist.get(position));
                return true;
            }
        });

    }

    // showDeleteConfirmationDialog2 adds updates the value of bookContract.bookEntry.COLUMN_FAV
    //    to 1 if clicked yes and 0 otherwise
    private void showDeleteConfirmationDialog2(final String pdfnam) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Remove from Favourites?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SQLiteDatabase db = mdbhelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(bookContract.bookEntry.COLUMN_FAV, "0");
                String[] selectionArgs1 = {pdfnam};
                db.update(bookContract.bookEntry.TABLE_NAME, values, bookContract.bookEntry.COLUMN_PDF_NAME + "=?", selectionArgs1);
                Toast.makeText(getApplicationContext(), "Removed from Favourites", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FavouriteActivity.this, FavouriteActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}