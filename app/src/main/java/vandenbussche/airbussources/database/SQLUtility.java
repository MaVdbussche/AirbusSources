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
import java.util.Collections;
import java.util.Set;

import vandenbussche.airbussources.core.Member;
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
    private final Context context;
    /**
     * The database version (should be incremented in order for the onUpgrade() method to be called).
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * The database file path.
     */
    private static String DATABASE_PATH; // = "/data/data/vandenbussche/airbussources/databases/";
    private static String DATABASE_NAME = "AirbusSourcesDB.sqlite";

    /***
     * Constructor. Instantiates the DB handling utility
     *
     * @param context the application context.
     */
    private SQLUtility(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = context.getFilesDir().getPath()+"/";
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
     * Ex: The name of all members older than 30 => getElementFromDB("Member", "Name", "Age>30")
     * @param table the name of the table in which elements will be looked for
     * @param column the column whose elements will be returned.
     * @param conditionSQL A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself).
     *                     Passing null will return all rows for the given table and column.
     * @throws SQLiteException if an error occurs while reading the DB
     * @return An ArrayList<String> containing all the results, or an empty list if there was none
     */
    public ArrayList<String> getElementFromDB(String table, String column, String conditionSQL) throws SQLiteException{
        ArrayList<String> requestResult = new ArrayList<>();
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
            for (int i = 0; i < c.getCount(); i++) {
                requestResult.add(c.getString(0));
                //0 since there will never be multiple columns in this Cursor, given the method's specs
                c.moveToNext();
            }
        }
        c.close();
        return requestResult;
    }

    /**
     * Returns all the DB entries (and all the columns of these entries) matching the conditions expressed in the passed arguments
     * @param table the DB table the query is run on
     * @param columns the columns of the found entries you want in the Cursor. Passing null will return all columns of the given row
     * @param conditionSQL the SQLite WHERE clause. Passing null will return all the rows of the table
     * @param orderBy the SQLite ORDERBY clause. Passing null means the results won't be ordered in any specific way
     * @return A Cursor containing the query result
     */
    public Cursor getEntriesFromDB(String table, String[] columns, String conditionSQL, String orderBy){
        Cursor c = myDB.query("\""+table+"\"",
                columns,
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
     * Returns all the Suppliers associated with the given Member.
     * Note that those Supplier instances also hold a list of the Products
     * from this Supplier the given Member is working on.
     * @param  IDProfile the IDProfile of the user we are looking for
     * @return An ArrayList<Supplier> containing all the suppliers associated with this member in the DB.
     * @throws SQLiteException if an error occurred while accessing the DB
     */
    public ArrayList<Supplier> getAllMembersSuppliers(String IDProfile) throws SQLiteException{

        ArrayList<Supplier> returnedList = new ArrayList<>();
        ArrayList<String> suppliersNames = getAllMembersSuppliersNames(IDProfile);
        for ( String name : suppliersNames )
        {
            returnedList.add( new Supplier(name, getRelevantSuppliersProducts(IDProfile,name), getNegotiationState(IDProfile,name)) );
        }
        return returnedList;
    }

    /**
     * Returns all the Products associated with the given Supplier
     * @param  supplier the name of the supplier we are looking for
     * @return An ArrayList<Product> containing all the products associated with this supplier in the DB.
     * @throws SQLiteException if an error occurred while accessing the DB
     */
    public ArrayList<Product> getAllSuppliersProducts(String supplier){

        ArrayList<String> names = getAllSuppliersProductsNames(supplier);
        ArrayList<Product> returnedList = new ArrayList<>(names.size());
        for( String name : names ){
            returnedList.add( new Product(name, false) );
        }
        return returnedList;
    }

    /**
     * Returns all the Products associated with the given Suppliers the given Member is working on
     * @param IDProfile the Member we are looking for
     * @param  supplier the name of the Supplier we are looking for
     * @return An ArrayList<Product> containing all the products associated
     * with this supplier and this Member in the DB. This list is ordered alphabetically.
     * @throws SQLiteException if an error occurred while accessing the DB
     */
    public ArrayList<Product> getRelevantSuppliersProducts(String IDProfile, String supplier){

        ArrayList<Product> returnedList = new ArrayList<>();
        Cursor c = getEntriesFromDB("Member_Supplier_Product",
                new String[]{"Product", "CFT"},
                "Member=\""+IDProfile+"\" AND Supplier=\""+supplier+"\"",
                null
        );
        if(c.moveToFirst()){
            for(int i=0; i<c.getCount(); i++){
                boolean cft = (c.getString(c.getColumnIndex("CFT")).equals("1"));
                returnedList.add(new Product(c.getString(c.getColumnIndex("Product")), cft));
                c.moveToNext();
            }
        }
        Collections.sort(returnedList);
        c.close();
        return returnedList;
    }

    /**
     * Returns all the Products associated with the given Member.
     * They are not ordered in any way: if you want them to be ordered by Supplier, use
     * getAllMemberSuppliers instead and query the Products list in those Supplier instances.
     * This makes this function quite useless in my opinion, but it's there in case one ever need it.
     * @param IDProfile the IDProfile of the user we are looking for
     * @return An ArrayList<Product> containing all the products associated with this member in the DB.
     * @throws SQLiteException if an error occurred while accessing the DB
     */
    /**
    public ArrayList<Product> getAllMembersProducts(String IDProfile) throws SQLiteException{

        ArrayList<Supplier> suppliers = getAllMembersSuppliers(IDProfile);
        ArrayList<Product> returnedList = new ArrayList<>();
        ArrayList<Product> suppliersProducts;
        for( Supplier supplier : suppliers ){
            suppliersProducts = getAllSuppliersProducts(supplier.getName());
            for (Product product : suppliersProducts ) {
                product.setCFT(getCFTState(IDProfile, product.getName()));
                returnedList.add(product);
            }
        }
        return returnedList;
    }
     **/

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

    public ArrayList<String> getAllSuppliersProductsNames(String supplier){

        return getElementFromDB("Suppl_Prod", "Product", "Supplier=\""+supplier+"\"");
    }

    public ArrayList<String> getAllMembersSuppliersNames(String IDProfile){

        return getElementFromDB("Member_Supplier_Product", "Supplier", "Member=\""+IDProfile+"\"");
    }

    public ArrayList<String> getAllMembersProductsNames(String IDProfile){

        return getElementFromDB("Member_Supplier_Product", "Product", "Member=\""+IDProfile+"\"");
    }

    /**
    public boolean isThereAssociation(String IDProfile, String supplier, String product){

        Cursor c = getEntriesFromDB("Member_Supplier_Product",
                                    new String[]{"Member", "Supplier", "Product"},
                                    "")
        return getElementFromDB("Member_Supplier_Product", "Product", "Member=\""+IDProfile+"\"");
    }
     **/

    /**
     * Returns the negotiation state between the given Member and Supplier
     * @param IDProfile the Member to be looked for
     * @param supplier the supplier to be looked for
     * @return the negotiation state as a boolean
     */
    public boolean getNegotiationState(String IDProfile, String supplier) {

        ArrayList<String> resultsAsStrings = getElementFromDB("Member_Supplier_Product", "Negotiation", "Member=\""+IDProfile+"\" AND Supplier=\""+supplier+"\"");
        if (resultsAsStrings.size() == 0) {
            System.err.println("Problem in the database structure ! (No such Member-Supplier association)");
            System.err.println("Location : getNegotiationState in SQLUtility");
            throw new SQLiteException();
        }
        return resultsAsStrings.get(0).equals("1");
    }

    /**
     * Returns the CFT state between the given Member and Product
     * @param IDProfile the Member to be looked for
     * @param product the Product to be looked for
     * @return the CFT state as a boolean
     */
    public boolean getCFTState(String IDProfile, String product) {

        ArrayList<String> resultsAsStrings = getElementFromDB("Member_Supplier_Product", "CFT", "Member=\""+IDProfile+"\" AND Product=\""+product+"\"");
        if (resultsAsStrings.size() == 0) {
            System.err.println("Problem in the database structure ! (No such Member-Product association)");
            System.err.println("Location : getCFTState in SQLUtility");
            throw new SQLiteException();
        }
        return resultsAsStrings.get(0).equals("1");
    }

    /**
     * Adds a new entry in the Member table.
     * @param values this map contains the initial column values for the
     *            row. The keys should be the column names and the values the
     *            column values
     * @return true if the insertion succeeded, false otherwise
     */
    public boolean addToMemberTable(ContentValues values){

        //if(values.keySet().size() == 7) {
            return (myDB.insert("Member", null, values) != -1);
        //} else {
        //    return false;
        //}
    }

    /**
     * Adds new entries to the Member_Supplier_Product table.
     * The ArrayList <#param>suppliers</#param> contains all the Suppliers to be added to the table
     * Those Supplier instances must hold the correct Product instances (including CFT state)
     * @param IDProfile the Member the given Suppliers should be associated with
     * @param suppliers the Suppliers that should be added to the table facing the given Member
     * @return true if the insertion succeeded, false otherwise
     */
    public boolean addToMemberSupplierProductTable(String IDProfile, ArrayList<Supplier> suppliers){
        ContentValues values = new ContentValues();
        boolean flag;

        for (int i = 0; i < suppliers.size(); i++) {
            Supplier supplier = suppliers.get(i);

            values.put("\"Member\"", IDProfile);
            values.put("\"Supplier\"", supplier.getName());
            if(supplier.getNegotiationState()) {
                values.put("\"Negotiation\"", "1");
            } else {
                values.put("\"Negotiation\"", "0");
            }

            ArrayList<Product> products = supplier.getProducts();

            for (Product product : products) {
                values.put("\"Product\"", product.getName());
                if(product.getIsOnCFT()) {
                    values.put("\"CFT\"", 1);
                } else {
                    values.put("\"CFT\"", 0);
                }
                flag = ( myDB.insert("Member_Supplier_Product", null, values) == -1 );
                if(flag){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Adds new entries to the Member_Supplier table.
     * The ArrayList <#param>suppliers</#param> contains all the suppliers to be added to the table.
     * @param login the user the given products should be associated with
     * @param suppliers the suppliers that should be added to the table facing the given member.
     *                  Make sure the Supplier instances contain the correct isOnNegotiation boolean !
     * @return true if the insertion succeeded, false otherwise
     */
    /**
    public boolean addToMemberSupplierTable(String login, ArrayList<Supplier> suppliers){
        ContentValues values = new ContentValues(suppliers.size());
        for (int i = 0; i < suppliers.size(); i++) {
            values.put("\"Supplier\"", suppliers.get(i).getName());
            values.put("\"Member\"", login);
            if(suppliers.get(i).getNegotiationState()) {
                values.put("\"Negotiation\"", 1);
            } else {
                values.put("\"Negotiation\"", 0);
            }
        }
        return (myDB.insert("Member_Supplier", null, values) != -1);
    }
     **/

    /**
     * Checks if a member with this login exists in the DB, then edits its information with the new values
     * @param idProfile the member whose information will be modified
     * @param values a map from column names to new column values. Can not be null, nor contain null values
     * @return True if the update succeeded, false otherwise
     */
    public boolean updateMemberBasicInfo(String idProfile, ContentValues values){
        if( ! this.idProfileExistsInDB(idProfile)){
            return false;
        } else {
            ContentValues newValues = new ContentValues();
            newValues.put("\"IDProfile\"", values.getAsString("IDProfile"));
            newValues.put("\"Password\"", values.getAsString("Password"));
            newValues.put("\"Name\"", values.getAsString("Name"));
            newValues.put("\"Surname\"", values.getAsString("Surname"));
            newValues.put("\"Role\"", values.getAsString("Role"));
            newValues.put("\"Commodity\"", values.getAsString("Commodity"));
            newValues.put("\"BU\"", values.getAsString("BU"));
            return (
                    myDB.update("Member", newValues, "IDProfile = \""+idProfile+"\"", null) == 1
            );
        }
    }

    /**
     * Checks if a member with this login exists in the DB, then edits its information with the new values
     * @param idProfile the member whose information will be modified
     * @param newValues an ArrayList<String> containing all the values of the updated member in the exact same order
     *                  as the database columns (IDProfile, password, name, surname, role, commodity, business unit).
     *                  Since this method is very sensitive to errors, prefer the method using ContentValues when possible.
     *                  Can not be null, nor contain any null values
     * @return True if the update succeeded, false otherwise
     */
    public boolean updateMemberBasicInfo(String idProfile, ArrayList<String> newValues){
        if( ! this.idProfileExistsInDB(idProfile)){
            return false;
        } else {
            ContentValues contentValues = new ContentValues(6);
            //We don't retrieve the IDProfile value, as we do not allow to modify this field in the database anyway
            contentValues.put("Password", newValues.get(1));
            contentValues.put("Name", newValues.get(2));
            contentValues.put("Surname", newValues.get(3));
            contentValues.put("Role", newValues.get(4));
            contentValues.put("Commodity", newValues.get(5));
            contentValues.put("BU", newValues.get(6));
            return (
                    myDB.update("Member", contentValues, "IDProfile = \""+idProfile+"\"", null) == 1
            );
        }
    }

    /**
     * Removes a Member from the DB
     * @param login the login of the Member to remove
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
            checkDB = SQLiteDatabase.openDatabase(path, null , SQLiteDatabase.OPEN_READWRITE);
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
            throw new SQLiteException("Error while opening the DB");
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
