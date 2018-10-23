package com.example.android.iread;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import java.io.File;
//check karna ke sab cursors close aa

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link recentlyAddedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link recentlyAddedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class recentlyAddedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // a GridView variable that is used to show books in a grid
    private GridView gridView;

    // a list that contains objects of list_class for the recentlyAddedFragment
    static public ArrayList<list_class> recentlyAddedList = new ArrayList<>();

    // a ListView variable that is used to show books in a list
    private ListView listView;

    // a variable for listadapter object
    private listadapter adapter;

    // a variable for ImageButton which when clicked changes the layout of the book to a grid view
    ImageButton convertIntoGrid;

    // a variable for ImageButton which when clicked changes the layout of the book to a list view
    ImageButton convertIntoList;

    //a variable for LineayLayout referencing to a GridView
    LinearLayout grididd1;

    //a variable for LineayLayout referencing to a ListView
    LinearLayout listidd1;

    // a variable that is 1 when books are being shown in a ListView and 0 when in GridView
    int alreadyListView = 1;

    // a variable that is 0 when books are being shown in a ListView and 1 when in GridView
    int alreadyGridView =0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // a variable for bookdbHelper class object
    public static bookdbHelper mdbhelper;

    // a cursor object
    Cursor cursor3;
    // a cursor object
    Cursor cursord;

    // this contains the pdfname of the book that has been clicked to open
    String filetoopen;

    View   view;

    // it is 0 when a book in recentlyAddedFragment is clicked for the first time and > 0 when any
    //   other book has been clicked in the recentlyAddedFragment while the activty lifecycle has not finished
    int callhelper = 0;
    int count5 = 0;

    // a list that contains pdfnames of the books present in the recentlyaddedFragment
    public static ArrayList<String> recentlyaddeddb = new ArrayList<>();

    // a list that contains pdfnames of the books present in the recentlyaddedFragment
    public static ArrayList<String> recentlyaddeddb1 = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public recentlyAddedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment recentlyAddedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static recentlyAddedFragment newInstance(String param1, String param2) {
        recentlyAddedFragment fragment = new recentlyAddedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public  void onStart(){
        super.onStart();
        displayDatabaseInfo();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view   =  inflater.inflate(R.layout.fragment_recently_added, container, false);
        mdbhelper = new bookdbHelper(getContext());
        listView = (ListView) view.findViewById(R.id.list1);
        gridView = (GridView) view.findViewById(R.id.grid2);
        convertIntoGrid = (ImageButton) view.findViewById(R.id.turnGridView1);
        convertIntoList = (ImageButton) view.findViewById(R.id.turnListView1);
        listidd1 = (LinearLayout) view.findViewById(R.id.listidd1);
        grididd1 = (LinearLayout) view.findViewById(R.id.grididd1);
        convertIntoGrid.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        convertintoGrid();
                    }
                });
        convertIntoList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
               convertintoList();
            }

        });
        return  view;
    }

    // a helper function to convert the layout of books in grid view
    public void convertintoGrid(){
        if(alreadyGridView == 0) {
            gridView.setNumColumns(2);
            listidd1.setVisibility(View.GONE);
            grididd1.setVisibility(View.VISIBLE);
            displayDatabaseInfoGrid();
            alreadyGridView = 1;
            alreadyListView = 0;
        }
    }

    // a helper function to convert the layout of books in listView
    public void convertintoList(){
        if(alreadyListView == 0){
            displayDatabaseInfo();
            grididd1.setVisibility(View.GONE);
            listidd1.setVisibility(View.VISIBLE);
            alreadyListView = 1;
            alreadyGridView = 0;
        }
    }


    // display the database info onto the currentReadsFragment in gridView
    public void displayDatabaseInfoGrid(){
        SQLiteDatabase db = mdbhelper.getReadableDatabase();
        recentlyAddedList.clear();
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


        // a cursor that returns recent 5 books added to the database
        cursor3 = db.query(
                bookContract.bookEntry.TABLE_NAME,
                projection,// The columns to return for each row
                null,                   // Selection criteria
                null,                   // Selection criteria
                null,
                null,
                bookContract.bookEntry._ID + " DESC", " 5");

        //retrieve the information about recent 5 books added to the database and show them using list adapter
        if (cursor3.getCount() > recentlyAddedList.size()) {
            try {
                int titleColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_TITLE);
                int pdfnameColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PDF_NAME);
                int authorColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_AUTHOR_NAME);
                int descriptionColumnindex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_DESCRIPTION);
                int pagesColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_FAV);
                int pdflinkColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PDF_LINK);
                int publisheddateColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PUBLISHED_DATE);
                int imageLinkColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_IMAGE_LINK);

                while ((cursor3.moveToNext())) {
                    String title = cursor3.getString(titleColumnIndex);
                    String imagelink = cursor3.getString(imageLinkColumnIndex);
                    String pdfname = cursor3.getString(pdfnameColumnIndex);
                    String author = cursor3.getString(authorColumnIndex);
                    String publisheddate = cursor3.getString(publisheddateColumnIndex);
                    String pdflink = cursor3.getString(pdflinkColumnIndex);
                    String pages = cursor3.getString(pagesColumnIndex);
                    String description = cursor3.getString(descriptionColumnindex);
                    recentlyAddedList.add(new list_class(title, author, imagelink, pages, publisheddate, description, pdflink, pdfname));
                }
            } finally {
                cursor3.close();
            }
        }

        // Get a reference to the ListView, and attach the adapter to the gridView.
        callhelper = 0;
        listadapter.pdfnamelist.clear();
        recentlyaddeddb.clear();
        recentlyaddeddb1.clear();
        listadapter.alreadypresent.clear();
        adapter = new listadapter(getActivity(), recentlyAddedList);
        gridView.setAdapter(adapter);
        View emptyView = view.findViewById(R.id.emptyview1);
        gridView.setEmptyView(emptyView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //get the pdfname of the book clicked in recentlyAddedFragment and open it using intent
                count5 = 0;
                if(callhelper == 0) {
                    recentlyaddeddb1 = adapter.getpdfnamelist();
                    Collections.reverse(recentlyaddeddb1);
                    callhelper++;
                }
                filetoopen =  recentlyaddeddb1.get(position);
                File file = new File(Environment.getExternalStorageDirectory() +"/Download/" + filetoopen);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(),"File not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int position, long arg3) {
                //get the pdfname of the book clicked in recentlyAddedFragment show the showDeleteConfirmationDialog2 box
                if(callhelper == 0) {
                    recentlyaddeddb1 = adapter.getpdfnamelist();
                    Collections.reverse(recentlyaddeddb1);
                    callhelper++;
                }
                filetoopen =  recentlyaddeddb1.get(position);
                showDeleteConfirmationDialog2(recentlyaddeddb1.get(position));
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
                bookContract.bookEntry.COLUMN_PUBLISHED_DATE,
                bookContract.bookEntry.COLUMN_CURRENT_READS,
                bookContract.bookEntry.COLUMN_FAV,
                bookContract.bookEntry.COLUMN_PDF_NAME};

        // a cursor that contains last 5 books of the database
        cursord = db.query(
                bookContract.bookEntry.TABLE_NAME,
                projection,// The columns to return for each row
                null,                   // Selection criteria
                null,                   // Selection criteria
                null,
                null,
                bookContract.bookEntry._ID + " DESC", " 5");


        // Get a reference to the ListView, and attach the adapter to the listView.
        callhelper = 0;
        cursoradapter.pdfnamelist.clear();
        recentlyaddeddb.clear();
        recentlyaddeddb1.clear();
        cursoradapter.alreadypresent.clear();
        ListView booklistView = (ListView) view.findViewById(R.id.list1);
        final cursoradapter adapter = new cursoradapter(getContext(), cursord);
        booklistView.setAdapter(adapter);
        View emptyView = view.findViewById(R.id.emptyview1);
        booklistView.setEmptyView(emptyView);

        //  cursord.close();
        booklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                    //get the pdfname of the book clicked in
                                                    // recentlyAddedFragment and open it using intent
                                                    count5 = 0;
                                                if(callhelper == 0) {
                                                    recentlyaddeddb1 = adapter.getpdfnamelist();
                                                    Collections.reverse(recentlyaddeddb1);
                                                    callhelper++;
                                                }
                                                    filetoopen =  recentlyaddeddb1.get(position);
                                                    File file = new File(Environment.getExternalStorageDirectory() +"/Download/" + filetoopen);
                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    intent.setDataAndType(Uri.fromFile(file),"application/pdf");
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    try {
                                                        startActivity(intent);

                                                    } catch (ActivityNotFoundException e) {
                                                        Toast.makeText(getContext(), "File not found", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

        );

        booklistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                //get the pdfname of the book clicked in recentlyAddedFragment and
                // show the showDeleteConfirmationDialog2 box
                count5 = 0;
                if(callhelper == 0) {
                    recentlyaddeddb1 = adapter.getpdfnamelist();
                    Collections.reverse(recentlyaddeddb1);
                    callhelper++;
                }
                filetoopen =  recentlyaddeddb1.get(position);

                Toast.makeText(getContext(), "db1 = " + Integer.toString(recentlyaddeddb1.size()), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "count5 = " + Integer.toString(count5), Toast.LENGTH_SHORT).show();
                showDeleteConfirmationDialog2(filetoopen);
                return true;
            }
        });
    }

    // alert dialog to add book to favourites
    private void showDeleteConfirmationDialog2(final String pdfnam) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Add to Favourites?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SQLiteDatabase db = mdbhelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(bookContract.bookEntry.COLUMN_FAV, "1");
                String[] selectionArgs1 = {pdfnam};
                db.update(bookContract.bookEntry.TABLE_NAME, values, bookContract.bookEntry.COLUMN_PDF_NAME + "= ?", selectionArgs1);
                Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


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
