package com.example.android.iread;

import android.content.Context;
import android.database.Cursor;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.iread.data.bookContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class cursoradapter extends CursorAdapter {

    // a variable for pdfname that is added in pdfnamelist list
    static String pdfnametoOpen = "";

    // a list that contains pdfnames of the books present in the ebooksFragment
    public static ArrayList<String> pdfnamelist = new ArrayList<>();

    // a list that contains pdfnames of the books present in the FavouriteActivity
    public static ArrayList<String> favlist = new ArrayList<>();

    //alreadypresent list contains one at positon i if the book is already present in
    //  currentReadFragment and 0 otherwise
    public static ArrayList<Integer> alreadypresent = new ArrayList<>();


     public cursoradapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    // this helps to bind view to the cursor
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView bookName = (TextView) view.findViewById(R.id.name);
        TextView authorName = (TextView) view.findViewById(R.id.author);
        TextView arrow = (TextView) view.findViewById(R.id.arrow);

        //get the columnindnex of each required field
        int bookNameIndex = cursor.getColumnIndex(bookContract.bookEntry.COLUMN_TITLE);
        int authorNameIndex = cursor.getColumnIndex(bookContract.bookEntry.COLUMN_AUTHOR_NAME);
        int imageLinkIndex = cursor.getColumnIndex(bookContract.bookEntry.COLUMN_IMAGE_LINK);
        int pdfnameIndex = cursor.getColumnIndex(bookContract.bookEntry.COLUMN_PDF_NAME);
        int currentReadIndex = cursor.getColumnIndex(bookContract.bookEntry.COLUMN_CURRENT_READS);
        int currentfavindex = cursor.getColumnIndex(bookContract.bookEntry.COLUMN_FAV);

        String fav = cursor.getString(currentfavindex);
        String currentRead=  cursor.getString(currentReadIndex);
        pdfnametoOpen = cursor.getString(pdfnameIndex);

        // this adds the pdfname of each book inside pdfnamelist and add 1 if that book is
        //  already present in ebooksFragment and 0 otherwise
        if(!(pdfnamelist.contains(pdfnametoOpen))) {
            pdfnamelist.add(pdfnametoOpen);
            if(fav.equals("1")){
                favlist.add(pdfnametoOpen);
            }
            if(!currentRead.equals("0")){
                alreadypresent.add(1);
            } else {
                alreadypresent.add(0);
            }
        }

        // Get a reference to the ListView, and attach the adapter to the listView.
        String book = cursor.getString(bookNameIndex);
        String author = cursor.getString(authorNameIndex);
        String imageLink = cursor.getString(imageLinkIndex);
        bookName.setText(book);
        authorName.setText(author);
        if ((imageLink != null) || (imageLink.length() > 0)) {
            Picasso.with(context).load(imageLink).placeholder(R.drawable.home_fragment_image).into(imageView);
        } else {
            Picasso.with(context).load(R.drawable.home_fragment_image).into(imageView);
        }
        arrow.setText(">");
    }



    public ArrayList<Integer> getLs1(){
         return alreadypresent;
    }
    public ArrayList<String> getfavlist(){
        return favlist;
    }
    public ArrayList<String> getpdfnamelist(){
        return pdfnamelist;
    }
}