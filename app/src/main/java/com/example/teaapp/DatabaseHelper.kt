package com.example.teaapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.teaapp.model.CartItem

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "cart.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CART = "cart"

        private const val COLUMN_ID = "id"
        private const val COLUMN_ITEM_ID = "itemId"
        private const val COLUMN_ITEM_NAME = "itemName"
        private const val COLUMN_ITEM_PRICE = "itemPrice"
        private const val COLUMN_QUANTITY = "quantity"
        private const val COLUMN_IMAGE_URL = "imageUrl"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_CART (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_ITEM_ID TEXT," +
                "$COLUMN_ITEM_NAME TEXT," +
                "$COLUMN_ITEM_PRICE REAL," +
                "$COLUMN_QUANTITY INTEGER," +
                "$COLUMN_IMAGE_URL TEXT" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    // CRUD Operations

    // Insert
    fun addCartItem(cartItem: CartItem): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ITEM_ID, cartItem.itemId)
        values.put(COLUMN_ITEM_NAME, cartItem.itemName)
        values.put(COLUMN_ITEM_PRICE, cartItem.itemPrice)
        values.put(COLUMN_QUANTITY, cartItem.quantity)
        values.put(COLUMN_IMAGE_URL, cartItem.imageUrl)

        val id = db.insert(TABLE_CART, null, values)
        db.close()
        return id
    }

    // Read
    @SuppressLint("Range")
    fun getCartItems(): List<CartItem> {
        val cartItems = mutableListOf<CartItem>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_CART", null)

        if (cursor.moveToFirst()) {
            do {
                val item = CartItem(
                    id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                    itemId = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_ID)),
                    itemName = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)),
                    itemPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_ITEM_PRICE)),
                    quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY)),
                    imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL))
                )
                cartItems.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return cartItems
    }

    // Update
    fun updateCartItem(cartItem: CartItem): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ITEM_ID, cartItem.itemId)
        values.put(COLUMN_ITEM_NAME, cartItem.itemName)
        values.put(COLUMN_ITEM_PRICE, cartItem.itemPrice)
        values.put(COLUMN_QUANTITY, cartItem.quantity)
        values.put(COLUMN_IMAGE_URL, cartItem.imageUrl)

        val result = db.update(TABLE_CART, values, "$COLUMN_ID = ?", arrayOf(cartItem.id.toString()))
        db.close()
        return result
    }

    // Delete
    fun deleteCartItem(itemId: Long): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CART, "$COLUMN_ID = ?", arrayOf(itemId.toString()))
        db.close()
        return result
    }

    // Clear all cart items
    fun clearCart() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_CART")
        db.close()
    }
}
