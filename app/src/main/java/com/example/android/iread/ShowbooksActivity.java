package com.example.android.iread;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class ShowbooksActivity extends AppCompatActivity {
    public static final String LOG_TAG = ShowbooksActivity.class.getSimpleName();
    public static int pos;

    public static int subject = 0;
    int networkAvailable = 1;
    static ArrayList<list_class> myBooksList = new ArrayList<list_class>();
    static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbooks);
        googleBookSync googlebooksync = new googleBookSync();
        googlebooksync.execute();
    }


    //update the interface with list of the results given by googleBookSync class
    public void updateUi(ArrayList<list_class> myBooksList) {
        listadapter adapter = new listadapter(this, myBooksList);

        // Get a reference to the ListView, and attach the adapter to the listView.
        final ListView listView = (ListView) findViewById(R.id.list8);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                networkAvailable = checknetwork();
                if(networkAvailable == 1) {
                    pos = position;
                    Intent myIntent = new Intent(view.getContext(), bookWithAdapterActivity.class);
                    startActivity(myIntent);
                }
            }
        });

    }

    // check if network is available
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


    //this function sets no book found on the  activity if there is no book found for a particular activity
    public void noBookfound (){
        TextView textView = (TextView) findViewById(R.id.noTitle);
        textView.setText("No Book Found! Please Try Again");
        Toast.makeText(getApplicationContext(), Integer.toString(count), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), searchActivity.searchedBook, Toast.LENGTH_LONG).show();
    }


    // this class is used to make a http connection and recieve a JSON response using Google API. The response
    // has the information about the result related to particular search
    private class googleBookSync extends AsyncTask<URL, Void, ArrayList<list_class>> {
        @Override
        protected ArrayList<list_class> doInBackground(URL... urls) {
            String urlstring = "https://www.googleapis.com/books/v1/volumes?q=" + searchActivity.searchedBook + "&maxResults=40&filter=free-ebooks&key=AIzaSyDzFtO1XX1KMob7c0NFH3mXXBmXFlga4lA";
            if(subject == 0){
                urlstring = "https://www.googleapis.com/books/v1/volumes?q=" + searchActivity.searchedBook + "&maxResults=40&filter=free-ebooks&key=AIzaSyDzFtO1XX1KMob7c0NFH3mXXBmXFlga4lA";
            } else if (subject == 1) {
                urlstring = "https://www.googleapis.com/books/v1/volumes?q="   + "subject:" +searchActivity.searchedBook  +"&maxResults=40&filter=free-ebooks&key=AIzaSyDzFtO1XX1KMob7c0NFH3mXXBmXFlga4lA";
            } else {
                urlstring = "https://www.googleapis.com/books/v1/volumes?q="   + "inauthor:" +searchActivity.searchedBook  +"&maxResults=40&filter=free-ebooks&key=AIzaSyDzFtO1XX1KMob7c0NFH3mXXBmXFlga4lA";
            }
            URL url = createUrl(urlstring);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }
            ArrayList<list_class> ans = extractFeatureFrom(jsonResponse);
            return ans;
        }

        @Override
        protected void onPostExecute(ArrayList<list_class> e) {
            if (e.size() > 0) {
                updateUi(e);
                Log.i("jatt", " ----  " + " apple ");
            } else {
                noBookfound();
                Log.i("jashan", " ----  " + "orange");
            }
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);

            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                try {
                    inputStream = urlConnection.getInputStream();
                } catch (IOException e) {
                }

                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;

        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private ArrayList<list_class> extractFeatureFrom(String response) {
            try {
                //
                JSONObject Response = new JSONObject(response);
                //  JSONObject base = baseJsonResponse.getJSONObject("list");
                int totalBooksFound = Response.getInt("totalItems");

                int myBooksListSize = myBooksList.size();

                while (myBooksListSize != 0) {
                    myBooksList.remove(myBooksListSize - 1);
                    myBooksListSize = myBooksListSize - 1;
                }

                if (totalBooksFound == 0) {
                    return myBooksList;
                }

                JSONArray bookInfo = Response.getJSONArray("items");
                int len = bookInfo.length();

                for (int i =  0; i < len; i++) {
                    JSONObject volume = bookInfo.getJSONObject(i);
                    JSONObject volinfo = volume.getJSONObject("volumeInfo");
                    String title = "";
                    if (volinfo.has("title")) {
                        title = volinfo.getString("title");
                    }

                    String trimmedTitle = "";


                    if (title.length() != 0) {
                        String[] titleArray = title.split(" ");

                        if (titleArray.length > 7) {
                            for (int k = 0; k < 7; k++) {
                                trimmedTitle = trimmedTitle.concat(titleArray[k] + " ");
                            }
                            trimmedTitle = trimmedTitle.concat("...");
                        } else {
                            for (int j = 0; j < titleArray.length; j++) {
                                trimmedTitle = trimmedTitle.concat(titleArray[j] + " ");
                            }
                            trimmedTitle = title;
                        }
                    }

                    JSONArray author;
                    String trimmedAuthor = "";
                    if (volinfo.has("authors")) {
                        author = volinfo.getJSONArray("authors");
                        int authorLen = author.length();
                        trimmedAuthor = author.getString(0);
                        for (int m = 1; m < authorLen; m++) {
                            trimmedAuthor = trimmedAuthor + " , " + author.getString(m);
                        }
                    }
                    String thumbnail = "";
                    String smallThumbnail = "";
                    if (volinfo.has("imageLinks")) {
                        JSONObject imagelinks = volinfo.getJSONObject("imageLinks");

                        if (imagelinks.has("smallThumbnail")) {
                            smallThumbnail = imagelinks.getString("smallThumbnail");
                        }
                        if (imagelinks.has("thumbnail")) {
                            thumbnail = imagelinks.getString("thumbnail");
                        }
                    }
                    JSONObject accessinfo;
                    JSONObject pdfObject = null;
                    String pdfLink = "";
                    if (volume.has("accessInfo")) {
                        accessinfo = volume.getJSONObject("accessInfo");
                        if (accessinfo.has("pdf")) {
                            pdfObject = accessinfo.getJSONObject("pdf");
                            Log.i("jashan", "xsxxs");
                        }
                        if (pdfObject.has("downloadLink")) {
                            pdfLink = pdfObject.getString("downloadLink");
                        }
                    }

                    String pdfname = "";
                    String trimmedpdfname = "";
                    if (pdfLink.length() != 0) {
                        String[] pdfName = pdfLink.split("/");
                        for (int z = 0; z < pdfName.length; z++) {
                            if (pdfName[z].contains(".pdf")) {
                                pdfname = pdfName[z];
                            }
                        }

                        for (int k = 0; k < pdfname.length(); k++) {
                            Character.toString(pdfname.charAt(0));
                            if (((Character.toString(pdfname.charAt(k))) + (Character.toString(pdfname.charAt(k + 1))) + (Character.toString(pdfname.charAt(k + 2))) + (Character.toString(pdfname.charAt(k + 3)))).equals(".pdf")) {
                                trimmedpdfname = trimmedpdfname + ".pdf";
                                break;
                            } else {
                                trimmedpdfname = trimmedpdfname + Character.toString(pdfname.charAt(k));
                            }
                        }
                    }

                    String pages = "";
                    if (volinfo.has("pageCount")) {
                        pages = String.valueOf(volinfo.getInt("pageCount"));
                    }

                    String publishedDate = "jhgg";
                    if (volinfo.has("publishedDate")) {
                        publishedDate = String.valueOf(volinfo.getInt("publishedDate"));
                    }
                    String trimmedDetails = "";
                    if (volume.has("searchInfo")) {
                        JSONObject textSnippet = volume.getJSONObject("searchInfo");
                        String details = textSnippet.getString("textSnippet");
                        String[] detailArray = details.split(" ");
                        int lenArray = detailArray.length;
                        for (int k = 0; k < lenArray; k++) {
                            if (!detailArray[k].contains("<b")) {
                                trimmedDetails = trimmedDetails + " " + detailArray[k];
                            }
                        }
                        trimmedDetails = trimmedDetails + "...";
                    }
                    if ((pdfLink.length() > 0) && (thumbnail.length() > 0)) {
                        myBooksList.add(new list_class(trimmedTitle, trimmedAuthor, thumbnail, pages, publishedDate, trimmedDetails, pdfLink, trimmedpdfname));
                    }
                }
                return myBooksList;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return myBooksList;
        }
    }
}


