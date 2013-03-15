package org.andrewfenner.commonreporting;
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

/**
 * Provides access to a database of notes. Each note has a title, the note
 * itself, a creation date and a modified data.
 */
public class SaveDataHelper extends ContentProvider {
	public static final String AUTHORITY = "org.biodiversityireland.butterfly";

    private static final String TAG = "SaveDataHelper";

    private static final String DATABASE_NAME = "butterfly.db";
    private static final int DATABASE_VERSION = 1;
    private static final String BUTTERFLY_TABLE_NAME = "recordedbutterflys";

    private static HashMap<String, String> sNotesProjectionMap;
   // private static HashMap<String, String> sLiveFolderProjectionMap;

    private static final int SENT = 1;
    private static final int RECORD_ID = 2;
 //   private static final int LIVE_FOLDER_NOTES = 3;

    private static final UriMatcher sUriMatcher;

    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + BUTTERFLY_TABLE_NAME + " ("
                    + ButterflyColumns._ID + " INTEGER PRIMARY KEY,"
                    + ButterflyColumns.NAME + " TEXT,"
                    + ButterflyColumns.GRIDREF + " TEXT,"
                    + ButterflyColumns.LONGLAT + " TEXT,"
                    + ButterflyColumns.LOCATION + " TEXT,"
                    + ButterflyColumns.SPECIES + " TEXT,"
                    + ButterflyColumns.COMMENT + " TEXT,"
                    + ButterflyColumns.SENT + " BOOLEAN,"
                      + ButterflyColumns.DATE + " INTEGER,"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS butterfly");
            onCreate(db);
        }
    }

    private DatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(BUTTERFLY_TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
        case SENT:
            qb.setProjectionMap(sNotesProjectionMap);
            break;

      

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = ButterflyColumns.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case SENT:
      
            return ButterflyColumns.CONTENT_ITEM_TYPE;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        // Validate the requested uri
        if (sUriMatcher.match(uri) != RECORD_ID) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        Long now = Long.valueOf(System.currentTimeMillis());

       

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(BUTTERFLY_TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(ButterflyColumns.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case SENT:
            count = db.delete(BUTTERFLY_TABLE_NAME, where, whereArgs);
            break;

        case RECORD_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.delete(BUTTERFLY_TABLE_NAME, ButterflyColumns._ID + "=" + noteId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case SENT:
            count = db.update(BUTTERFLY_TABLE_NAME, values, where, whereArgs);
            break;

        case RECORD_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.update(BUTTERFLY_TABLE_NAME, values, ButterflyColumns._ID + "=" + noteId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "sent", SENT);
        sUriMatcher.addURI(AUTHORITY, "notes/#", RECORD_ID);
        //sUriMatcher.addURI(AUTHORITY, "live_folders/notes", LIVE_FOLDER_NOTES);

        sNotesProjectionMap = new HashMap<String, String>();
        sNotesProjectionMap.put(ButterflyColumns._ID, ButterflyColumns._ID);
       
        sNotesProjectionMap.put(ButterflyColumns.NAME, ButterflyColumns.NAME);
        sNotesProjectionMap.put(ButterflyColumns.GRIDREF, ButterflyColumns.GRIDREF);
        sNotesProjectionMap.put(ButterflyColumns.LONGLAT, ButterflyColumns.LONGLAT);
        sNotesProjectionMap.put(ButterflyColumns.LOCATION, ButterflyColumns.LOCATION);
        sNotesProjectionMap.put(ButterflyColumns.SPECIES, ButterflyColumns.SPECIES);
        sNotesProjectionMap.put(ButterflyColumns.COMMENT, ButterflyColumns.COMMENT);
        sNotesProjectionMap.put(ButterflyColumns.SENT, ButterflyColumns.SENT);
        sNotesProjectionMap.put(ButterflyColumns.DATE, ButterflyColumns.DATE);

        // Support for Live Folders.
      //  sLiveFolderProjectionMap = new HashMap<String, String>();
      //  sLiveFolderProjectionMap.put(LiveFolders._ID, ButterflyColumns._ID + " AS " +
      //          LiveFolders._ID);
      //  sLiveFolderProjectionMap.put(LiveFolders.NAME, ButterflyColumns.TITLE + " AS " +
       //         LiveFolders.NAME);
        // Add more columns here for more robust Live Folders.
    }
    
    
    /**
     * Notes table
     */
    public static final class ButterflyColumns implements BaseColumns {
        // This class cannot be instantiated
        private ButterflyColumns() {}

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/butterfly");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        /**
         * The title of the note
         * <P>Type: TEXT</P>
         */
        public static final String NAME = "name";

        /**
         * The note itself
         * <P>Type: TEXT</P>
         */
        public static final String GRIDREF = "gridref";
        public static final String LONGLAT = "longlat";

        public static final String LOCATION = "location";
        public static final String COMMENT = "comment";
        public static final String SPECIES = "species";
        public static final String SENT = "sent";

        /**
         * The timestamp for when the note was created
         * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
         */
        public static final String DATE = "recorddate";

       
    }
}
