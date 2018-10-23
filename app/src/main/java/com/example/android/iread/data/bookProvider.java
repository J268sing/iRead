package com.example.android.iread.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;




public class bookProvider extends ContentProvider {

    public static final String LOG_TAG = bookProvider.class.getSimpleName();

    private static final int books = 100;
    private static final int books_id = 101;
    private bookdbHelper mbookdbHelper;
    private static final UriMatcher sUrimatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUrimatcher.addURI(bookContract.CONTENT_AUTHORITY, bookContract.PATH_BOOKS  , books);
        sUrimatcher.addURI(bookContract.CONTENT_AUTHORITY, bookContract.PATH_BOOKS +"/#" , books_id);
    }
    @Override
    public boolean onCreate() {
        mbookdbHelper = new bookdbHelper(getContext());
        return true;
    }



    /**

     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.

     */



    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,@Nullable String selection,@Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mbookdbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUrimatcher.match(uri);
        switch (match) {
            case books:
                cursor = database.query(bookContract.bookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case books_id:
                selection = bookContract.bookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(bookContract.bookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }



    /**

     * Returns the MIME type of data for the content URI.

     */


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUrimatcher.match(uri);
        switch (match) {
            case books:
                return bookContract.bookEntry.CONTENT_LIST_TYPE;
            case books_id:
                return bookContract.bookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);

        }
    }


    /**

     * Insert new data into the provider with the given ContentValues.

     */



    @Nullable
    @Override

    public Uri insert(@NonNull  Uri uri,@NonNull ContentValues contentValues) {

        final int match = sUrimatcher.match(uri);
        switch (match) {
            case books:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }





    private Uri insertBook(Uri uri, ContentValues values){



        String title = values.getAsString(bookContract.bookEntry.COLUMN_TITLE);
        if((title == null)||(title.length() == 0)){
            throw new IllegalArgumentException("Book requires a title " + uri);
        }
        String author = values.getAsString(bookContract.bookEntry.COLUMN_AUTHOR_NAME);
        String imageLink = values.getAsString(bookContract.bookEntry.COLUMN_IMAGE_LINK);

        if((imageLink == null)||(imageLink.length() == 0)){
            throw new IllegalArgumentException("Book requires a title " + uri);
        }
        String pdfLink = values.getAsString(bookContract.bookEntry.COLUMN_PDF_LINK);
        if((pdfLink == null)||(pdfLink.length() == 0)){
            throw new IllegalArgumentException("Book requires a title " + uri);
        }
        String pages = values.getAsString(bookContract.bookEntry.COLUMN_PAGES);
        String publishedDate = values.getAsString(bookContract.bookEntry.COLUMN_PUBLISHED_DATE);
        String description = values.getAsString(bookContract.bookEntry.COLUMN_DESCRIPTION);
        String pdfName = values.getAsString(bookContract.bookEntry.COLUMN_PDF_NAME);
        if((pdfName == null)||(pdfName.length() == 0)){
            throw new IllegalArgumentException("Book requires a title " + uri);
        }

        SQLiteDatabase database = mbookdbHelper.getWritableDatabase();
        long id = database.insert(bookContract.bookEntry.TABLE_NAME, null, values);

        if(id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**

     * Updates the data at the given selection and selection arguments, with the new ContentValues.

     */


    @Override

    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


    /**

     * Delete the data at the given selection and selection arguments.

     */


    @Override

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mbookdbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUrimatcher.match(uri);
        switch (match) {
            case books:
                rowsDeleted = database.delete(bookContract.bookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case books_id:
                selection = bookContract.bookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(bookContract.bookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
}