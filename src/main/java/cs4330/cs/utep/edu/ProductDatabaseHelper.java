package cs4330.cs.utep.edu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class ProductDatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "ProductListDB";

    public static final String TABLE_PRODUCT = "Products";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_INITIAL = "Initial";
    public static final String COLUMN_CURRENT = "Current";
    public static final String COLUMN_URL = "Url";
    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_PERCENT = "Percent";


   public ProductDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     *
     * Create sqli database tables
     *
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String table = "CREATE TABLE " + TABLE_PRODUCT + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_INITIAL + " DOUBLE NOT NULL, " +
                COLUMN_CURRENT + " DOUBLE NOT NULL, " +
                COLUMN_URL + " TEXT NOT NULL, " +
                COLUMN_PERCENT + " DOUBLE NOT NULL" + ")";


        sqLiteDatabase.execSQL(table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(sqLiteDatabase);
    }

    /**
     *
     * Add product to sqli database
     *
     * @param product
     */
    public void add(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_CURRENT, product.getCurrentPrice());
        values.put(COLUMN_INITIAL, product.getInitialPrice());
        values.put(COLUMN_URL, product.getTrackUrl());
        values.put(COLUMN_PERCENT, product.getPercentChange());

        long id = db.insert(TABLE_PRODUCT, null, values);
        product.setId((int)id);

        db.close();
    }

    /**
     * Retreive all products
     *
     *
     *
     * @return list of products stored in database
     */

    public List<Product> allItems() {
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                double initial = cursor.getDouble(cursor.getColumnIndex(COLUMN_INITIAL));
                double current = cursor.getDouble(cursor.getColumnIndex(COLUMN_CURRENT));
                String url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
                double percent = cursor.getDouble(cursor.getColumnIndex(COLUMN_PERCENT));

                Product product = new Product(name, initial, url, current, percent, id);

                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return products;
    }


    /**
     * Delete product
     *
     *
     * @param id of product to delete
     */
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, COLUMN_ID + " = ?", new String[] { Integer.toString(id) } );
        db.close();
    }

    /**
     * Update product
     *
     * @param product to update
     */
    public void updateName(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_URL, product.getTrackUrl());
        db.update(TABLE_PRODUCT, values, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
        db.close();
    }

    /**
     *
     * Update price
     *
     * @param product
     */
    public void updatePrice(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CURRENT, product.getCurrentPrice());
        values.put(COLUMN_PERCENT, product.getPercentChange());
        db.update(TABLE_PRODUCT, values, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
        db.close();
    }


}
