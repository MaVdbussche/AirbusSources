package vandenbussche.airbussources.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.core.Supplier;

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
    private String DATABASE_PATH = "/data/data/vandenbussche/airbussources/database";
    private static String DATABASE_NAME = "AirbusSourcesDB.sqlite";
    /***
     * Constructor. Instantiates the DB handling utility
     *
     * @param context the application context.
     */
    private SQLUtility(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Checks whether this IDProfile exists in this DB.
     * @param IDProfile the IDProfile to look for
     * @throws SQLiteException if an error occurs while reading the DB
     * @return true if this IDProfile already exists, false otherwise
     */
    public boolean idProfileExistsInDB(String IDProfile) throws SQLiteException {
        Cursor c = myDB.query("\"Member\"",
                new String[]{"\"IDProfile\""},
                "IDProfile=?",
                new String[]{IDProfile},
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
    public ArrayList<String> getElementFromDB(String table, String column, String conditionSQL) throws SQLiteException{
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
     * Returns all the DB entries (and all the columns of these entries) matching the conditions expressed in the passed arguments
     * @param table the DB table the query is run on
     * @param conditionSQL the SQLite WHERE clause (passing null will return the whole table)
     * @param orderBy the SQLite ORDERBY clause (can be null)
     * @return A Cursor containing the query result
     */
    public Cursor getEntriesFromDB(String table, String conditionSQL, String orderBy){
        Cursor c = myDB.query("\""+table+"\"",
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
     * Returns all the personal information on a given member
     * @param IDProfile the IDProfile of the member to look for
     * @return An ArrayList<String> containing the info : {IDProfile, password, name, surname, role, commodity, business unit}
     *          or null if this IDProfile does not appear in the DB
     * @throws SQLiteException if an error occurs while accessing the DB
     */
    public ArrayList<String> getMemberBasicInfo(String IDProfile) throws SQLiteException{
        ArrayList<String> requestResult = null;
        Cursor c = myDB.query("Member",
                new String[]{"IDProfile", "Password", "Name", "Surname", "Role", "Commodity", "BU"},
                "IDProfile=\""+IDProfile+"\"",
                null,
                null,
                null,
                null
        );
        if(c.moveToFirst()){
            requestResult = new ArrayList<>();
            requestResult.add(c.getString(c.getColumnIndex("IDProfile")));
            requestResult.add(c.getString(c.getColumnIndex("Password")));
            requestResult.add(c.getString(c.getColumnIndex("Name")));
            requestResult.add(c.getString(c.getColumnIndex("Surname")));
            requestResult.add(c.getString(c.getColumnIndex("Role")));
            requestResult.add(c.getString(c.getColumnIndex("Commodity")));
            requestResult.add(c.getString(c.getColumnIndex("BU")));
        }
        c.close();
        return requestResult;
    }

    /**
     * Returns all the Suppliers associated with the given Member
     * @param  IDProfile the IDProfile of the user we are looking for
     * @return An ArrayList<String> containing all the suppliers associated with this member in the DB.
     * @throws SQLiteException if an error occured while accessing the DB
     */
    public ArrayList<String> getAllMembersSuppliers(String IDProfile) throws SQLiteException{

        return getElementFromDB("Member_Supplier", "Supplier", "Member=\""+IDProfile+"\"");
    }

    public ArrayList<String> getAllSuppliersNames() throws SQLiteException {

        return getElementFromDB("Supplier", "Name", null);
    }

    public ArrayList<String> getAllProductsNames() throws SQLiteException {

        return getElementFromDB("Product", "Name", null);
    }

    public ArrayList<String> getAllRolesNames() throws SQLiteException {

        return getElementFromDB("Role", "Name", null);
    }

    public ArrayList<String> getAllCommoditiesNames() throws SQLiteException {

        return getElementFromDB("Commodity", "Name", null);
    }

    public ArrayList<String> getAllBUsNames() throws SQLiteException {

        return getElementFromDB("BU", "Name", null);
    }

    public ArrayList<String> getAllSuppliersProductsNames(Supplier supplier){

        return getElementFromDB("Suppl_Prod", "Product", "Supplier=\""+supplier+"\"");
    }

    /**
     * Adds a new entry in the Member table.
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
     * @param idProfile the menber whose information will be modified
     * @param newValues a map from column names to new column values. Can not be null, nor contain null values
     * @return True if the update succeeded, false otherwise
     */
    public boolean updateMemberBasicInfo(String idProfile, ContentValues newValues){
        if( ! this.idProfileExistsInDB(idProfile)){
            return false;
        } else {
            return (
                    myDB.update("Member", newValues, "Login = \"" + idProfile + "\"", null) == 1
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
        if( ! this.idProfileExistsInDB(login)){
            return false;
        }
        return (myDB.delete("Member","Login=\""+login+"\"",null) == 1);
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
