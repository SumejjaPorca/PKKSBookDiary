package ba.unsa.etf.pkks.sil.myapplication.Backand;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ba.unsa.etf.pkks.sil.myapplication.R;

/**
 * Created by luka454 on 2.6.2015.
 */
public class BooksAdapter extends BaseAdapter {

    private List<Book> mBooks;
    private LayoutInflater mLayoutInflater = null;
    private Activity mContext;
    public BooksAdapter(List<Book> books, Activity context){
        this.mBooks = books;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if(v == null){
            v = mLayoutInflater.inflate(R.layout.book_item, viewGroup, false);
        }

        Book book = mBooks.get(i);

        TextView title = (TextView) v.findViewById(R.id.book_item_title);
        title.setText(book.getTitle());

        TextView status = (TextView) v.findViewById(R.id.book_item_status);
        status.setText(book.getStatusName(mContext.getResources()));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return v;
    }
}
