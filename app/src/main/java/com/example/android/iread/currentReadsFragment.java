package com.example.android.iread;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
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
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link currentReadsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link currentReadsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class currentReadsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // a list that contains objects of list_class for the currentReadsFragment
    static public ArrayList<list_class> currentReadsList = new ArrayList<>();

    // a GridView variable that is used to show books in a grid
    private GridView gridView;

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

    // this contains the pdfname of the book that has been clicked to open
    String filetoopen;

    // a variable that is 1 when books are being shown in a ListView and 0 when in GridView
    int alreadyListView = 1;

    // a variable that is 0 when books are being shown in a ListView and 1 when in GridView
    int alreadyGridView = 0;

    // a list that contains pdfnames of the books present in the currentReadsFragment
    public static ArrayList<String> currentreadlistdb = new ArrayList<>();

    private View   view;

    // a cursor object
    Cursor cursor3;
    // a cursor object
    Cursor cursord;

    // a variable for bookdbHelper class object
    public static bookdbHelper mdbhelper;

    // it is 0 when a book in currentReadsFragment is clicked for the first time and > 0 when any
    //   other book has been clicked in the currentReadsFragment while the activty lifecycle has not finished
    int callhelper = 0;

    // a list that contains pdfnames of the books present in the ebooksFragment
    ArrayList<String> ls2;

    //alreadypresent list contains one at positon i if the book is already present in
    //  currentReadFragment and 0 otherwise
    ArrayList<Integer> alreadypresent;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public currentReadsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment currentReadsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static currentReadsFragment newInstance(String param1, String param2) {
        currentReadsFragment fragment = new currentReadsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view   =  inflater.inflate(R.layout.fragment_current_reads, container, false);
        mdbhelper = new bookdbHelper(getContext());
        listView = (ListView) view.findViewById(R.id.list2);
        gridView = (GridView) view.findViewById(R.id.grid3);
        listidd2 = (LinearLayout) view.findViewById(R.id.listidd2);
        grididd2 = (LinearLayout) view.findViewById(R.id.grididd2);
        convertIntoGrid = (ImageButton) view.findViewById(R.id.turnGridView2);
        convertIntoList = (ImageButton) view.findViewById(R.id.turnListView2);
        convertIntoList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
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
    public void convertintoGrid(){
        if(alreadyGridView == 0) {
            gridView.setNumColumns(2);
            listidd2.setVisibility(View.GONE);
            grididd2.setVisibility(View.VISIBLE);
            displayDatabaseInfoGrid();
            alreadyGridView = 1;
            alreadyListView = 0;
        }
    }


    // a helper function to convert the layout of books in list view
    public void convertintoList (){
        if(alreadyListView == 0){
            //displaydatabaseAsync sync = new displaydatabaseAsync();
            // sync.execute();
            displayDatabaseInfo();
            grididd2.setVisibility(View.GONE);
            listidd2.setVisibility(View.VISIBLE);
            alreadyListView = 1;
            alreadyGridView = 0;
        }
    }



    // display the database info onto the currentReadsFragment in gridview
    public void displayDatabaseInfoGrid(){
        currentReadsList.clear();
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
                bookContract.bookEntry.COLUMN_CURRENT_READS,
                bookContract.bookEntry.COLUMN_PUBLISHED_DATE};

        String  selection = bookContract.bookEntry.COLUMN_CURRENT_READS + ">?";
        String []  selectionArgs = {"0"};
        // cursor3 has all the rows from database that have value of COLUMN_CURRENT_READS > 0
        cursor3 = db.query(bookContract.bookEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, null);


        //obtain the information about the rows from cursor3 and add it to arraylist currentReadsList()
        // Then inflate the layout using list adapter
        if (cursor3.getCount() > currentReadsList.size()) {
            try {
                int titleColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_TITLE);
                int pdfnameColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PDF_NAME);
                int authorColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_AUTHOR_NAME);
                int descriptionColumnindex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_DESCRIPTION);
                int pagesColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PAGES);
                int pdflinkColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PDF_LINK);
                int publisheddateColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_PUBLISHED_DATE);
                int imageLinkColumnIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_IMAGE_LINK);
                int currentreadIndex = cursor3.getColumnIndex(bookContract.bookEntry.COLUMN_CURRENT_READS);

                while (cursor3.moveToNext()) {
                    String title = cursor3.getString(titleColumnIndex);
                    String imagelink = cursor3.getString(imageLinkColumnIndex);
                    String pdfname = cursor3.getString(pdfnameColumnIndex);
                    String author = cursor3.getString(authorColumnIndex);
                    String publisheddate = cursor3.getString(publisheddateColumnIndex);
                    String pdflink = cursor3.getString(pdflinkColumnIndex);
                    String pages = cursor3.getString(pagesColumnIndex);
                    String description = cursor3.getString(descriptionColumnindex);
                    currentReadsList.add(new list_class(title, author, imagelink, pages, publisheddate, description, pdflink, pdfname));
                }
            } finally {
                cursor3.close();
            }
        }

        // Get a reference to the ListView, and attach the adapter to the gridView.
        callhelper = 0;
        listadapter.pdfnamelist.clear();
        View emptyView = view.findViewById(R.id.emptyview2);
        gridView.setEmptyView(emptyView);
        adapter = new listadapter(getActivity(), currentReadsList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //get the pdfname of the book clicked in currentReadsFragment and open it using intent
                if(callhelper == 0) {
                    ls2 = adapter.getpdfnamelist();
                    alreadypresent=  adapter.getLs1();
                    callhelper++;
                }
                currentreadlistdb.clear();

                for(int i = 0; i < ls2.size(); i++){
                    if(alreadypresent.get(i) == 1){
                        currentreadlistdb.add(ls2.get(i));
                    }
                }
                filetoopen = currentreadlistdb.get(position);
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
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int position, long arg3) {
                //get the pdfname of the book clicked in currentReadsFragment show the showDeleteConfirmationDialog2 box
                if(callhelper == 0) {
                    ls2 = adapter.getpdfnamelist();
                    alreadypresent=  adapter.getLs1();
                    callhelper++;
                }
                currentreadlistdb.clear();

                for(int i = 0; i < ls2.size(); i++){
                    if(alreadypresent.get(i) == 1){
                        currentreadlistdb.add(ls2.get(i));
                    }
                }
                showDeleteConfirmationDialog2(currentreadlistdb.get(position));
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
                bookContract.bookEntry.COLUMN_FAV,
                bookContract.bookEntry.COLUMN_CURRENT_READS,
                bookContract.bookEntry.COLUMN_PDF_NAME};
        String  selection = bookContract.bookEntry.COLUMN_CURRENT_READS + " > ?";
        String []  selectionArgs = {"0"};
        cursord = db.query(
                bookContract.bookEntry.TABLE_NAME,
                 projection,// The columns to return for each row
                selection,                   // Selection criteria
                selectionArgs,                   // Selection criteria
                null,
                null,
                bookContract.bookEntry.COLUMN_CURRENT_READS  +" ASC");


        // display the database info onto the currentReadsFragment in listview
        ListView booklistView = (ListView) view.findViewById(R.id.list2);
        callhelper = 0;
        cursoradapter.pdfnamelist.clear();
        final cursoradapter adapter = new cursoradapter(getContext(), cursord);
        booklistView.setAdapter(adapter);
        View emptyView = view.findViewById(R.id.emptyview2);
        booklistView.setEmptyView(emptyView);
        booklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                    //get the pdfname of the book clicked in
                                                    // currentReadsFragment and open it using intent
                                                    int j =   adapter.getCount();
                                                    if(callhelper == 0) {
                                                        ls2 = adapter.getpdfnamelist();
                                                        alreadypresent=  adapter.getLs1();
                                                        callhelper++;
                                                    }
                                                    currentreadlistdb.clear();

                                                    for(int i = 0; i < ls2.size(); i++){
                                                        if(alreadypresent.get(i) == 1){
                                                            currentreadlistdb.add(ls2.get(i));
                                                        }
                                                    }
                                                    filetoopen = currentreadlistdb.get(position);
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
                //get the pdfname of the book clicked in currentReadsFragment and
                // show the showDeleteConfirmationDialog2 box
                if(callhelper == 0) {
                    ls2 = adapter.getpdfnamelist();
                    alreadypresent=  adapter.getLs1();
                    callhelper++;
                }
                currentreadlistdb.clear();

                for(int i = 0; i < ls2.size(); i++){
                    if(alreadypresent.get(i) == 1){
                        currentreadlistdb.add(ls2.get(i));
                    }
                }
                showDeleteConfirmationDialog2(currentreadlistdb.get(position));
                return true;
            }
        });

    }


    // showDeleteConfirmationDialog2 adds updates the value of bookContract.bookEntry.COLUMN_FAV
    //    to 1 if clicked yes and 0 otherwise
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