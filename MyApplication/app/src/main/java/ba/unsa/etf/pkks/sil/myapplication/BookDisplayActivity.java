package ba.unsa.etf.pkks.sil.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ba.unsa.etf.pkks.sil.myapplication.Backand.Book;
import ba.unsa.etf.pkks.sil.myapplication.Backand.BookDAO;

public class BookDisplayActivity extends AppCompatActivity {

    Book mBook;
    BookDAO mDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_display);

        Intent intent = getIntent();
        long id = intent.getLongExtra(MainActivity.EXTRA_BOOKID, 0L);

        mDao = new BookDAO();
        mBook = mDao.getById(id);

        TextView text =(TextView) findViewById(R.id.book_display_title);
        text.setText(mBook.getTitle());

        setTitle(mBook.getTitle());
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
        }

        return super.onOptionsItemSelected(item);
    }
}
