package com.example.countrieswithhandlerexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView rank,population,country;
    Button previous,next;
    ImageView countryImage;
    List<Country> myCountryList;
    Handler handler;
    JsonHandler jsonHandler;

    int counter=0;

    public MainActivity() {
        myCountryList=new ArrayList<Country>();
        jsonHandler = new JsonHandler();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rank=findViewById(R.id.countrynumTxt);
        population=findViewById(R.id.pepoleTxt);
        country=findViewById(R.id.countrynameTxt);
        previous=findViewById(R.id.perviousBtn);
        next=findViewById(R.id.nextBtn);
        countryImage=findViewById(R.id.flagimageView);
        myCountryList=jsonHandler.getCountriesList();

        handler=new Handler(){


            @Override
            public void handleMessage(Message msg) {
                rank.setText(myCountryList.get(counter).getRank());
                country.setText(myCountryList.get(counter).getCountry());
                population.setText(myCountryList.get(counter).getPopulation());
                GetCountries getCountries= new GetCountries();
                try {
                    getCountries.execute(new URL(myCountryList.get(counter).getFlag()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        };
        Thread updater= new Thread(jsonHandler);
        updater.start();



        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                counter++;
                if(counter==10) {
                    counter = 0;
                }
                rank.setText(myCountryList.get(counter).getRank());
                country.setText(myCountryList.get(counter).getCountry());
                population.setText(myCountryList.get(counter).getPopulation());

                GetCountries getCountries= new GetCountries();
                try {
                    getCountries.execute(new URL(myCountryList.get(counter).getFlag()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                if (counter == -1) {
                    counter = 9;
                }
                rank.setText(myCountryList.get(counter).getRank());
                country.setText(myCountryList.get(counter).getCountry());
                population.setText(myCountryList.get(counter).getPopulation());
                GetCountries getCountries= new GetCountries();
                try {
                    getCountries.execute(new URL(myCountryList.get(counter).getFlag()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

        });

    }
    class JsonHandler implements Runnable {
        private String url = "https://www.androidbegin.com/tutorial/jsonparsetutorial.txt";
        private Country countryObj;
        private HttpHandler httpHandler;
        private List<Country> countriesList;

        public JsonHandler() {

            httpHandler = new HttpHandler();
            countriesList = new ArrayList<Country>();

        }
        public List<Country> getCountriesList() {
            return countriesList;
        }

        @Override
        public void run() {
            GetCountries getCountries=new GetCountries();
            URL imageUrl;
            String jsonStr = httpHandler.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray countriesArr = jsonObj.getJSONArray("worldpopulation");

                    // looping through All Contacts
                    for (int i = 0; i < countriesArr.length(); i++) {
                        JSONObject c = countriesArr.getJSONObject(i);
                        String rank = c.getString("rank");
                        String country = c.getString("country");
                        String population = c.getString("population");
                        String flag = c.getString("flag");
                        countryObj = new Country(rank, country, population, flag);
                        countriesList.add(countryObj);
                    }
                    handler.sendEmptyMessage(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class GetCountries extends AsyncTask<URL, Void, Bitmap> {
        Bitmap bitmap = null;


        @Override
        protected Bitmap doInBackground(URL... urls) {
            try {
                bitmap = downloadImage(urls[0]);
                Log.d("Exception in download", urls[0].toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // super.onPostExecute(bitmap);
            countryImage.setImageBitmap(bitmap);


        }

        private Bitmap downloadImage(URL strUrl) throws IOException {
            Bitmap bitmap = null;
            InputStream iStream = null;
            try {
                URL url =strUrl;
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.connect();
                iStream = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(iStream);
                Log.d("Exception in download", strUrl.toString());

            } catch (Exception e) {
                Log.d("Exception in download", e.toString());
            } finally {
                iStream.close();
            }
            return bitmap;
        }

    }
}
