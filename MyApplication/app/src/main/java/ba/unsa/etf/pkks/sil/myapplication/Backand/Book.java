package ba.unsa.etf.pkks.sil.myapplication.Backand;

import android.content.res.Resources;
import android.widget.DatePicker;

import java.util.Date;

import ba.unsa.etf.pkks.sil.myapplication.R;

/**
 * Created by luka454 on 2.6.2015.
 */
public class Book {

    public long id;
    public String title;
    public String author;
    public String isbn;
    public Date publishDate;
    public int pagesNumber;
    public int status;
    public String description;
    public String photoLink;

    public Book(){

    }

    //id, title, description, author, d, pages, status, isbn, photo
    public Book(int id, String title, String description, String author, Date d,int pages, int status, String isbn,String photo){
        setStatus(status);
        setAuthor(author);
        setDescription(description);
        setId(id);
        setIsbn(isbn);
        setPagesNumber(pages);
        setPhotoLink(photo);
        setPublishDate(d);
        setTitle(title);

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String link) {
        this.photoLink = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusName(Resources res){
        switch (status){
            case 0:
                return res.getString(R.string.notRead);
            case 1:
                return res.getString(R.string.reading);
            case 2:
                return  res.getString(R.string.read);
            default:
                return res.getString(R.string.invalid);
        }
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
