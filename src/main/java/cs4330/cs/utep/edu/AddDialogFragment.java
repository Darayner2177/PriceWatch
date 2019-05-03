package cs4330.cs.utep.edu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
/*
* Custom add dialog fragment creates a dialog fragment
* that allow a user to enter product information
*
*/

public class AddDialogFragment extends DialogFragment implements WebPriceFinder.AsyncResponse {

    private EditText productNameEditText;
    private EditText productUrlEditText;
    private Listener listener;
    private String url;
    double currentPrice;
    private View view;
    private ProgressBar progress;
    Context context;

    /*
    * Interface Listener to be implemented in main activity.
    * The dialog fields are set in the onCreateDialog
    * Main activity can now access the fields
    *
    */
    public interface Listener{
        void addProduct(String name, String url, double price);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        listener = (Listener) context;
    }

    /**
     * Create a custom dialog. Inflate custom add xml dialog menu
     * set dialog buttons add and cancel. override onclick listeners for buttons
     * Get name, url, and price fields. Add to listener method.
     *
     * @param savedInstanceState Bundle saved state
     * @return custom built dialog menu
     */
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder mBuild = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_add, null);

        productNameEditText = view.findViewById(R.id.ProductNameEditText);
        productUrlEditText = view.findViewById(R.id.ProductUrlEditText);

        productUrlEditText.setText(url);

        mBuild .setView(view)
               .setPositiveButton("ADD", (dialog, id) -> {
                  if(!productNameEditText.getText().toString().isEmpty() && !productUrlEditText.getText().toString().isEmpty()){
                      boolean check =  URLUtil.isValidUrl(productUrlEditText.getText().toString());
                      System.out.println(check);
                      if(check != false){
                          WebPriceFinder syncTask = new WebPriceFinder(getActivity());
                          syncTask.response = this;
                          String url = productUrlEditText.getText().toString();
                          syncTask.execute(url);
                        }
                      }
                  else{
                      Toast.makeText(context, "Fields Missing", Toast.LENGTH_SHORT).show();
                  }
               })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return mBuild.create();
    }

    /**
    * Url is set, this is used to get shared intent url set in main activity
    *
    * @param url url to set
    */

    public void setUrl(String url){
        this.url = url;
    }

    public void processFinish(double output) {
        currentPrice = output;
        if(currentPrice != -1) {
            listener.addProduct(productNameEditText.getText().toString(), productUrlEditText.getText().toString(), currentPrice);
        }
        else{
           Toast.makeText(context, "Could not find price of Product", Toast.LENGTH_SHORT).show();
        }
    }



}
