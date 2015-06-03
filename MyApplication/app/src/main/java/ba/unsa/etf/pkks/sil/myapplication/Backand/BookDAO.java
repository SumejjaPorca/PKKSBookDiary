package ba.unsa.etf.pkks.sil.myapplication.Backand;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by luka454 on 3.6.2015.
 */
public class BookDAO
{
    private static final String TAG = BookDAO.class.getSimpleName();

    private SQLiteDatabase mDatabase;
    private BookDiaryDbHelper mBookDiaryDataHelper;

    private static final String[] ALL_COLUMNS = {
            BookDiaryContract.BookEntry.COLUMN_NAME_BOOK_ID, BookDiaryContract.BookEntry.COLUMN_NAME_STATUS,
            BookDiaryContract.BookEntry.COLUMN_NAME_PAGES, BookDiaryContract.BookEntry.COLUMN_NAME_PHOTO_LINK,
            BookDiaryContract.BookEntry.COLUMN_NAME_DESCRIPTION, BookDiaryContract.BookEntry.COLUMN_NAME_AUTHOR,
            BookDiaryContract.BookEntry.COLUMN_NAME_DATE, BookDiaryContract.BookEntry.COLUMN_NAME_ISBN,
            BookDiaryContract.BookEntry.COLUMN_NAME_TITLE
    };

    public BookDAO(Context context) {
        Log.i(TAG,"BookDAO()");
        mBookDiaryDataHelper = new BookDiaryDbHelper(context);
    }

    public void open() {
        Log.i(TAG,"open()");
        mDatabase = mBookDiaryDataHelper.getWritableDatabase();
    }

    public void close() {
        Log.i(TAG,"close()");
        mDatabase.close();
        mBookDiaryDataHelper.close();
    }


    public long addBook(Book book){

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BookDiaryContract.BookEntry.COLUMN_NAME_TITLE, book.getTitle());
        values.put(BookDiaryContract.BookEntry.COLUMN_NAME_AUTHOR, book.getAuthor());
        values.put(BookDiaryContract.BookEntry.COLUMN_NAME_DATE, getDateTime(book.getPublishDate()));
        values.put(BookDiaryContract.BookEntry.COLUMN_NAME_ISBN, book.getIsbn());
        values.put(BookDiaryContract.BookEntry.COLUMN_NAME_DESCRIPTION, book.getDescription());
        values.put(BookDiaryContract.BookEntry.COLUMN_NAME_PAGES, book.getPagesNumber());
        values.put(BookDiaryContract.BookEntry.COLUMN_NAME_PHOTO_LINK, book.getPhotoLink());
        values.put(BookDiaryContract.BookEntry.COLUMN_NAME_STATUS, book.getStatus());

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = mDatabase.insert(
                BookDiaryContract.BookEntry.TABLE_NAME_BOOK,
                null,
                values);
        return newRowId;
    }

    public int updateStatus(Book book){
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(BookDiaryContract.BookEntry.COLUMN_NAME_STATUS, book.getStatus());

        // Which row to update, based on the ID
        String selection = BookDiaryContract.BookEntry.COLUMN_NAME_BOOK_ID + " = ?";
        String[] selectionArgs = { String.valueOf(book.getId())};

        int count = mDatabase.update(
                BookDiaryContract.BookEntry.TABLE_NAME_BOOK,
                values,
                selection,
                selectionArgs);
        return count;
    }
    public int updatePhoto(Book book){
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(BookDiaryContract.BookEntry.COLUMN_NAME_PHOTO_LINK, book.getPhotoLink());

        // Which row to update, based on the ID
        String selection = BookDiaryContract.BookEntry.COLUMN_NAME_BOOK_ID + " = ?";
        String[] selectionArgs = { String.valueOf(book.getId())};

        int count = mDatabase.update(
                BookDiaryContract.BookEntry.TABLE_NAME_BOOK,
                values,
                selection,
                selectionArgs);
        return count;
    }


    public Book getById(long id){
        String selectQuery = BookDiaryContract.BookEntry.COLUMN_NAME_BOOK_ID + " = ?";
        Cursor c = mDatabase.query(BookDiaryContract.BookEntry.TABLE_NAME_BOOK, ALL_COLUMNS, selectQuery, new String[]{String.valueOf(id)}, null, null, null);

        if (c.moveToFirst()) {
            Book book = cursorToBook(c);
            return book;
        }
        return null;
    }



    public List<Book> getAllBooks(){
        Log.i(TAG, "getAllBooks()");
        List<Book> books = new ArrayList<>();
        Cursor cursor = mDatabase.query(BookDiaryContract.BookEntry.TABLE_NAME_BOOK, ALL_COLUMNS, null, null, null, null, null);
        while (cursor.moveToNext()) {
            books.add(cursorToBook(cursor));
        }

        return books;
    }


    private static Book cursorToBook(Cursor cursor) {
        Log.i(TAG, "cursorToBook()");
        int id = cursor.getInt(cursor.getColumnIndex(BookDiaryContract.BookEntry.COLUMN_NAME_BOOK_ID));
        String title = cursor.getString(cursor.getColumnIndex(BookDiaryContract.BookEntry.COLUMN_NAME_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(BookDiaryContract.BookEntry.COLUMN_NAME_DESCRIPTION));
        String author = cursor.getString(cursor.getColumnIndex(BookDiaryContract.BookEntry.COLUMN_NAME_AUTHOR));
        String date = cursor.getString(cursor.getColumnIndex(BookDiaryContract.BookEntry.COLUMN_NAME_DATE));
        int pages = cursor.getInt(cursor.getColumnIndex(BookDiaryContract.BookEntry.COLUMN_NAME_PAGES));
        int status = cursor.getInt(cursor.getColumnIndex(BookDiaryContract.BookEntry.COLUMN_NAME_STATUS));
        String isbn = cursor.getString(cursor.getColumnIndex(BookDiaryContract.BookEntry.COLUMN_NAME_ISBN));
        String photo = cursor.getString(cursor.getColumnIndex(BookDiaryContract.BookEntry.COLUMN_NAME_PHOTO_LINK));

        Date d = setDateTime(date);
        return new Book(id, title, description, author, d, pages, status, isbn, photo);

    }

    public List<Book> getAllByTitle(String title){
        String selectQuery = BookDiaryContract.BookEntry.COLUMN_NAME_TITLE + " LIKE ?";
        Cursor c = mDatabase.query(BookDiaryContract.BookEntry.TABLE_NAME_BOOK, ALL_COLUMNS, selectQuery, new String[]{'%' + title + '%'}, null, null, null);


        ArrayList<Book> result = new ArrayList<>();
        while (c.moveToNext()) {
            result.add(cursorToBook(c));

        }
        return result;
    }

    public void checkout(){
        // delete all books
        mDatabase.delete(BookDiaryContract.BookEntry.TABLE_NAME_BOOK,null,null);
    }

    private static String getDateTime(Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(d);
    }

    private static Date setDateTime(String d){
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = iso8601Format.parse(d);
            return date;
        } catch (ParseException e) {
            Log.e(TAG, "Parsing ISO8601 datetime failed", e);
            return new Date(); //this si just temporarily
        }
    }
}
