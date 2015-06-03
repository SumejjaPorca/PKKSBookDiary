package ba.unsa.etf.pkks.sil.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import ba.unsa.etf.pkks.sil.myapplication.Backand.Book;
import ba.unsa.etf.pkks.sil.myapplication.Backand.BookDAO;
import ba.unsa.etf.pkks.sil.myapplication.Backand.BooksAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "ba.unsa.etf.pkks.sil.myapplication.MainActivity.MESSAGE";
    public static final String EXTRA_BOOKID = "ba.unsa.etf.pkks.sil.myapplication.MainActivity.BOOKID";

    private ListView list;
    private BookDAO mBooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mBooks = new BookDAO(getBaseContext());
        mBooks.open();



/*
        Book b2 = new Book();
        b2.setTitle("Parfem");
        b2.setAuthor("Branko Ćopić");
        b2.setIsbn("ISBN 978-86-01-01688-2");
        b2.setDescription("Knjiga istaknutog fracuskog književnika.");
        b2.setPublishDate(new Date());
        b2.setPagesNumber(190);
        b2.setStatus(1);
        mBooks.addBook(b2);
        */

        list = (ListView) findViewById(R.id.listView);

        BooksAdapter adapter = new BooksAdapter(mBooks.getAllBooks(), this);
        list.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_book) {
            Intent intent = new Intent(this,AddBook.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void showBook(long bookId){
        Intent intent = new Intent(this, BookDisplayActivity.class);
        intent.putExtra(EXTRA_BOOKID, bookId);
        startActivity(intent);
    }

    public void searchBooks(View view) {
        EditText edit = (EditText) findViewById(R.id.edit_search_title);
        String title = edit.getText().toString();

        BooksAdapter adapter = new BooksAdapter(mBooks.getAllByTitle(title), this);
        list.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        mBooks.open();

    }

    @Override
    public void onPause() {
        super.onPause();
        mBooks.close();
    }
}