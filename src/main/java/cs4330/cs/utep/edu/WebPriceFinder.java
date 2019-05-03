package cs4330.cs.utep.edu;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * Find price given url if valid
 *
 */
public class WebPriceFinder extends AsyncTask<String, Integer, Double> {
    private WeakReference<Context> contextRef;
    private double currentPrice;
    private String getProductName;
    private boolean isComplete;
    public AsyncResponse response;
    private ProgressBar bar;

    public interface AsyncResponse {
        void processFinish(double output);
    }

    public WebPriceFinder(Context context){
        contextRef = new WeakReference<>(context);

    }

    protected void onPreExecute() {
        super.onPreExecute();
        bar = new ProgressBar(contextRef.get());
        bar.setVisibility(View.VISIBLE);
    }

    /**
     * Connect to website parse page for region containing price
     *
     *
     * @param strings arguments url
     * @return price of product if valid
     */

    protected Double doInBackground(String... strings) {
        Document document;

        URL url;
        Element out = null;
        HttpURLConnection urlConnection = null;
        StringBuilder sb = null;
        String dollars;
        String cents;
        double price;

        int count = 0;


        try {
            url = new URL(strings[0]);

            urlConnection = (HttpURLConnection) url.openConnection();

            sb = new StringBuilder();
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();
            Document doc = Jsoup.parse(sb.toString());

            if(url.getHost().equals("www.homedepot.com")){
                try {
                    dollars = doc.selectFirst("span.price__dollars").text();
                    cents = doc.selectFirst("span.price__cents").text();
                    price = Double.parseDouble(dollars+"."+cents);
                    return price;
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            else if(url.getHost().equals("www.walmart.com")){
                try {
                    dollars = doc.selectFirst("span.price-group").text();
                    price = Double.parseDouble(dollars.substring(1));
                    return price;
                }
                catch(NullPointerException e){
                        e.printStackTrace();
                }

            }
            else if(url.getHost().equals("www.samsclub.com")){
                try {
                    dollars = doc.selectFirst("span.visuallyhidden:contains($)").text();
                    price = Double.parseDouble(dollars.substring(16));
                    return price;
                }
                catch(NullPointerException e){
                    e.printStackTrace();
                }

            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        return null;

    }


    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        bar.setProgress(values[0]);
    }

    /**
     *
     * Operation complete notify through response listenter
     *
     * @param price of product
     */

    @Override
    protected void onPostExecute(Double price) {
        bar.setVisibility(View.INVISIBLE);
        if(price == null) {
            currentPrice = -1;
        }
        else {
            currentPrice = price;

        }
        response.processFinish(currentPrice);

    }





}
