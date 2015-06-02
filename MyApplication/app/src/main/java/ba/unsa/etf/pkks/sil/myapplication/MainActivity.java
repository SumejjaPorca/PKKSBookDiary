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
import ba.unsa.etf.pkks.sil.myapplication.Backand.BooksAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "ba.unsa.etf.pkks.sil.myapplication.MainActivity.MESSAGE";

    private ArrayList<Book> books;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        books = new ArrayList<Book>();
        dodajKnjige();

        list = (ListView) findViewById(R.id.listView);

        BooksAdapter adapter = new BooksAdapter(books, this);
        list.setAdapter(adapter);


    }

    void dodajKnjige(){
        if(books == null)
            books = new ArrayList<Book>();

        Book b1 = new Book();
        b1.setTitle("Orlovi rano lete");
        b1.setAuthor("Branko Ćopić");
        b1.setIsbn("ISBN 978-86-01-01688-0");
        b1.setDescription("Dječja knjiga.");
        b1.setPublishDate(new Date());
        b1.setPagesNumber(190);

        books.add(b1);


        Book b2 = new Book();
        b2.setTitle("Parfem");
        b2.setAuthor("Branko Ćopić");
        b2.setIsbn("ISBN 978-86-01-01688-0");
        b2.setDescription("Dječja knjiga.");
        b2.setPublishDate(new Date());
        b2.setPagesNumber(190);
        b2.setStatus(1);

        books.add(b2);
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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.send_message){
            sendMessage(findViewById(R.id.send_message));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText text = (EditText) findViewById(R.id.edit_message);
        String message = text.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void showBook(long bookId){

    }
}