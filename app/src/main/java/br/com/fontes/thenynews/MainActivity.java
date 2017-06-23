package br.com.fontes.thenynews;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<MovieReviews> reviews = new ArrayList<>();
    public ListView movieReviewListView;
    public MovieReviewArrayAdapter movieReviewArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        movieReviewListView = (ListView) this.findViewById(R.id.movieReviewList);
        movieReviewArrayAdapter = new MovieReviewArrayAdapter(this.getApplicationContext(), reviews);
        movieReviewListView.setAdapter(movieReviewArrayAdapter);


        movieReviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView txtTitulo = (TextView) view.findViewById(R.id.titleMovie);

                int i=0, achou=0;
                String titulo = txtTitulo.getText().toString();
                String movie="";
                while(i<reviews.size()){
//                while(!titulo.replaceAll(" ", "").equalsIgnoreCase(reviews.get(i).getTitle().replaceAll(" ", ""))){
                    movie = titulo.replaceAll(getResources().getString(R.string.filmDetail).toString(), "")
                            .replaceAll(" ", "");
                    if(movie.equalsIgnoreCase(reviews.get(i).getTitle().replaceAll(" ", ""))){
                        achou = i;
                        break;
                    }
                    i++;
                }
//                if(titulo.replaceAll(" ", "").equalsIgnoreCase(reviews.get(i).getTitle().replaceAll(" ", ""))){
                if(movie.replaceAll(" ", "").equalsIgnoreCase(reviews.get(achou).getTitle().replaceAll(" ", ""))){

                    MovieReviews mr = reviews.get(achou);

                    Intent in = new Intent(view.getContext(), DetailsActivity.class);
                    Bundle params = new Bundle();
                    params.putString("titulo", mr.getTitle());
                    params.putString("data", mr.getDate_publ());
                    params.putString("imgSrc", mr.getImgPath());
                    params.putString("summary", mr.getSummary());
                    in.putExtras(params);
                    startActivityForResult(in, RESULT_OK);
//                    startActivity(in);
                }

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Realizar a pesquisa por aqui.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                reviews.clear();
                movieReviewArrayAdapter.notifyDataSetChanged();
                movieReviewListView.smoothScrollToPosition(0);

                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                URL url = null;
                try{
//                    url = new URL("https://api.nytimes.com/svc/movies/v2/reviews/search.json?api_key=476fa32b122d474595bb695e05484c59&query=cars 3");
//                    url = new URL("https://api.nytimes.com/svc/movies/v2/reviews/search.json?api_key=476fa32b122d474595bb695e05484c59&query=cars%203");
                    Context con = getApplicationContext();
                    EditText filmField = (EditText) findViewById(R.id.txtMessage);
//                    EditText filmField = (EditText) view.findViewById(R.id.txtMessage);
                    String film = manageFilmName(filmField.getText().toString());
                    String endereco = getResources().getString(R.string.endReq).toString();
                    String chave = getResources().getString(R.string.chaveApi).toString();
                    String uri = String.format(endereco, chave);
                    uri += "&"+String.format(getResources().getString(R.string.queryApi).toString(), film);
//                    uri += "&"+String.format(getResources().getString(R.string.queryApi).toString(), "fastandfurious");
                    url = new URL(uri);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                if (url != null){
//                    GetMoviesReview getReviews = new GetMoviesReview();
                    GetMoviesReview getReviews = new GetMoviesReview(view);
                    getReviews.execute(url);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Cannot create a URL.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public String manageFilmName(String filme){
        String nome = "";

        nome = filme.toLowerCase().replaceAll(" ", "%20");

        return "%27"+nome+"%27";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetMoviesReview extends AsyncTask<URL, Void, JSONObject> {
        View view;
        public GetMoviesReview(View v){
            this.view = v;
        }
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK){
                    StringBuilder builder = new StringBuilder ();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))){
                        String line;
                        while ((line = reader.readLine()) != null){
                            builder.append(line);
                        }
                    }
                    catch (IOException e){
                        Toast.makeText(getApplicationContext(), "Errors while trying to get the donates.", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    return new JSONObject(builder.toString());
                }
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "Errors while trying to connect with server of donates.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally{
                if (connection != null){
                    connection.disconnect();
                }
            }
            return null;
        }

        private void convertJSONToArrayList (JSONObject forecast){
            reviews.clear();
            try{
//                JSONObject lineResults = forecast.getJSONObject("");
//                if(Integer.parseInt(lineResults.getString("num_results")) > 0){
                Integer lineResult = Integer.parseInt(forecast.getString("num_results"));
                if(lineResult > 0){
                    JSONArray list = forecast.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++){
                        JSONObject line = list.getJSONObject(i);
                        if(!line.isNull("multimedia")){
//                        if(line.getJSONObject("multimedia") != null){
                            JSONObject mult = line.getJSONObject("multimedia");
                            String path = "";
                            path = mult.getString("src");
                            reviews.add(new MovieReviews(null, line.optString("display_title"),
                                    tratarData(line.optString("publication_date")), line.optString("summary_short"), path));
                        }else{
                            Snackbar.make(this.view, getResources().getString(R.string.noImgSrc), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            reviews.add(new MovieReviews(null, line.optString("display_title"),
                                    tratarData(line.optString("publication_date")), line.optString("summary_short")));
                        }
                    }
                }else{
                    Snackbar.make(this.view, getResources().getString(R.string.noItem), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }

        protected void onPostExecute(JSONObject don) {
            Log.d("Reviews", don.toString());
            convertJSONToArrayList(don);
            movieReviewArrayAdapter.notifyDataSetChanged();
            movieReviewListView.smoothScrollToPosition(0);

        }

        public String tratarData(String date){

            String ano = date.substring(0, 4);
            String mes = date.substring(5, 7);
            String dia = date.substring(8, 10);
            return dia+"/"+mes+"/"+ano;
        }
    }

}
