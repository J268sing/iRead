package com.example.android.iread;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class listadapter extends ArrayAdapter<list_class> {


    // a list that contains pdfnames of the books present in the ebooksFragment
    public static ArrayList<String> pdfnamelist = new ArrayList<>();


    public static ArrayList<String> favlist = new ArrayList<>();

    //alreadypresent list contains one at poisiton i if the book is already present in
    //  currentReadFragment and 0 otherwise
    public static ArrayList<Integer> alreadypresent = new ArrayList<>();

    public listadapter(Activity context, ArrayList<list_class> booksList) {
        super(context, 0, booksList);
    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view

        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

        }


        // Get the {@link AndroidFlavor} object located at this position in the list

        list_class currentlist_class = getItem(position);

        String fav = currentlist_class.getmPages();
        String currentRead=  currentlist_class.getmdetails();
        String pdfnametoOpen = currentlist_class.getMpdfName();

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

        // Find the TextView in the list_item.xml layout with the ID version_name

        TextView bookNameTextView = (TextView) listItemView.findViewById(R.id.name);



        // Get the version name from the current AndroidFlavor object and

        // set this text on the name TextView

        bookNameTextView.setText(currentlist_class.getmBookName());


        // Find the TextView in the list_item.xml layout with the ID version_number

        TextView AuthorTextView = (TextView) listItemView.findViewById(R.id.author);

        // Get the version number from the current AndroidFlavor object and

        // set this text on the number TextView

        AuthorTextView.setText(currentlist_class.getmAuthorName());

        // Find tf /] he ImageView in the list_item.xml layout with the ID list_item_icon

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        // Get the image resource ID from the current AndroidFlavor object and

        // set the image to iconView
        if((currentlist_class.getmImage() != null)|| (currentlist_class.getmImage().length()>0)) {
            Picasso.with(getContext()).load(currentlist_class.getmImage()).placeholder(R.drawable.home_fragment_image).into(imageView);
        } else {
            Picasso.with(getContext()).load(R.drawable.home_fragment_image).into(imageView);
        }

        // Return the whole list item layout (containing 2 TextViews and an ImageView)

        // so that it can be shown in the ListView

        return listItemView;
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
