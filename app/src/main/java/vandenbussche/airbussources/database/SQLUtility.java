package vandenbussche.airbussources.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import vandenbussche.airbussources.core.Product;

/**
 * Created on 03-04-17.
 * This class provides an interface between the SQL database and the Java classes.
 */

public class SQLUtility extends SQLiteOpenHelper {

    /**
     * Our database
     */
    private SQLiteDatabase myDB;
    /**
     * The application context
     */
    private Context context;
    /**
     * The database version (should be incremented in order for the onUpgrade() method to be called).
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * THe database file path.
     */
    private String DATABASE_PATH = "/data/data/vandenbussche.airbussources/databases/"; //context.getFilesDir().getPath();
    private static String DATABASE_NAME = "AirbusSourcesDB.sqlite";
    /**
     * Constructor. Instantiates the DB handling utility
     *
     * @param context the application context.
     */
    private SQLUtility(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Checks whether this login exists in this DB.
     * @param login the login to look for
     * @throws SQLiteException if an error occurs while reading the DB
     * @return true if this login already exists, false otherwise
     */
    public boolean loginExistsInDB(String login) throws SQLiteException {
        Cursor c = myDB.query("\"Member\"",
                new String[]{"\"Login\""},
                "Login=?",
                new String[]{login},
                null,
                null,
                null
        );
        if (c.getCount()==0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    /**
     * Looks in the DB for all the values of the given column that comply with the given condition. Only works for one column
     * Ex: The name of all members older than 30 => getElementFromDB("Member", "Name", "Age<30")
     * @param table the name of the table in which elements will be looked for
     * @param column the column whose elements will be returned.
     * @param conditionSQL A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself).
     *                     Passing null will return all rows for the given table and column.
     * @throws SQLiteException if an error occurs while reading the DB
     * @return An ArrayList<String> containing all the results, or null if there was none
     */
    public ArrayList<String> getElementFromDB(@NonNull String table, @NonNull String column, String conditionSQL) throws SQLiteException{
        ArrayList<String> requestResult = null;
        Cursor c = myDB.query("\""+table+"\"",
                new String[]{"\""+column+"\""},
                conditionSQL,
                null,
                null,
                null,
                null,
                null
        );
        if (c.moveToFirst()) {
            requestResult = new ArrayList<>();
            for (int i = 0; i < c.getCount(); i++) {
                requestResult.add(c.getString(0));  //0 since there will never be multiple columns in this Cursor, given the method's specification
                c.moveToNext();
            }
        }
        c.close();
        return requestResult;
    }

    /**
     * Returns all the DB entries matching the conditions expressed in the passed arguments
     * @param distinct pass true if you want each row to be unique, false otherwise
     * @param table the DB table the query is run on
     * @param conditionSQL ths SQLite WHERE clause (passing null will return the whole table)
     * @param orderBy the SQLite ORDERBY clause (can be null)
     * @return A Cursor containing the query result
     */
    public Cursor getEntriesFromDB(boolean distinct, String table, @Nullable String conditionSQL, @Nullable String orderBy){
        Cursor c = myDB.query(distinct,
                "\""+table+"\"",
                null,
                conditionSQL,
                null,
                null,
                null,
                orderBy,
                null
        );
        return c;
    }

    /**
     * Returns all the information on a given member
     * @param login the login of the member
     * @return An ArrayList<String> containing the info : {login, password, firstName, name}
     *          or null if this login does not appear in the DB
     * @throws SQLiteException if an error occurs while accessing the DB
     */
    public ArrayList<String> getMemberInfo(String login) throws SQLiteException{
        ArrayList<String> requestResult = null;
        Cursor c = myDB.query("Member",
                null,
                "Login=\""+login+"\"",
                null,
                null,
                null,
                null
        );
        if(c.moveToFirst()){
            requestResult = new ArrayList<>();
            requestResult.add(c.getString(c.getColumnIndex("Login")));
            requestResult.add(c.getString(c.getColumnIndex("Password")));
            requestResult.add(c.getString(c.getColumnIndex("First Name")));
            requestResult.add(c.getString(c.getColumnIndex("Name")));
        }
        c.close();
        return requestResult;
    }

    /**
     * Returns all the products associated with the given Member
     * @param login the login of the user we are looking for
     * @return An ArrayList<Product> containing all the product associated with this member in the DB.
     *          Please note that the ArrayList is not ordered in any way,
     *          as it depends on the order in which the SQLite request returned its result
     * @throws SQLiteException if an error occured while accessing the DB
     */
    public ArrayList<Product> getMemberProductsInfo(String login) throws SQLiteException{
        ArrayList<Product> requestResult = null;
        Cursor c = myDB.query("MemberProduct",
                new String[]{"\"Product\""},
                "Login=\""+login+"\"",
                null,
                null,
                null,
                null
        );
        if(c.moveToFirst()){
            requestResult = new ArrayList<>();
            for(int i=0; i<c.getCount(); i++){
                requestResult.add(new Product(c.getString(c.getColumnIndex("Product"))));
            }
        }
        c.close();
        return requestResult;
    }

    /**
     * Renvoie toutes les infos connues sur une recette donnee, EXCEPTE les ingredients, les types et les sous-types.
     * @param name Le nom de la recette voulue
     * @return Un ArrayList<String> contenant les infos : {Name, Image, Total_Time, Prep_Time, Cooking_Time, Nb_Ppl, Creation_Date, Diff_Lvl, Prep_Text, Descr_Text},
     *          ou null si ce nom ne correspond pas a une recette dans la DB
     * @throws SQLiteException en cas d'erreur dans l'acces a la DB
     */
    public ArrayList<String> getRecipeBasicInfo(String name) throws SQLiteException{
        ArrayList<String> requestResult = null;
        Cursor c = myDB.query("Recipe",
                null,   //Puisqu'on veut toutes les colonnes
                "Name=\""+name+"\"",
                null,
                null,
                null,
                null
        );
        if(c.moveToFirst()){
            requestResult = new ArrayList<>();
            for(int i=0; i<9; i++){
                requestResult.add(c.getString(i));
            }
        }
        c.close();
        return requestResult;
    }

    /**
     * Renvoie les ingredients pour une recette donnee.
     * @param name Le nom de la recette voulue
     * @return Un ArrayList<String[]> contenant, pour chaque ingredient, le nom (colonne 0) et la quantite de cet ingredient (colonne 1),
     *          ou null si ce nom ne correspond pas a une recette dans la DB.
     *          Pour acceder a un ingredient, il faut donc appeler getRecipeIngrsInfos().get(0)[0]
     *          Pour acceder a sa quantite, il faut appeler getRecipeIngrsInfos().get(0)[1]
     * @throws SQLiteException en cas d'erreur dans l'acces a la DB
     */
    public ArrayList<String[]> getRecipeIngrsInfo(String name) throws SQLiteException{
        ArrayList<String[]> requestResult = null;
        Cursor c = myDB.query("Recip_Ingrs",
                null,
                "Name=\""+name+"\"",
                null,
                null,
                null,
                null
        );
        if(c.moveToFirst()){
            requestResult = new ArrayList<>();
            for(int i=0; i<c.getCount(); i++){
                Integer amount = c.getInt(c.getColumnIndex("Amount"));
                String[] strings = {c.getString(c.getColumnIndex("Ing_Name")), amount.toString()};
                requestResult.add(strings);
                c.moveToNext();
            }
        }
        c.close();
        return requestResult;
    }

    /**
     * Renvoie les types et tous leurs sous-types de recette, pour une recette donnee.
     * @param name Le nom de la recette voulue
     * @return Un ArrayList<ArrayList<String>> contenant, pour chaque type de la liste, un ArrayList. Celui-ci suit cette structure:{Nom du Type, Sous-Type1, Sous-Type2, etc.}
     *          ou null si ce nom ne correspond pas a une recette dans la DB.
     *          Pour acceder au premier type, il faut donc appeler getRecipeTypesInfos().get(0).get(0)
     *          Pour acceder a un des sous-types de ce type, il faut appeler getRecipeIngrsInfos().get(0).get(1/2/etc)
     *          Pour acceder au deuxieme type, il faut donc appeler getRecipeTypesInfos().get(1).get(0)
     *          Pour acceder a un des sous-types de ce type, il faut appeler getRecipeIngrsInfos().get(1).get(1/2/etc)
     *          etc...
     * @throws SQLiteException en cas d'erreur dans l'acces a la DB
     */
    public ArrayList<ArrayList<String>> getRecipeTypesInfo(String name) throws SQLiteException{
        ArrayList<ArrayList<String>> requestResult = null;
        Cursor c = myDB.query("Recip_Types",
                new String[]{"\"Type\"","\"Sub_Type\""},
                "Name=\""+name+"\"",
                null,
                null,
                null,
                null
        );
        if(c.moveToFirst()){
            requestResult = new ArrayList<>();
            for(int i=0; i<c.getCount(); i++){  //Pour chaque ligne du curseur (Sera de structure {"Type";"SubType"})
                String type = c.getString(0);
                String subType = c.getString(1);
                for(int j=0;j<requestResult.size();j++){    //Pour chaque sous-tableau
                    if(requestResult.get(j).get(0).equals(type)){  //Si il contient le type, on ajoute le sous-type
                        requestResult.get(j).add(subType);
                    }   //Sinon, on continue à chercher
                }
                //Ici, on peut dire que ce type n'existe pas encore.
                ArrayList<String> newList = new ArrayList<>();
                requestResult.add(newList);
                newList.add(type);
                newList.add(subType);
            }
        }
        c.close();
        return requestResult;
    }

    /**
     * Renvoie toutes les infos connues sur un ingredient donne.
     * @param name Le nom pour identifier l'ingredient voulu
     * @return Un ArrayList<String> contenant les infos : {Name, Unit},
     *          ou null si ce nom ne correspond pas a un ingredient dans la DB
     * @throws SQLiteException en cas d'erreur dans l'acces a la DB
     */
    public ArrayList<String> getIngredientFullInfo(String name) throws SQLiteException{
        ArrayList<String> requestResult = null;
        Cursor c = myDB.query("Ingredients",
                null,   //Puisqu'on veut toutes les colonnes
                "Ing_Name=\""+name+"\"",
                null,
                null,
                null,
                null
        );
        if(c.moveToFirst()){
            requestResult = new ArrayList<>();
            requestResult.add(c.getString(c.getColumnIndex("Ing_Name")));
            requestResult.add(c.getString(c.getColumnIndex("Unit")));
        }
        c.close();
        return requestResult;
    }

    /**
     * Adds a nex entry in the Member table.
     * @param values this map contains the initial column values for the
     *            row. The keys should be the column names and the values the
     *            column values
     * @return true if the insertion succeeded, false otherwise
     */
    public boolean addToMemberTable(ContentValues values){

        return (myDB.insert("Member", null, values) != -1);

    }

    /**
     * Adds new entries to the MemberProduct table.
     * The ArrayList products contains all the products to be added to the table
     * @param login the user the given products should be associated with
     * @param products the products that should be added to the table facing the given member
     * @return true if the insertion succeeded, false otherwise
     */
    public boolean addToMemberProductTable(String login, ArrayList<Product> products){
        ContentValues values = new ContentValues(products.size());
        for (int i = 0; i < products.size(); i++) {
            values.put("\"Product\"", products.get(i).getName());

        }
        return (myDB.insert("MemberProduct", null, values) != -1);
    }

    /**
     * Checks if a member with this login exists in the DB, then edits its information with the new values
     * @param login the menber whose information will be modified
     * @param newValues a map from column names to new column values. Can not be null, nor contain null values
     * @return True if the update succeeded, false otherwise
     */
    public boolean updateMemberBasicInfo(String login, ContentValues newValues){
        if( ! this.loginExistsInDB(login)){
            return false;
        } else {
            return (
                    myDB.update("Member", newValues, "Login = \"" + login + "\"", null) == 1
            ); //TODO Care: values should be preceded & followed by a " symbol
        }

        //String instruction = "UPDATE User SET "+
                //"Mail=\'"+newValues.get(0)+"\',"+
                //"Name=\'"+newValues.get(1)+"\',"+
                //"Age="+newValues.get(2)+","+
                //"Sex=\'"+newValues.get(3)+"\',"+
                //"Address=\'"+newValues.get(4)+"\',"+
                //"Password=\'"+newValues.get(5)+"\'"+"" +
                //"WHERE Mail="+"'"+userEmail+"'"+";";
        //myDB.execSQL(instruction);
    }

    /**
     * Removes a member from the DB
     * @param login the login of the menber to remove
     * @return true if deletion has succeeded, false otherwise
     */
    public boolean deleteFromMemberTable(String login){
        if( ! this.loginExistsInDB(login)){
            return false;
        }
        myDB.delete("Member","Login=\""+login+"\"",null);
        return true;
    }


    //----------
    //End of the DB utility methods
    //----------

    /**
     * Creates an empty DB and remplaces it with ours if required
     * @throws IOException if an error occurs during DB copy.
     */
    private void createDataBase() throws IOException {
        boolean dbExists = checkDataBase();
        if(!dbExists){
            //We create a new empty DB.
            this.getReadableDatabase();
            try{
                //We override it with our DB.
                overrideDataBase();
            }catch (IOException e){
                throw new IOException("Error while copying the DB");
            }
        }
    }

    /**
     * Checks whether the DB already exists, to avoid copying it unnecessarily.
     * @return true if the DB already exists, false otherwise
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String path = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null , SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
            //The DB couldn't be opened -> it doesn't exist yet
        }
        if(checkDB != null){
            checkDB.close();
        }
        return (checkDB != null);
    }

    /**
     * Copies our DB onto the empty one we just created
     * @throws IOException if an error occurs during DB copy.
     * @author Juan-Manuel Fluxa | Code piece found on https://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
     */
    private void overrideDataBase() throws IOException{
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outputFile = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outputFile);

        byte[] buffer = new byte[1024];
        int length;
        while((length=myInput.read(buffer)) > 0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Opens the DB on READWRITE mode
     */
    private void openDataBase() throws SQLiteException {
        String path = DATABASE_PATH + DATABASE_NAME;
        this.myDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Prepares the DB in order to be able to execute requests/insertions/etc. on it.
     */
    public static SQLUtility prepareDataBase(Context context){
        SQLUtility db = new SQLUtility(context);
        try{
            db.createDataBase();
        }catch (IOException e){
            throw new Error("Error while creating the DB");
        }
        try{
            db.openDataBase();
        }catch (SQLiteException e){
            throw new SQLiteException("Error while openeing the DB");
        }
        db.myDB.execSQL("PRAGMA foreign_keys = ON;");
        return db;
    }

    @Override
    public synchronized void close(){
        if(myDB!=null){
            myDB.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}
