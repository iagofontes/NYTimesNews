package br.com.fontes.thenynews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by root on 19/06/17.
 */

public class MovieReviewArrayAdapter extends ArrayAdapter<MovieReviews>{

    private static class ViewHolder{
        ImageView imgNews;
        TextView titleTextView;
        TextView publicationTextView;
//        TextView summaryTextView;
    }

    public MovieReviewArrayAdapter (Context context, List<MovieReviews> forecast){
        super (context, -1, forecast);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieReviews mvr = getItem (position);
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item, parent, false);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.titleMovie);
            viewHolder.publicationTextView = (TextView) convertView.findViewById(R.id.datePublic);
            viewHolder.imgNews =  (ImageView) convertView.findViewById(R.id.imageView);
//            viewHolder.summaryTextView =  (TextView) convertView.findViewById(R.id.descrMovie);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.titleTextView.setText(
                this.getContext().getResources().getString(R.string.filmDetail).toString() + mvr.getTitle());
        viewHolder.publicationTextView.setText(
                this.getContext().getResources().getString(R.string.dateDetail).toString() + mvr.getDate_publ());
//        viewHolder.publicationTextView.setText(
//                this.getContext().getResources().getString(R.string.summary).toString() + mvr.getSummary());
//        viewHolder.titleTextView.setText("Oieeeee");

        //baixar a imagem aqui
        if((mvr.getImgPath() != null) && (mvr.getImgPath()  != "")){
            DownloadImageTask dit = new DownloadImageTask(viewHolder.imgNews);
            dit.execute(mvr.getImgPath());
        }else{
            viewHolder.imgNews.setImageResource(R.mipmap.imgvideo);
        }
        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView imagem;

        public DownloadImageTask(ImageView img) {
            this.imagem = img;
        }

        protected Bitmap doInBackground(String... urls) {

            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            try{
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                try(InputStream inputStream = connection.getInputStream ()){
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally{
                connection.disconnect();
            }
            return bitmap;

        }

        protected void onPostExecute(Bitmap result) {
            imagem.setImageBitmap(result);
        }
    }
}
