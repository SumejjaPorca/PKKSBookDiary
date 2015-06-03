package ba.unsa.etf.pkks.sil.myapplication.Backand;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luka454 on 3.6.2015.
 */
public class BookDAO
{
    static ArrayList<Book> mBooks;

    public BookDAO(){

        if(mBooks == null) {
            mBooks = new ArrayList<>();

            Book b1 = new Book();
            b1.setId(1);
            b1.setTitle("Orlovi rano lete");
            b1.setAuthor("Branko Ćopić");
            b1.setIsbn("ISBN 978-86-01-01688-1");
            b1.setDescription("Dječja knjiga.");
            b1.setPublishDate(new Date());
            b1.setPagesNumber(190);

            mBooks.add(b1);


            Book b2 = new Book();
            b2.setId(2);
            b2.setTitle("Parfem");
            b2.setAuthor("Branko Ćopić");
            b2.setIsbn("ISBN 978-86-01-01688-2");
            b2.setDescription("Knjiga istaknutog fracuskog književnika.");
            b2.setPublishDate(new Date());
            b2.setPagesNumber(190);
            b2.setStatus(1);

            mBooks.add(b2);
        }
    }

    public Book addBook(Book book){
        if(mBooks.isEmpty())
            book.setId(1);
        else{
            long idMax = mBooks.get(0).getId();
            for(Book b : mBooks){
                if(b.getId() > idMax)
                    idMax = b.getId();
            }

            book.setId(idMax+1);
        }

        mBooks.add(book);
        return book;
    }

    public Book update(Book book){
        for(Book b : mBooks)
            if(b == book)
                return  book;

        return null;
    }

    public int getCount(){
        return mBooks.size();
    }

    public Book getById(long id){
        for (Book b : mBooks) {
            if(b.getId() == id)
                return b;
        }
        return  null;
    }

    public Book getByISBN(String isbn){
        for (Book b : mBooks) {
            if(b.getIsbn() == isbn)
                return b;
        }
        return  null;
    }

    public List<Book> getAllBooks(){
        return mBooks;
    }

    public List<Book> getAllByTitle(String title){
        ArrayList<Book> result = new ArrayList<>();
        for (Book b : mBooks) {
            if(b.getTitle().toLowerCase().contains(title.toLowerCase()))
                result.add(b);
        }
        return result;
    }
}
