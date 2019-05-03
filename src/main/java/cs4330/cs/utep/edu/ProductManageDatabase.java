package cs4330.cs.utep.edu;

import android.content.Context;
import android.widget.Toast;

public class ProductManageDatabase extends ProductManage {

    private ProductDatabaseHelper productDatabaseHelper;

    public ProductManageDatabase(Context context){
        super();
        productDatabaseHelper = new ProductDatabaseHelper(context);

        if(productDatabaseHelper.allItems() != null){
            productList = productDatabaseHelper.allItems();
        }

    }

    /**
     * Add product to product list
     *
     * @param product product to add
     */
    public void addProduct(Product product){
        productDatabaseHelper.add(product);
        productList.add(product);
    }

    /**
     * Remove product from product list
     *
     * @param product product to remove
     */
    public void removeProduct(Product product){
        productDatabaseHelper.delete(product.getId());
        productList.remove(product);
    }

    /**
     * Rename product from product list
     *
     * @param product product to rename
     * @param name of product
     */

    public void updateEdit(Product product, String name, String url, double price){
        for (Product i : productList){
            if(i.equals(product)){
                i.setName(name);
                i.setUrl(url);
                i.setCurrentPrice(price);
            }
        }
        productDatabaseHelper.updateName(product);
    }


    /**
     *
     * update each product and update database products
     *
     *
     * @param context
     */
    public void updateRefresh(Context context){
        if(productList != null) {
            for(Product product : productList) {
                product.updatePrice(context);
                product.getPercentChange();
                productDatabaseHelper.updatePrice(product);
            }
        }

    }


}
