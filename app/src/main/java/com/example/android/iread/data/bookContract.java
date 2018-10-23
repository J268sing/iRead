package com.example.android.iread.data;






import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class bookContract {

    //constructor
    private bookContract(){};

    public static final String CONTENT_AUTHORITY = "com.example.android.database";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "database";

    public static final class bookEntry implements BaseColumns {

        //Name of the uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);
        // nae of the table containing book information
        public static final String TABLE_NAME = "books";


        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";               //notnull
        public static final String COLUMN_AUTHOR_NAME = "author";       // can be null
        public static final String COLUMN_IMAGE_LINK = "image";         // not null
        public static final String COLUMN_PDF_LINK = "pdflink";         // not null
        public static final String COLUMN_PAGES = "pages";             // can be null
        public static final String COLUMN_PUBLISHED_DATE = "publisheddate"; // can be null
        public static final String COLUMN_DESCRIPTION = "description";     // can be null
        public static final String COLUMN_PDF_NAME = "pdfname";            //notnull
        public static final String COLUMN_CURRENT_READS = "currentreads";
        public static final String COLUMN_FAV = "fav";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

    }

    public static  final class profileEntry implements BaseColumns{

        // name of the table that has user's profile information
        public static final String TABLE_NAME = "profile";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FAV_AUTHOR = "favauthor";
        public static final String COLUMN_FAV_QUOTE = "favquote";

    }
}

