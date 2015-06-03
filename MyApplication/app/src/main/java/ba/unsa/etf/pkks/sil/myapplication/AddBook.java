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

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        Long isbn = Long.getLong(text);
        Log.i("searcBooks()", text);

        ISBNDBService ser = new ISBNDBService();
        ser.execute(isbn);
    }

    private class ISBNDBService extends AsyncTask<Long, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(AddBook.this);
            dialog.setMessage("Pretraživanje...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Long... params) {

            if(params.length != 1)
                return null;

            Long isbn = params[0];

            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            String url = String.format("http://isbndb.com/api/v2/json/%s/book/"+isbn, APIkey);
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
            dialog.dismiss();
            EditText edit =(EditText) findViewById(R.id.edit_search_isbn);

            if (result!=null){
                edit.setText(result);
            } else {
                edit.setText("Null je");
            }
        }

    }
}
