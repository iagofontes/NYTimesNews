package br.com.fontes.thenynews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Context ct = this.getApplicationContext();

        ImageView imgMovieField = (ImageView) this.findViewById(R.id.imgMovie);
        TextView txtTituloField = (TextView) this.findViewById(R.id.movieName);
        TextView txtDateField = (TextView) this.findViewById(R.id.movieDatePublic);

        Intent intent = getIntent();
        Bundle param = intent.getExtras();

        if(param != null){
//            Format format = new SimpleDateFormat("dd/MM/yyyy");
//            String date = format.format(param.getString("data"));
            String ano = param.getString("data").substring(0, 4);
            String mes = param.getString("data").substring(5, 7);
            String dia = param.getString("data").substring(8, 10);



            txtTituloField.setText(param.getString("titulo"));
//            txtDateField.setText(dia+"/"+mes+"/"+ano);
//            txtDateField.setText(dateFormat.format(date));
            txtDateField.setText(param.getString("data"));
            String src = param.getString("imgSrc");
            if((src != "") && (src != null)){
                DownloadImageTask dit = new DownloadImageTask(imgMovieField);
                dit.execute(src);
            }
        }
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
