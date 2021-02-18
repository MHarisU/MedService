package com.med.medservice.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.med.medservice.Models.CartItems.CartItemsList;

import java.util.ArrayList;
import java.util.HashMap;

public class CartDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CART.db";
    public static final String TABLE_CART_ITEMS = "CART_ITEMS";
    public static final String ID = "ID";
    public static final String USER_ID = "USER_ID";
    public static final String ITEM_ID = "ITEM_ID";
    public static final String NAME = "NAME";
    public static final String QUANTITY = "QUANTITY";
    public static final String PRICE = "PRICE";
    public static final String DISCOUNT = "DISCOUNT";
    public static final String TYPE = "TYPE";
    public static final String IMAGE = "IMAGE";
    private HashMap hp;

    public CartDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table CART_ITEMS " +
                        "(ID integer primary key," +
                        " USER_ID integer," +
                        " ITEM_ID integer," +
                        " NAME text," +
                        " QUANTITY integer," +
                        " PRICE integer, " +
                        " DISCOUNT integer," +
                        " TYPE text," +
                        " IMAGE text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS CART_ITEMS");
        onCreate(db);
    }

    public boolean insertCartItem(String USER_ID, String ITEM_ID, String NAME, String QUANTITY, String PRICE, String DISCOUNT, String TYPE, String IMAGE) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_ID", USER_ID);
        contentValues.put("ITEM_ID", ITEM_ID);
        contentValues.put("NAME", NAME);
        contentValues.put("QUANTITY", QUANTITY);
        contentValues.put("PRICE", PRICE);
        contentValues.put("DISCOUNT", DISCOUNT);
        contentValues.put("TYPE", TYPE);
        contentValues.put("IMAGE", IMAGE);
        db.insert("CART_ITEMS", null, contentValues);
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from CART_ITEMS", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_CART_ITEMS);
        return numRows;
    }

    public boolean updateQuantity(Integer ID, String QUANTITY) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("QUANTITY", QUANTITY);
        db.update("CART_ITEMS", contentValues, "ID = ? ", new String[]{Integer.toString(ID)});
        return true;
    }

    public Integer deleteItem(Integer ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("CART_ITEMS",
                "ID = ? ",
                new String[]{Integer.toString(ID)});
    }

    public Integer removeItemId(Integer ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("CART_ITEMS",
                "ITEM_ID = ? ",
                new String[]{Integer.toString(ID)});
    }

    public Integer removeAllItems(Integer ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("CART_ITEMS",
                "ITEM_ID > ? ",
                new String[]{Integer.toString(ID)});
    }

    public ArrayList<CartItemsList> getAllItems() {
        ArrayList<CartItemsList> cart_items_list = new ArrayList<CartItemsList>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from CART_ITEMS", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            cart_items_list.add(new CartItemsList(
                            res.getString(res.getColumnIndex(ID)),
                            res.getString(res.getColumnIndex(USER_ID)),
                            res.getString(res.getColumnIndex(ITEM_ID)),
                            res.getString(res.getColumnIndex(NAME)),
                            res.getString(res.getColumnIndex(QUANTITY)),
                            res.getString(res.getColumnIndex(PRICE)),
                            res.getString(res.getColumnIndex(DISCOUNT)),
                            res.getString(res.getColumnIndex(TYPE)),
                            res.getString(res.getColumnIndex(IMAGE))
                    )
            );
            res.moveToNext();
        }
        return cart_items_list;
    }
}