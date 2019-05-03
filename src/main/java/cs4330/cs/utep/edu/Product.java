package cs4330.cs.utep.edu;

import android.content.Context;


/**
 * Keep track of products through Product object.
 * Stores product details: name, url, and initial price.
 * updates current price and calculates percent change
 */

public class Product implements WebPriceFinder.AsyncResponse {

    /** name of product*/
    private String name;

    /** url to track product*/
    private String trackUrl;

    /** initial price of product*/
    private double initialPrice;

    /** current price of product*/
    private double currentPrice;

    /** percent change of product*/
    private double percentChange;

    private int id;

    /**
     * Product to be created.
     *
     * @param name name user provides of product.
     * @param initialPrice price user provides.
     * @param trackUrl url user provides to track price
     */
    public Product(String name, double initialPrice, String trackUrl){
        this(name, initialPrice, trackUrl, initialPrice, 0, 0);
    }

    public Product(String name, double initialPrice, String trackUrl, double currentPrice, double percentChange, int id){
        this.name = name;
        this.initialPrice = initialPrice;
        this.trackUrl = trackUrl;
        this.currentPrice = currentPrice;
        this.percentChange = percentChange;
        this.id = id;

    }


    /**
     * initial price changes.
     *
     * @param initialPrice price that is set.
     */
    public void setInitialPrice(double initialPrice){
        this.initialPrice = initialPrice;
    }

    /**
     * Product name changes.
     *
     * @param name product name can be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Product url changes.
     *
     * @param trackUrl product url can be set.
     */
    public void setUrl(String trackUrl){
        this.trackUrl = trackUrl;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    /**
     * Product price updated, current price changed.
     * Product url is given to WebPriceFinder and return current price.
     */
    public void updatePrice(Context context){
        WebPriceFinder syncTask = new WebPriceFinder(context);
        syncTask.response = this;
        syncTask.execute(trackUrl);
    }

    /**
     * Fetch initial price.
     *
     * @return initial price of product
     */
    public double getInitialPrice(){
        return this.initialPrice;
    }

    /**
     * Fetch current price.
     *
     * @return current price of product
     */
    public double getCurrentPrice(){
        this.currentPrice = Math.round(this.currentPrice * 100.0) / 100.0;

        return this.currentPrice;
    }

    /**
     * Fetch name
     *
     * @return name of product
     */
    public String getName(){
        return this.name;
    }

    /**
     * Fetch url
     *
     * @return url of product.
     */
    public String getTrackUrl(){
        return trackUrl;
    }

    /**
     * Calculate percent difference between initial and current prices
     *
     * @return percent change of prices
     */
    public double getPercentChange(){
        this.percentChange = ((this.initialPrice - this.currentPrice) / this.initialPrice) * 100;
        this.percentChange = Math.round(this.percentChange * 100.0) / 100.0 ;

        return this.percentChange;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    @Override
    public void processFinish(double output) {
        this.currentPrice = output;
    }
}
