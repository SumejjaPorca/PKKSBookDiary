package ba.unsa.etf.pkks.sil.myapplication.Backand;

import android.content.Context;
import android.database.sqlite.*;

/**
 * Created by Sumejja on 3.6.2015.
 */
public class BookDiaryDbHelper extends SQLiteOpenHelper {

        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "BookDiary.db";

        private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String PRIMARY_KEY_TYPE = " INTEGER PRIMARY KEY AUTOINCREMENT,";
    private static final String DATE_TYPE = " Date";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + BookDiaryContract.BookEntry.TABLE_NAME_BOOK + " (" +
                        BookDiaryContract.BookEntry.COLUMN_NAME_BOOK_ID +  PRIMARY_KEY_TYPE +
                        BookDiaryContract.BookEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        BookDiaryContract.BookEntry.COLUMN_NAME_AUTHOR + TEXT_TYPE + COMMA_SEP +
                        BookDiaryContract.BookEntry.COLUMN_NAME_ISBN + TEXT_TYPE + COMMA_SEP +
                        BookDiaryContract.BookEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                        BookDiaryContract.BookEntry.COLUMN_NAME_PHOTO_LINK + TEXT_TYPE + COMMA_SEP +
                        BookDiaryContract.BookEntry.COLUMN_NAME_PAGES + INT_TYPE + COMMA_SEP +
                        BookDiaryContract.BookEntry.COLUMN_NAME_STATUS + INT_TYPE + COMMA_SEP +
                        BookDiaryContract.BookEntry.COLUMN_NAME_DATE + DATE_TYPE +
                        // Any other options for the CREATE command
                " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + BookDiaryContract.BookEntry.TABLE_NAME_BOOK;

        public BookDiaryDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

}
