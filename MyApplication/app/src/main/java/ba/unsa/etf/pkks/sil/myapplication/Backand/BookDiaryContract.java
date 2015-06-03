package ba.unsa.etf.pkks.sil.myapplication.Backand;


import android.provider.BaseColumns;

/**
 * Created by Sumejja on 3.6.2015.
 */
public final class BookDiaryContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public BookDiaryContract() {}

        /* Inner class that defines the table contents */
        public static abstract class BookEntry implements BaseColumns {
            public static final String TABLE_NAME_BOOK = "Book";
            public static final String COLUMN_NAME_BOOK_ID = "id";
            public static final String COLUMN_NAME_TITLE = "title";
            public static final String COLUMN_NAME_AUTHOR = "author";
            public static final String COLUMN_NAME_ISBN = "isbn";
            public static final String COLUMN_NAME_DATE = "publishDate";
            public static final String COLUMN_NAME_PAGES = "pagesNumber";
            public static final String COLUMN_NAME_STATUS = "status";
            public static final String COLUMN_NAME_DESCRIPTION = "description";
            public static final String COLUMN_NAME_PHOTO_LINK = "photoLink";
        }
    }
