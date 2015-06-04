package ba.unsa.etf.pkks.sil.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ba.unsa.etf.pkks.sil.myapplication.Backand.Book;
import ba.unsa.etf.pkks.sil.myapplication.Backand.BookDAO;
import ba.unsa.etf.pkks.sil.myapplication.Scanner.IntentIntegrator;
import ba.unsa.etf.pkks.sil.myapplication.Scanner.IntentResult;

public class AddBook extends AppCompatActivity {

    public final static  String EXTRA_BOOKID = "ba.unsa.etf.pkks.sil.myapplication.AppCompatActivity.BOOKID";

    private static final String APIkey = "YPLJ3TGY";

    BookDAO mBookDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mBookDao = new BookDAO(getBaseContext());


    }

    public void onScanClick(View view){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();

    }

    @Override
    public void onResume() {
        super.onResume();
        mBookDao.open();

    }

    @Override
    public void onPause() {
        super.onPause();
        mBookDao.close();
    }

    public void searchBooks(View view) {
        EditText edit =(EditText) findViewById(R.id.add_book_isbn);
        String text = edit.getText().toString();
        text = text.replace("-","").trim();
        Long isbn = Long.parseLong(text);
        Log.i("searcBooks()", text);

        //ISBNDBService ser = new ISBNDBService();
        //ser.execute(isbn, null);

        String url = String.format("http://isbndb.com/api/v2/json/%s/book/"+isbn.toString(), APIkey);
        Task t= new Task(url);
        t.execute();

    }



    public class Task extends AsyncTask<Void, Void, String> {
        private String url;

        public Task(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();

            // Prepare a request object
            HttpGet httpGet = new HttpGet(url);

            try {
                HttpResponse httpResponse = httpclient.execute(httpGet);
                if (httpResponse != null) {
                    StatusLine status = httpResponse.getStatusLine();
                    if (status.getStatusCode()==200){

                        InputStream content = httpResponse.getEntity().getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                        String line;
                        StringBuilder builder = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        android.os.Debug.waitForDebugger();
                        String json = builder.toString();
                        if(json == null)
                            return null;
                        //Moramo parsiranje uraditi
                        return json;
                    }

                }

                return null;
            } catch (ClientProtocolException e) {
                // writing exception to log

                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // writing exception to log
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            android.os.Debug.waitForDebugger();

            //EditText edit = (EditText) findViewById(R.id.add_book_show);

            try {
                JSONObject temp = new JSONObject(json);
                if( temp.has("error")) {
                    Toast.makeText(AddBook.this, R.string.book_not_found, Toast.LENGTH_LONG).show();
                    return;
                }
                Book foundBook = JsonToBook(json);
                Toast.makeText(AddBook.this, R.string.book_found, Toast.LENGTH_LONG).show();

                showBook(foundBook);
                /*Intent intent = new Intent(AddBook.this, BookDisplayActivity.class);
                intent.putExtra(EXTRA_BOOKID, id);
                startActivity(intent);*/

            }catch (JSONException e){
                Toast.makeText(AddBook.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

        Book JsonToBook(String json) throws JSONException {

            JSONObject object = new JSONObject(json);
            boolean bul = object.has("data");
            JSONObject data = object.getJSONArray("data").getJSONObject(0);

            Book b = new Book();
            if(data.has("title"))
                b.setTitle(data.getString("title"));

            if(data.has("physical_description_text")) {
                String pages = data.getString("physical_description_text");
                Pattern p = Pattern.compile("^.*((\\d+)[ ]p\\.).*$");
                Matcher m = p.matcher(pages);
                if(m.matches()) {
                    String pagesStr = m.group(2);
                    b.setPagesNumber(Integer.parseInt(pagesStr));
                }
            }

            if(data.has("isbn13"))
                b.setIsbn(""+data.getLong("isbn13"));

            if(data.has("summary"))
                b.setDescription(data.getString("summary"));

            if(data.has("author_data" )){
                JSONArray authors = data.getJSONArray("author_data" );
                if(authors.length() > 0)
                    b.setAuthor(authors.getJSONObject(0).getString("name"));
            }
            return  b;
        }
    }

    private void showBook(Book foundBook) {
        EditText title = (EditText) findViewById(R.id.add_book_title);
        title.setText(foundBook.getTitle() == null ? "" : foundBook.getTitle());

        EditText author = (EditText) findViewById(R.id.add_book_author);
        author.setText(foundBook.getAuthor() == null ? "" :  foundBook.getAuthor());

        EditText isbn = (EditText) findViewById(R.id.add_book_isbn);
        isbn.setText(foundBook.getIsbn() == null ? "" : foundBook.getIsbn());

        EditText pages = (EditText) findViewById(R.id.add_book_pages);
        pages.setText(""+foundBook.getPagesNumber());

        DatePicker dp = (DatePicker)findViewById(R.id.add_book_publish_date);
        if(foundBook.getPublishDate() != null)
            dp.updateDate(foundBook.getPublishDate().getYear(), foundBook.getPublishDate().getMonth(),
                foundBook.getPublishDate().getDay());

        EditText desc = (EditText) findViewById(R.id.add_book_description);
        desc.setText(foundBook.getDescription() == null ? "" : foundBook.getDescription());
    }

    public void saveBook(View view){
        Book newBook = new Book();

        EditText title = (EditText) findViewById(R.id.add_book_title);
        newBook.setTitle(title.getText().toString());

        EditText author = (EditText) findViewById(R.id.add_book_author);
        newBook.setAuthor(author.getText().toString());

        EditText isbn = (EditText) findViewById(R.id.add_book_isbn);
        newBook.setIsbn(isbn.getText().toString());

        EditText pages = (EditText) findViewById(R.id.add_book_pages);
        try{
            newBook.setPagesNumber(Integer.parseInt(pages.getText().toString()));
        }catch (Exception e){
            newBook.setPagesNumber(0);
        }

        DatePicker dp = (DatePicker)findViewById(R.id.add_book_publish_date);
        newBook.setPublishDate(new Date(dp.getYear(), dp.getMonth(), dp.getDayOfMonth()));

        EditText desc = (EditText) findViewById(R.id.add_book_description);
        newBook.setDescription(desc.getText().toString());

        long id = mBookDao.addBook(newBook);
        Intent intent = new Intent(this, BookDisplayActivity.class);
        intent.putExtra(MainActivity.EXTRA_BOOKID, id);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            EditText contentTxt = (EditText)findViewById(R.id.add_book_isbn);
            contentTxt.setText(scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
