package br.com.fontes.thenynews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static br.com.fontes.thenynews.R.layout.activity_details;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_details);

        //habilita o botão para voltar a activity anterior
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView imgMovieField = (ImageView) this.findViewById(R.id.imgMovie);
        TextView txtTituloField = (TextView) this.findViewById(R.id.movieName);
        TextView txtDateField = (TextView) this.findViewById(R.id.movieDatePublic);
        TextView txtSummary = (TextView) this.findViewById(R.id.descrMovie);

        Intent intent = getIntent();
        Bundle param = intent.getExtras();

        if(param != null){

            String ano = param.getString("data").substring(0, 4);
            String mes = param.getString("data").substring(5, 7);
            String dia = param.getString("data").substring(8, 10);

            txtTituloField.setText(this.getResources().getString(R.string.filmDetail).toString() + param.getString("titulo"));
            txtDateField.setText(this.getResources().getString(R.string.dateDetail).toString() + param.getString("data"));
            txtSummary.setText(this.getResources().getString(R.string.summary).toString() + param.getString("summary"));
            String src = param.getString("imgSrc");
            if((src != "") && (src != null)){
                DownloadImageTask dit = new DownloadImageTask(imgMovieField);
                dit.execute(src);
                imgMovieField.setContentDescription(param.getString("titulo"));

            }
        }
    }
    public boolean onOptionsItemSelected(MenuItem item){
        setResult(RESULT_OK);
        finish();
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return true;
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
