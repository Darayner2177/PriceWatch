package cs4330.cs.utep.edu;

import java.util.ArrayList;
import java.util.List;

public class ProductManage {

    public List<Product> productList;

    /*
    * Constructor: Create a new product list
    *
    */
    public ProductManage() {
        productList = new ArrayList<Product>();

    }
    /**
    * Add product to product list
    *
    * @param product product to add
    */
    public void addProduct(Product product){
       productList.add(product);
    }

    /**
    * Remove product from product list
    *
    * @param product product to remove
    */
    public void removeProduct(Product product){
        productList.remove(product);
    }

    /**
    * Rename product from product list
    *
    * @param product product to rename
    * @param newName of product
    */

    public void renameProduct(Product product, String newName){
        for (Product i : productList){
            if(i.equals(product)){
                i.setName(newName);
            }
         }
    }

    /**
     * Change product url
     *
     * @param product product to change url
     * @param url new url of product
     */
    public void update(Product product, String name, String url){
        for (Product i : productList){
            if(i.equals(product)){
                i.setName(name);
                i.setUrl(url);
            }
        }
    }

    /**
     * Get product list
     *
     * @return product list
     */
    public List<Product> getProductList(){
        return this.productList;
    }

    /**
     * Set a product list,
     * this helps when a orientation change needs to restore product list
     *
     * @param list a product list that is to be set
     */
    public void setProductList(List<Product> list) {
        this.productList = new ArrayList<Product>(list);

    }


}
