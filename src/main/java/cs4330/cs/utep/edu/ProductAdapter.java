package cs4330.cs.utep.edu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/*
* Custom Product Adapter extends array adapter
* Allows custom layout to be inflated
* Connects a Product Arraylist to ProductAdapter
*
*/

public class ProductAdapter extends ArrayAdapter<Product> {

    private static List<Product> products;

    /**
     * Constructor: create product adapter
     *
     * @param context context of application
     * @param products list of products to create adapter
     */
    public ProductAdapter(Context context, List<Product> products){
        super(context, -1, products);
        this.products = products;

    }

    /**
     * Get the product at the position
     *
     * @param position of product
     * @return return the Product at the given position
     */
    public Product getItem(int position) {
       return super.getItem(position);
    }


    /**
     * Inflates the custom_layout xml to be used to set views based on the relative
     * position within the adapter
     *
     * @param position product position in adapter
     * @param convertView view to inflate custom_layout xml
     * @param parent view group of layout
     * @return the view at product position
     */
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView != null ? convertView : LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false);
        Product productPos = products.get(position);

        TextView price = v.findViewById(R.id.priceTextView);
        TextView percent = v.findViewById(R.id.percentTextView);
        TextView initialPrice = v.findViewById(R.id.initialPriceTextView);
        TextView productName = v.findViewById(R.id.itemNameTextView);
        ImageView searchUrlImageView = v.findViewById(R.id.searchUrlImageView);

        String currPrice = "$" + String.valueOf(productPos.getCurrentPrice());
        String perChange = String.valueOf(productPos.getPercentChange())+ "%";
        String initPrice = "$" + String.valueOf(productPos.getInitialPrice());

        if(productPos.getPercentChange() > 0){
            percent.setTextColor(getContext().getResources().getColor(R.color.green));
        }
        else{
            percent.setTextColor(getContext().getResources().getColor(R.color.red));
        }


        searchUrlImageView.setOnClickListener((View view) -> searchProduct(productPos.getTrackUrl()));

        productName.setText(productPos.getName());
        initialPrice.setText(initPrice);
        price.setText(currPrice);
        percent.setText(perChange);

        return v;

    }


    /**
     * Search product allows the image view button to send an intent to view the
     * webpage of the url
     *
     * @param url a url to set
     */
    public void searchProduct(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        if(intent.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(intent);
        }
    }


}
