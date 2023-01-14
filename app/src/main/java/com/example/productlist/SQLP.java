package com.example.productlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLP extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ProductDB";

    // Nom de la table
    private static final String TABLE_PRODUCT = "Product";

    // Les colonnes de la table Task
    private static final String KEY_ID = "id";
    private static final String KEY_LIBELLE = "libelle";
    private static final String KEY_CODEBARRE = "codeBarre";
    private static final String KEY_PRIX = "prix";
   // private static final String KEY_DISPONIBLE= "disponible";
    private static final String KEY_IMAGE = "image";

    private static final String[] COLUMNS = {KEY_ID,KEY_LIBELLE,KEY_CODEBARRE,KEY_PRIX,KEY_IMAGE};
    public SQLP(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLP(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create task table
        String CREATE_Product_TABLE = "CREATE TABLE Product ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "libelle TEXT, "+
                "CodeBarre TEXT, "+
                "prix TEXT, "+
                "image TEXT )";

        // create task table
        db.execSQL(CREATE_Product_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older joueur table if existed
        db.execSQL("DROP TABLE IF EXISTS Product");

        // create fresh books table
        this.onCreate(db);
    }
    public long AddTask(Produit produit){


        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. créer un objet  ContentValues pour stocker objets de type cle "column"/valeur
        ContentValues values = new ContentValues();
        values.put(KEY_LIBELLE, produit.getLibelle()); // get Libelle
        values.put(KEY_CODEBARRE, produit.getCodeBarre()); // get Date
        values.put(KEY_PRIX, produit.getPrix());

        values.put(KEY_IMAGE, produit.getImage()); //get Heure
        // 3. insert
        long rowId =  db.insert(TABLE_PRODUCT, // table
                null, //nullColumnHack
                values); // cle/valeur -> cle = column names/ valeurs = column values

        // 4. close
        db.close();
        return rowId;
    }
    public Produit getProduit(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_PRODUCT, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build task object
        Produit task = new Produit(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                Boolean.FALSE);
        // 5. return task
        return task;
    }
    public ArrayList<Produit> getListProduits() {
        ArrayList<Produit> PRODUITS = new ArrayList<Produit>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_PRODUCT + " ORDER BY " + KEY_ID  +  " DESC";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Produit produit = null;
        if (cursor.moveToFirst()) {
            do {
                produit = new Produit();
                produit.setId(Integer.parseInt(cursor.getString(0)));
                produit.setLibelle(cursor.getString(1));
                produit.setCodeBarre(cursor.getString(2));
                produit.setPrix(cursor.getString(3));
                produit.setImage(cursor.getString(4));

                // Add book to books
                PRODUITS.add(produit);
            } while (cursor.moveToNext());
        }

        // return books
        return PRODUITS;
    }
    public int updateProduit(Produit Produits) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("libelle", Produits.getLibelle()); // get libelle
        values.put("codeBarre", Produits.getCodeBarre()); // get date
        values.put("prix", Produits.getPrix());
        // 3. updating row
        int i = db.update(TABLE_PRODUCT, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(Produits.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    public void supprimerProduit(Produit produits) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_PRODUCT, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(produits.getId())}); //selections args

        // 3. close
        db.close();






    }
}
