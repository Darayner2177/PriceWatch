package cs4330.cs.utep.edu;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
* David Rayner
*
* HW#3
*
* Program to add products using dialog Fragments to add and edit.
* A context menu to allow user selection as well as an options menu.
* Can view webpage of product url.
* Checks network status if connected or not.
* Finds price of watched item.
* Three stores supported HomeDepot, Sams, and Walmart
*
*/
public class MainActivity extends AppCompatActivity implements AddDialogFragment.Listener, EditDialogFragment.Listener, CheckNetworkStatus.CheckConnection{

    private ListView productListView;
    private static List<Product> myProducts;
    private ProductManage manage;
    private ProductAdapter productAdapt;
    private Product product;
    private String url;
    private ProductManageDatabase productManageDatabase;
    private CheckNetworkStatus checkNetworkStatus;

    /**
    * Check to see if saved state is null if so restore product list
    * Otherwise, create a new product list by Product manage class
    * set the list view, and create a new Product adapter
    * set the adapter on the listview
    * Register a context menu for later use
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list_view);
        productManageDatabase = new ProductManageDatabase(this);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkNetworkStatus = new CheckNetworkStatus();
        checkNetworkStatus.checkConnection = this;
        this.registerReceiver(checkNetworkStatus, filter);

        myProducts = productManageDatabase.getProductList();
        productListView = findViewById(R.id.productListView);
        productAdapt = new ProductAdapter(this, myProducts);
        productListView.setAdapter(productAdapt);
        registerForContextMenu(productListView);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo){
        super.onCreateContextMenu(menu, view, contextMenuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    /**
     * Get position of selected product through Adapter view menuInfo
     * determine what view was clicked
     * Edit create edit dialog set the position of clicked view, show dialog
     * Delete get position and remove from product list
     * Notify product adapter of changes
     *
     * @param menuItem
     * @return super class context menu value
     */
    public boolean onContextItemSelected(MenuItem menuItem){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        int pos = menuInfo.position;

        switch(menuItem.getItemId()){
            case R.id.Edit:
                EditDialogFragment editDialog = new EditDialogFragment();
                editDialog.setPosition(pos);
                editDialog.show(getSupportFragmentManager(), "edit Dialog");
                return true;
            case R.id.Delete:
                product = productAdapt.getItem(pos);
                productManageDatabase.removeProduct(product);
                productAdapt.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(menuItem);
    }

    /**
     * Inflate product menu to be viewed
     *
     * @param menu options menu to inflate
     * @return boolean if created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return true;

    }

    /**
     * Options menu
     * If item selected
     * Refresh update product prices. notify product adapter
     * Add product create Add Dialog Fragment check if intent was shared,
     * set url with intent sent
     *
     * @param item that was clicked
     * @return super class option menu value
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                    productAdapt.notifyDataSetChanged();
                    productManageDatabase.updateRefresh(this);
                return true;

            case R.id.addProduct:
                AddDialogFragment addDialog = new AddDialogFragment();
                addDialog.show(getSupportFragmentManager(), "add Dialog");

                if(getIntent() != null) {
                    String action = getIntent().getAction();
                    String type = getIntent().getType();

                    if (Intent.ACTION_SEND.equalsIgnoreCase(action) && (type != null && type.equals("text/plain"))) {
                        url = getIntent().getStringExtra(Intent.EXTRA_TEXT);
                        addDialog.setUrl(url);
                    }

                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Must be implemented via listener in EditDialogFragment class
     * Allows dialog values to be used
     * Rename and change product url at position
     * notify product adapter
     *
     * @param name new name of product
     * @param url new url of product
     * @param position position of product to change
     */
    @Override
    public void editProduct(String name, String url, int position, double price){
        product = productAdapt.getItem(position);
        productAdapt.notifyDataSetChanged();
        productManageDatabase.updateEdit(product, name, url, price);
    }

    /**
     * Must be implemented via listener in AddDialogFragment class
     * Allows dialog values to be used
     * Add products to product list
     * notify product adapter
     *
     * @param name name of product to add
     * @param url url of product to add
     * @param price initial price of product to add
     */
    @Override
    public void addProduct(String name, String url, double price) {
        product = new Product(name, price, url);
        productManageDatabase.addProduct(product);
        productAdapt.notifyDataSetChanged();
    }

    @Override
    /**
     * Call back method to check is network is connected.
     *
     * @param is connected check connectivity
     */
    public void connection(boolean isConnection) {
        if(!isConnection){
            Toast.makeText(getApplicationContext(), "lost WI-FI connection", Toast.LENGTH_SHORT).show();
            Intent wifi = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
            startActivity(wifi);
        }
    }
}
