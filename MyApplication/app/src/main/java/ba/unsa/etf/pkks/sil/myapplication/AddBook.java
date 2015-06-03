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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ba.unsa.etf.pkks.sil.myapplication.Backand.Book;
import ba.unsa.etf.pkks.sil.myapplication.Backand.BookDAO;

public class AddBook extends AppCompatActivity {


    private static final String APIkey = "YPLJ3TGY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
    }

    public void searchBooks(View view) {
        EditText edit =(EditText) findViewById(R.id.edit_search_isbn);
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

    String on(Long isbn){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String url = String.format("http://isbndb.com/api/v2/json/%s/book/"+isbn.toString(), APIkey);
        Log.i("url", url);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode()==200){
                InputStream content = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return builder.toString();
    }


    private class ISBNDBService extends AsyncTask<Long, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            android.os.Debug.waitForDebugger();
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(AddBook.this);
            dialog.setMessage("Pretraživanje...");
            dialog.show();
        }



        @Override
        protected String doInBackground(Long... params) {
            android.os.Debug.waitForDebugger();
            if(params.length != 1)
                return null;

            Long isbn = params[0];

            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            String url = String.format("http://isbndb.com/api/v2/json/%s/book/"+isbn.toString(), APIkey);
            Log.i("url", url);
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode()==200){
                    InputStream content = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }

            return builder.toString();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            android.os.Debug.waitForDebugger();
            dialog.dismiss();
            EditText edit =(EditText) findViewById(R.id.edit_search_isbn);

            if (result!=null){
                edit.setText(result);
            } else {
                edit.setText("Null je");
            }
        }
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

            EditText edit = (EditText) findViewById(R.id.add_book_show);
            BookDAO dao = new BookDAO();
            try {
                Book foundBook = JsonToBook(json);
                dao.addBook(foundBook);
                Toast.makeText(AddBook.this, R.string.book_found, Toast.LENGTH_LONG);

            }catch (JSONException e){
                Toast.makeText(AddBook.this, e.getMessage(), Toast.LENGTH_LONG);
            }

        }

        Book JsonToBook(String json) throws JSONException {

            JSONObject object = new JSONObject(json);
            JSONObject data = object.getJSONObject("data");

            Book b = new Book();
            b.setTitle(data.getString("title"));

            if(data.has("physical_description_text")) {
                String pages = data.getString("physical_description_text");
                Pattern p = Pattern.compile(".*((\\d+)[ ]p\\.).*");
                Matcher m = p.matcher(pages);
                if(m.matches()) {
                    String pagesStr = m.group(2);
                    b.setPagesNumber(Integer.parseInt(pagesStr));
                }
            }

            b.setIsbn(""+data.getLong("isbn13"));
            if(data.has("summary"))
                b.setDescription(data.getString("summary"));

            if(data.has("author_data" )){
                JSONArray authors = data.getJSONArray("author_data" );
                b.setAuthor(authors.getJSONObject(0).getString("name"));
            }
            return  b;
        }
    }
}
