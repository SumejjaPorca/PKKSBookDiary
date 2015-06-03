package ba.unsa.etf.pkks.sil.myapplication;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
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

        mDao = new BookDAO(this);
        mDao.open();
        mBook = mDao.getById((int)id);

        TextView text =(TextView) findViewById(R.id.book_display_title);
        text.setText(mBook.getTitle());
        TextView author =(TextView) findViewById(R.id.book_display_author);
        author.setText(mBook.getAuthor());
        TextView desc =(TextView) findViewById(R.id.book_display_desc);
        desc.setText(mBook.getDescription());

        setTitle(mBook.getTitle());

        RadioButton radio0 = (RadioButton) findViewById(R.id.radio_notRead);
        RadioButton radio1 = (RadioButton) findViewById(R.id.radio_reading);
        RadioButton radio2 = (RadioButton) findViewById(R.id.radio_read);

        switch(mBook.getStatus()){
            case 0:
                radio0.setChecked(true);
                radio1.setChecked(false);
                radio2.setChecked(false);
                break;
            case 1:
                radio0.setChecked(false);
                radio1.setChecked(true);
                radio2.setChecked(false);
                break;
            case 2:
                radio0.setChecked(false);
                radio1.setChecked(false);
                radio2.setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_book) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view){
        if(view == null)
            return;
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_notRead:
                if (checked)
                    mBook.setStatus(0);
                    mDao.updateStatus(mBook);
                    break;
            case R.id.radio_reading:
                if (checked)
                    mBook.setStatus(1);
                mDao.updateStatus(mBook);
                    break;
            case R.id.radio_read:
                if (checked)
                    mBook.setStatus(2);
                mDao.updateStatus(mBook);
                    break;
        }
        if(mDao.getById(mBook.getId()) != null)
        mBook = mDao.getById(mBook.getId());

    }


    @Override
    public void onResume() {
        super.onResume();
        mDao.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDao.close();
    }
}
