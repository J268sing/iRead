package com.example.android.iread;


import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.android.iread.data.bookContract;
import com.example.android.iread.data.bookContract;
import com.example.android.iread.data.bookdbHelper;
import com.squareup.picasso.Picasso;


public class  bookWithAdapterActivity extends AppCompatActivity {
    // a variable to check the availability of network connection
    int networkavailable = 1;
    // A variable that contains the link to download the selected book
    String link = "";
    // A variable that contains the name of the book which is going to be downloaded
    String name = "";
    //a variable for book database
    public static bookdbHelper mdbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_with_adapter);

        //inflate the activity with the layout of the book that has been selected to download
        updateBookUi();

        //inititalize the  bookdbHelper;
        mdbhelper = new bookdbHelper(this);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkavailable = checknetwork();
                if(networkavailable == 1) {

                    //get the download link of the book that has been selected to download
                    link = ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getMpdfLink();
                    //get the name of the book thas has been selected to download
                    name = ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getmBookName() + ".pdf";
                    //check if memory is availabe for the book to be downloaded
                    Boolean memoerAvailable = (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
                    if (memoerAvailable) {
                        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
                        request.setTitle("Click to download the book");
                        request.setDescription("File is being downloaded...");
                        //CookieManager cookieManager = CookieManager.getInstance();
                        //  String cookie = cookieManager.getCookie(link);
                        // request.addRequestHeader("Cookie", cookie);


                        //use downlaod manager to download the clicked book
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getMpdfName());
                        request.setDestinationInExternalFilesDir(bookWithAdapterActivity.this, Environment.DIRECTORY_DOWNLOADS, "myfile.jpg");
                        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        final long queueid = manager.enqueue(request);
                        // enq=dm.enqueue(r);
                        //  final DownloadTask downloadTask = new DownloadTask(getApplicationContext());
                        //downloadTask.execute(link);

                        //add the book in myeBooksList
                        myeBooksFragment.myeBooksList.add(ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos));

                        // add the downloaded book into the database
                        inserts();
            /*    BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                            DownloadManager.Query query = new DownloadManager.Query();
                            query.setFilterById(jshan);

                            Cursor c = manager.query(query);
                            if (c.moveToFirst()) {
                                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                                    String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                                    //TODO : Use this local uri and launch intent to open file

                                    String titlexy = uriString.substring(uriString.lastIndexOf('/') + 1, uriString.length());
                                    myeBooksFragment.dmMyEbooksList.add(titlexy);
                                }
                            }
                        }
                    }
                };*/
                        //  registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    }
                }
            }
        });
    }


    //a helper function used to insert the details of downloaded book into database
    private void inserts() {
        SQLiteDatabase db = mdbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        myeBooksFragment.myeBooksList.add(ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos));
        values.put(bookContract.bookEntry.COLUMN_TITLE,  ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getmBookName());//ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getmBookName());
        values.put(bookContract.bookEntry.COLUMN_AUTHOR_NAME,  ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getmAuthorName());
        values.put(bookContract.bookEntry.COLUMN_IMAGE_LINK,  ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getmImage());
        values.put(bookContract.bookEntry.COLUMN_PAGES,   ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getmPages());
        values.put(bookContract.bookEntry.COLUMN_PUBLISHED_DATE,   ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getmPublishedDate());
        values.put(bookContract.bookEntry.COLUMN_DESCRIPTION,  ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getmdetails());
        values.put(bookContract.bookEntry.COLUMN_PDF_LINK,  ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getMpdfLink());
        values.put(bookContract.bookEntry.COLUMN_PDF_NAME,   ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos).getMpdfName());
        values.put(bookContract.bookEntry.COLUMN_CURRENT_READS, "0");
        values.put(bookContract.bookEntry.COLUMN_FAV, "0");
        long newRowId = db.insert(bookContract.bookEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Download the book by clicking on the notification" , Toast.LENGTH_SHORT).show();
        }
    }

    // a helper function to check if internet is available
    public int checknetwork() {
        ConnectivityManager checkConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo checkNetwork = checkConnectivity.getActiveNetworkInfo();
        if (checkNetwork == null || !checkNetwork.isConnected()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            return 1;
        }
    }

    // uddate the interface with the information of book that has been clicked
    public void updateBookUi() {
        Object list_class = ShowbooksActivity.myBooksList.get(ShowbooksActivity.pos);
        list_class listItem = (list_class) list_class;
        TextView bookName = findViewById(R.id.bookName);
        bookName.setText(listItem.getmBookName());
        TextView authorName = findViewById(R.id.authorName);
        authorName.setText(listItem.getmAuthorName());
        TextView pagesText = findViewById(R.id.pagesText);
        if (listItem.getmPages().length() > 0) {
            pagesText.setText("Pages: ");
        } else {
            pagesText.setText("");
        }
        TextView pages = findViewById(R.id.pages);
        pages.setText(listItem.getmPages());
        TextView publishedDate = findViewById(R.id.publishedDate);
        publishedDate.setText(listItem.getmPublishedDate());
        TextView details = findViewById(R.id.description);
        ImageView imageView = (ImageView) findViewById(R.id.imageunique);
        if (listItem.getmdetails().length() == 0) {
            details.setText(listItem.getmBookName());
        } else {
            details.setText(listItem.getmdetails());
        }
        if ((listItem.getmImage() != null) || (listItem.getmImage().length() > 0)) {
            Picasso.with(getApplicationContext()).load(listItem.getmImage()).placeholder(R.drawable.home_fragment_image).into(imageView);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.home_fragment_image).into(imageView);
        }
    }

}