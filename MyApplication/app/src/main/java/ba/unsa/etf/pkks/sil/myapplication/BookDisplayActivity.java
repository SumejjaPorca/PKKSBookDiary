package ba.unsa.etf.pkks.sil.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import ba.unsa.etf.pkks.sil.myapplication.Backand.Book;
import ba.unsa.etf.pkks.sil.myapplication.Backand.BookDAO;

public class BookDisplayActivity extends AppCompatActivity {

    Book mBook;
    BookDAO mDao;
    private static int RESULT_LOAD_IMG = 1;

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
        TextView date =(TextView) findViewById(R.id.book_display_date);
        date.setText(prettyDate(mBook.getPublishDate()));

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

        showImage(mBook.getPhotoLink());
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

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String imgDecodableString;
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                mBook.setPhotoLink(imgDecodableString);
                mDao.updatePhoto(mBook);

                showImage(imgDecodableString);

            } else {
                Toast.makeText(this,  getResources().getString(R.string.img_not_picked),
                Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void showImage(String imgDecodableString){

        ImageView imgView = (ImageView) findViewById(R.id.imgView);
        // Set the Image in ImageView after decoding the String
        imgView.setImageBitmap(BitmapFactory
                .decodeFile(imgDecodableString));
        return;
    }

    private String prettyDate(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        return sdf.format(date);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDao.close();
    }
}
