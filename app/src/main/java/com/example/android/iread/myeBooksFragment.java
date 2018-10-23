package com.example.android.iread;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
 import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.iread.data.bookContract;
import com.example.android.iread.data.bookdbHelper;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
 import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link myeBooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link myeBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myeBooksFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    int count;

    // a list that contains objects of list_class for the ebooksFragment
    static public ArrayList<list_class> myeBooksList = new ArrayList<>();

    // a cursor object
    Cursor cursor3;
    // a cursor object
    Cursor cursord;

    // this variable is used to return the type of cursor we want. 0 will return cursor that has the order
    //   same as database , 1 will return the cursor that has information sorted by authorname
    //   and 2 will return the cursor that has information sorted by title
    public static int sortconstant = 0;

    // this contains the pdfname of the book that has been clicked to open
    String filetoopen;

    // a Uri variable
    private Uri mCurrent;

    // a variable that is 1 when books are being shown in a ListView and 0 when in GridView
    int alreadyListView = 1;

    // a variable that is 0 when books are being shown in a ListView and 1 when in GridView
    int alreadyGridView = 0;

    // a ListView variable that is used to show books in a list
    private ListView listView;

    // a GridView variable that is used to show books in a grid
    private GridView gridView;

    // a variable for listadapter object
    private listadapter adapter;

    // a variable for ImageButton which when clicked changes the layout of the book to a grid view
    ImageButton convertIntoGrid;

    // a variable for ImageButton which when clicked changes the layout of the book to a list view
    ImageButton convertIntoList;

    private  View view;

    // a variable for bookdbHelper class object
    public static bookdbHelper mdbhelper;


    //a variable for LineayLayout referencing to a GridView
    LinearLayout gridid;

    //a variable for LineayLayout referencing to a ListView
    LinearLayout listid;


    // a list that contains pdfnames of the books present in the ebooksFragment
    ArrayList<String> ls1;

    //alreadypresent list contains one at positon i if the book is already present in
    //  currentReadFragment and 0 otherwise
    ArrayList<Integer> alreadypresent;

    // a variable to tell the number of books present in currentReadsFragment
    int present = 0;

    // a list that contains objects of list_class for the currentReadsFragment
    public static ArrayList<String> currentreadlistdb = new ArrayList<>();

    // it is 0 when a book in currentReadsFragment is clicked for the first time and > 0 when any
    //   other book has been clicked in the currentReadsFragment while the activty lifecycle has not finished
    int callhelper = 0;

     // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;


    public myeBooksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myeBooksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static myeBooksFragment newInstance(String param1, String param2) {
        myeBooksFragment fragment = new myeBooksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        displayDatabaseInfo();
      //  displaydatabaseAsync sync = new displaydatabaseAsync();
        //sync.execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mye_books, container, false);
        mdbhelper = new bookdbHelper(getContext());
        listView = (ListView) view.findViewById(R.id.listxx);
        gridView = (GridView) view.findViewById(R.id.grid1);
        convertIntoGrid = (ImageButton) view.findViewById(R.id.turnGridView);
        listid = (LinearLayout) view.findViewById(R.id.listidd);
        gridid = (LinearLayout) view.findViewById(R.id.grididd);
        convertIntoList = (ImageButton) view.findViewById(R.id.turnListView);
        convertIntoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertintoList();
            }

        });
        convertIntoGrid.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         convertintoGrid();
                    }
                });
        return view;
    }



    // a helper function to convert the layout of books in grid view
    public void convertintoGrid() {
        if (alreadyGridView == 0) {
            gridView.setNumColumns(2);
            //     gridView.setVisibility();
            listid.setVisibility(View.GONE);
            gridid.setVisibility(View.VISIBLE);
            displayDatabaseInfoGrid();
            alreadyGridView = 1;
            alreadyListView = 0;
        }
    }


    // a helper function to convert the layout of books in list view
    public void convertintoList() {
        if (alreadyListView == 0) {
            gridid.setVisibility(View.GONE);
            listid.setVisibility(View.VISIBLE);
            displayDatabaseInfo();
            alreadyListView = 1;
            alreadyGridView = 0;
        }

    }


    // display the database info onto the ebooksFragment in gridView
    public void displayDatabaseInfoGrid()  {
        SQLiteDatabase db2 = mdbhelper.getReadableDatabase();

        String[] projection = {
                bookContract.bookEntry._ID,
                bookContract.bookEntry.COLUMN_AUTHOR_NAME,
                bookContract.bookEntry.COLUMN_DESCRIPTION,
                bookContract.bookEntry.COLUMN_IMAGE_LINK,
                bookContract.bookEntry.COLUMN_PAGES,
                bookContract.bookEntry.COLUMN_TITLE,
                bookContract.bookEntry.COLUMN_PDF_NAME,
                bookContract.bookEntry.COLUMN_PDF_LINK,
                bookContract.bookEntry.COLUMN_PUBLISHED_DATE,
                bookContract.bookEntry.COLUMN_FAV,
                bookContract.bookEntry.COLUMN_CURRENT_READS};

        // cursor3 has all the rows from database
        cursor3 = db2.query(bookContract.bookEntry.TABLE_NAME, projection, null, null,
                null, null, null);

        //obtain the information about the rows from cursor3 and add it to arraylist myebooksList()
        // Then inflate the layout using list adapter
        if (cursor3.getCount() > myeBooksList.size()) {
            try {
                int titleColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_TITLE);
                int pdfnameColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PDF_NAME);
                int authorColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_AUTHOR_NAME);
                int descriptionColumnindex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_DESCRIPTION);
                int pagesColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PAGES);
                int pdflinkColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PDF_LINK);
                int publisheddateColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PUBLISHED_DATE);
                int imageLinkColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_IMAGE_LINK);
                int favColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_FAV);
                int currentReadIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_CURRENT_READS);

                while (cursor3.moveToNext()) {
                    String title = cursor3.getString(titleColumnIndex);
                    String imagelink = cursor3.getString(imageLinkColumnIndex);
                    String pdfname = cursor3.getString(pdfnameColumnIndex);
                    String author = cursor3.getString(authorColumnIndex);
                    String publisheddate = cursor3.getString(publisheddateColumnIndex);
                    String pdflink = cursor3.getString(pdflinkColumnIndex);
                    String pages = cursor3.getString(favColumnIndex);
                    String description = cursor3.getString(currentReadIndex);
                    myeBooksList.add(new list_class(title, author, imagelink, pages, publisheddate, description, pdflink, pdfname));
                }
            } finally {
                cursor3.close();
            }

        }
        // Get a reference to the ListView, and attach the adapter to the gridView.

        callhelper = 0;
            listadapter.pdfnamelist.clear();
            listadapter.alreadypresent.clear();
            adapter = new listadapter(getActivity(), myeBooksList);
            gridView.setAdapter(adapter);
            View emptyView = view.findViewById(R.id.emptyview);
            gridView.setEmptyView(emptyView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                     @Override
                                                     public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                         //get the pdfname of the book clicked in ebooksFragment and open it using intent
                                                         if(callhelper == 0) {
                                                             ls1 = adapter.getpdfnamelist();
                                                             alreadypresent = adapter.getLs1();
                                                             callhelper++;
                                                         }

                                                         Intent intent = new Intent(Intent.ACTION_VIEW);
                                                         filetoopen = ls1.get(position);
                                                         if(hasRealRemovableSdCard(getContext()) < 2) {
                                                             Toast.makeText(getContext(), "hnji kinda " , Toast.LENGTH_SHORT).show();

                                                             File file = new File(Environment.getExternalStorageDirectory() +"/Download/" + filetoopen);
                                                             intent = new Intent(Intent.ACTION_VIEW);
                                                             intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                                             intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                             try {
                                                                 startActivity(intent);

                                                             } catch (ActivityNotFoundException e) {
                                                                 Toast.makeText(getContext(), "File not found", Toast.LENGTH_SHORT).show();
                                                             }
                                                         } else{
                                                             Toast.makeText(getContext(), Integer.toString(count) , Toast.LENGTH_SHORT).show();
                                                             Toast.makeText(getContext(), Environment.getExternalStorageState() , Toast.LENGTH_SHORT).show();

                                                         }

                                                         //check if the book that has been clicked is already present in currentReadsFragment
                                                         SQLiteDatabase db1 = mdbhelper.getReadableDatabase();
                                                         SQLiteDatabase db = mdbhelper.getWritableDatabase();
                                                         present = 0;
                                                      //   Toast.makeText(getContext(), "alreadypresent + " + alreadypresent.size(), Toast.LENGTH_SHORT).show();
                                                         int loopcount = 0;
                                                         while(loopcount < alreadypresent.size()){
                                                             if(alreadypresent.get(loopcount) > 0) {
                                                                 present++;
                                                             }
                                                             loopcount++;
                                                         }

                                                         //if book is already present in the currentReadsFragment then we do nothing else if the currentReadsFragment
                                                         //  contains less then add the clicked book into currentReadsFragment and if there are more than 5 books
                                                         // then remove any randomnly selected book from currentReadsFramgnet and add the clicked book
                                                         if(alreadypresent.get(position) == 0) {
                                                             if (present > 5) {
                                                                 Toast.makeText(getContext(), "edar nu", Toast.LENGTH_SHORT).show();
                                                                 removelast(ls1, alreadypresent);
                                                                 ContentValues values = new ContentValues();
                                                                 values.put(bookContract.bookEntry.COLUMN_CURRENT_READS, Integer.toString(1));
                                                                 String[] selectionArgs1 = {ls1.get(position)};
                                                                 db.update(bookContract.bookEntry.TABLE_NAME, values, bookContract.bookEntry.COLUMN_PDF_NAME + "= ?", selectionArgs1);

                                                             } else {
                                                                 ContentValues values = new ContentValues();
                                                                 values.put(bookContract.bookEntry.COLUMN_CURRENT_READS, Integer.toString(1));
                                                                 String[] selectionArgs1 = {ls1.get(position)};
                                                                 db.update(bookContract.bookEntry.TABLE_NAME, values, bookContract.bookEntry.COLUMN_PDF_NAME + "= ?", selectionArgs1);

                                                             }
                                                         }
                                                     }
                                                 }
            );
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int position, long arg3) {
                //get the pdfname of the book clicked in ebooksFragment
                // show the showDeleteConfirmationDialog1 box
                mCurrent = ContentUris.withAppendedId(bookContract.bookEntry.CONTENT_URI, arg3);
                if(callhelper == 0) {
                    ls1 = adapter.getpdfnamelist();
                    alreadypresent = adapter.getLs1();
                    callhelper++;
                }
                showDeleteConfirmationDialog1(ls1.get(position), mCurrent);
                return true;
            }
        });
    }


    public  int hasRealRemovableSdCard(Context context) {
         count =  ContextCompat.getExternalFilesDirs(context, null).length;
        return ContextCompat.getExternalFilesDirs(context, null).length;
    }

    private void loadfile(String filetoopen){

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
                bookContract.bookEntry.COLUMN_PUBLISHED_DATE,
                bookContract.bookEntry.COLUMN_CURRENT_READS,
                bookContract.bookEntry.COLUMN_FAV,
                bookContract.bookEntry.COLUMN_PDF_NAME};


        if(sortconstant == 0) {
            cursord = db.query(
                    bookContract.bookEntry.TABLE_NAME,
                    projection,// The columns to return for each row
                    null,                   // Selection criteria
                    null,                   // Selection criteria
                    null,
                    null,
                    null);

        } else if(sortconstant == 1){
            cursord = db.query(bookContract.bookEntry.TABLE_NAME,
                    projection,// The columns to return for each row
                    null,                   // Selection criteria
                    null,                   // Selection criteria
                    null,
                    null,
                    bookContract.bookEntry.COLUMN_AUTHOR_NAME + " ASC");

        } else{
            cursord = db.query(bookContract.bookEntry.TABLE_NAME,
                    projection,// The columns to return for each row
                    null,                   // Selection criteria
                    null,                   // Selection criteria
                    null,
                    null,
                    bookContract.bookEntry.COLUMN_TITLE + " ASC");
        }

        // display the database info onto the currentReadsFragment in listview
        ListView booklistView = (ListView) view.findViewById(R.id.listxx);
        callhelper = 0;
        cursoradapter.pdfnamelist.clear();
        cursoradapter.alreadypresent.clear();
        final cursoradapter adapter = new cursoradapter(getContext(), cursord);
        booklistView.setAdapter(adapter);
        View emptyView = view.findViewById(R.id.emptyview);
        booklistView.setEmptyView(emptyView);

        booklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                    //get the pdfname of the book clicked in
                                                    // ebooksFragment and open it using intent

                                                    if(callhelper == 0) {
                                                        ls1 = adapter.getpdfnamelist();
                                                        alreadypresent = adapter.getLs1();
                                                        callhelper++;
                                                    }

                                                    filetoopen = ls1.get(position);
                                                    Toast.makeText(getContext(),"/SD card" + "/Download" + filetoopen, Toast.LENGTH_SHORT).show();


                                                    File file = new File(Environment.getExternalStorageDirectory() +"/Download/" + filetoopen);
                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    intent.setDataAndType(Uri.fromFile(file),"application/pdf");
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    try {
                                                        startActivity(intent);

                                                    } catch (ActivityNotFoundException e) {
                                                        Toast.makeText(getContext(), "File not found", Toast.LENGTH_SHORT).show();
                                                    }


                                                    //check if the book that has been clicked is already present in currentReadsFragment
                                                    SQLiteDatabase db1 = mdbhelper.getReadableDatabase();
                                                    SQLiteDatabase db = mdbhelper.getWritableDatabase();
                                                    int loopcount = 0;
                                                    while(loopcount < alreadypresent.size()){
                                                        if(alreadypresent.get(loopcount) > 0) {
                                                            present++;
                                                        }
                                                        loopcount++;
                                                    }

                                                    //if book is already present in the currentReadsFragment then we do nothing else if the currentReadsFragment
                                                    //  contains less then add the clicked book into currentReadsFragment and if there are more than 5 books
                                                    // then remove any randomnly selected book from currentReadsFramgnet and add the clicked book
                                                    if(alreadypresent.get(position) == 0) {
                                                        if (present > 5) {
                                                            Toast.makeText(getContext(), "edar nu", Toast.LENGTH_SHORT).show();
                                                            removelast(ls1, alreadypresent);
                                                            ContentValues values = new ContentValues();
                                                            values.put(bookContract.bookEntry.COLUMN_CURRENT_READS, Integer.toString(1));
                                                            String[] selectionArgs1 = {ls1.get(position)};
                                                            db.update(bookContract.bookEntry.TABLE_NAME, values, bookContract.bookEntry.COLUMN_PDF_NAME + "= ?", selectionArgs1);

                                                        } else {
                                                            ContentValues values = new ContentValues();
                                                            values.put(bookContract.bookEntry.COLUMN_CURRENT_READS, Integer.toString(1));
                                                            String[] selectionArgs1 = {ls1.get(position)};
                                                            db.update(bookContract.bookEntry.TABLE_NAME, values, bookContract.bookEntry.COLUMN_PDF_NAME + "= ?", selectionArgs1);
                                                        }
                                                    }
                                                }
                                            }
        );

        booklistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                //get the pdfname of the book clicked in ebooksFragment
                // show the showDeleteConfirmationDialog1 box
                mCurrent = ContentUris.withAppendedId(bookContract.bookEntry.CONTENT_URI, id);
                if(callhelper == 0) {
                    ls1 = adapter.getpdfnamelist();
                    alreadypresent = adapter.getLs1();
                    callhelper++;
                }
                showDeleteConfirmationDialog1(ls1.get(position), mCurrent);
                return true;
            }
        });

    }


    //if there are more 6 books in the currentReadsFragment then remove the last opened book and add a new one
    public void removelast(ArrayList<String> pdfnamlist, ArrayList<Integer> alpresent){

        currentreadlistdb.clear();
        for(int i = 0; i < pdfnamlist.size(); i++){
            if(alpresent.get(i) == 1){
                currentreadlistdb.add(pdfnamlist.get(i));
            }
        }

        // get pdfname of the any random book that is present in currentReadsFragment
        Random rand = new Random();
        int arbitrary = rand.nextInt(3);
        String toremove = currentreadlistdb.get(arbitrary);

        // update the COLUMN_CURRENT_READ of the book that has pdfname equal to toremove to 0

        SQLiteDatabase db = mdbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(bookContract.bookEntry.COLUMN_CURRENT_READS, "0");
        String selection1 = bookContract.bookEntry.COLUMN_PDF_NAME + " = ?";
        String[] selectionArgs3 = {toremove};
        db.update(bookContract.bookEntry.TABLE_NAME, values, selection1, selectionArgs3);
    }


    // showDeleteConfirmationDialog1 adds updates the value of bookContract.bookEntry.COLUMN_FAV
    //    to 1 if clicked Favourite and  deletes the book if Delete is selected
    private void showDeleteConfirmationDialog1(final String pdfnam, final Uri uri) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to Delete the book or Add to Favourites");
        builder.setPositiveButton("Favourite", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SQLiteDatabase db = mdbhelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(bookContract.bookEntry.COLUMN_FAV, "1");
                        String[] selectionArgs1 = {pdfnam};
                        db.update(bookContract.bookEntry.TABLE_NAME, values, bookContract.bookEntry.COLUMN_PDF_NAME + "= ?", selectionArgs1);
                        Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
                        bookContract.bookEntry.COLUMN_PUBLISHED_DATE};

                Cursor x = db.query(bookContract.bookEntry.TABLE_NAME, projection, null, null, null, null, null);
                x.close();
                int rowsdelete = delete(mCurrent, null, null);
                Intent intent = new Intent(getActivity(), LibraryActivity.class);
                startActivity(intent);
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    //**********************************************************************************************
    //delete functions
    //**********************************************************************************************
    private static final int books = 100;
    private static final int books_id = 101;
    private static final UriMatcher sUrimatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        sUrimatcher.addURI(bookContract.CONTENT_AUTHORITY, bookContract.PATH_BOOKS, books);
        sUrimatcher.addURI(bookContract.CONTENT_AUTHORITY, bookContract.PATH_BOOKS + "/#", books_id);
    }

    // a function that deletes the book that has book selected in the showDeleteConfirmationDialog1
    //   to be deleted
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mdbhelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUrimatcher.match(uri);
        switch (match) {
            case books:
                rowsDeleted = database.delete(bookContract.bookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case books_id:
                selection = bookContract.bookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(bookContract.bookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        return rowsDeleted;
    }

    // delete all rows from bookContract.bookEntry.TABLE_NAME tablev
    public static void deleteall() {
        SQLiteDatabase db1 = mdbhelper.getWritableDatabase();
        db1.execSQL("DELETE FROM " + bookContract.bookEntry.TABLE_NAME);
        db1.close();
    }
    //**********************************************************************************************



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            ;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
