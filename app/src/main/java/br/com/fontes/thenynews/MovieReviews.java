package br.com.fontes.thenynews;

import android.graphics.Bitmap;

/**
 * Created by root on 19/06/17.
 */

public class MovieReviews {

    Bitmap img;
    String title;
    String date_publ;
    String imgPath;
    String summary;

    public MovieReviews(Bitmap img, String title, String date_publ, String Summary, String imgPath){
        setImg(img);
        setTitle(title);
        setDate_publ(date_publ);
        setSummary(Summary);
        setImgpath(imgPath);
    }

    public MovieReviews(Bitmap img, String title, String date_publ, String Summary){
        setImg(img);
        setTitle(title);
        setDate_publ(date_publ);
        setSummary(Summary);
        setImgpath(null);
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_publ() {
        return date_publ;
    }

    public void setDate_publ(String date_publ) {
        this.date_publ = date_publ;
    }

    public String getImgPath(){
        return this.imgPath;
    }

    public void setImgpath(String imgPath){
        this.imgPath = imgPath;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String text) {
        this.summary = text;
    }


}
