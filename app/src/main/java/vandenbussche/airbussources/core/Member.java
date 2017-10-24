package vandenbussche.airbussources.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Collections;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.database.SQLUtility;
import vandenbussche.airbussources.exception.InvalidFieldException;
import vandenbussche.airbussources.exception.InvalidPasswordException;

public class Member implements Nameable {

    private String idProfile;
    private String password;
    private String firstName;
    private String surname;
    private String bu;
    //private String commodity;
    private String role;
    private ArrayList<Supplier> suppliers;

    public static Member connectedMember;

    /**
     * Constructor used when signing up for the first time
     * Please make sure the <#param>suppliers</#param> list contains valid Supplier instances
     * (that is, instances who contain the correct Products in them) !
     */
    public Member(Context context, String idProfile, String password, String firstName, String surname, String bu, String role, ArrayList<Supplier> suppliers)
            throws SQLiteException {

        this.idProfile = idProfile;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.bu = bu;
        //this.commodity = commodity;
        this.role = role;
        this.suppliers = suppliers;

        if( ! this.addBasicInfoToDB(context)){
            throw new SQLException("The user could not be inserted in the database. Please try again");
        }
        if( ! this.addSuppliersToDB(context)){
            throw new SQLiteException("Suppliers could not be added to the database. Please try again");
        }
    }

    /**
     * Slightly easier constructor, used during registering, when some info aren't available yet.
     * This incomplete Member instance will NOT be sent to the DB !
     */
    public Member(String idProfile, String password, String firstName, String surname, String bu, String role){
        this.idProfile = idProfile;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.bu = bu;
        //this.commodity = commodity;
        this.role = role;
        this.suppliers = null;
    }

    /**
     * Slightly easier constructor, used during registering, when some info aren't available yet.
     * This incomplete Member instance will NOT be sent to the DB !
     */
    public Member(String idProfile, String password, String firstName, String surname){
        this.idProfile = idProfile;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.bu = null;
        //this.commodity = null;
        this.role = null;
        this.suppliers = null;
    }

    /**
     * Slightly easier constructor, used during registering, when some info aren't available yet.
     * This incomplete Member instance will NOT be sent to the DB !
     */
    public Member(String idProfile, String password, String firstName, String surname, String bu, String role, ArrayList<Supplier> suppliers){
        this.idProfile = idProfile;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.bu = bu;
        //this.commodity = commodity;
        this.role = role;
        this.suppliers = suppliers;
    }

    /**
     * Constructor used when logging in from an existing account
     */
    public Member(Context context, String idProfile, String password) throws InvalidFieldException, InvalidPasswordException {

        SQLUtility db = SQLUtility.prepareDataBase(context);
        if( ! db.idProfileExistsInDB(idProfile)) {
            db.close();
            throw new InvalidFieldException(context.getString(R.string.login_login_not_exist), "login");
        } else {
            ArrayList<String> values = db.getMemberBasicInfo(idProfile);

            if( ! password.equals(values.get(1))){
                db.close();
                throw new InvalidPasswordException(context.getString(R.string.login_password_incorrect));
            } else {
                this.idProfile = values.get(0);
                this.password = values.get(1);
                this.firstName = values.get(2);
                this.surname = values.get(3);
                this.role = values.get(4);
                //this.commodity = values.get(5);
                this.bu = values.get(6);
                this.suppliers = db.getAllMembersSuppliers(this.idProfile);
                db.close();
            }
        }
    }

    /**
     * Easier to use constructor that retrieves values from the database.
     * Please first make sure the idProfile exists in the DB before calling this ! (Otherwise no code will be executed)
     */
    public Member(Context context, String idProfile){

        SQLUtility db = SQLUtility.prepareDataBase(context);

        if(db.idProfileExistsInDB(idProfile)) {
            ArrayList<String> values = db.getMemberBasicInfo(idProfile);
            this.idProfile = values.get(0);
            this.password = values.get(1);
            this.firstName = values.get(2);
            this.surname = values.get(3);
            this.role = values.get(4);
            //this.commodity = values.get(5);
            this.bu = values.get(6);
            this.suppliers = db.getAllMembersSuppliers(this.idProfile);
        }
        db.close();
    }

    private boolean addBasicInfoToDB(Context context){

        ContentValues values = new ContentValues();
        values.put("\"IDProfile\"", this.idProfile);
        values.put("\"Password\"", this.password);
        values.put("\"Surname\"", this.firstName);
        values.put("\"Name\"", this.surname);
        values.put("\"BU\"", this.bu);
        values.put("\"Commodity\"", "");
        values.put("\"Role\"", this.role);

        SQLUtility db = SQLUtility.prepareDataBase(context);
        try {
            return (db.addToMemberTable(values));
        }
        finally{
            db.close();
        }
    }

    private boolean addSuppliersToDB(Context context){
        SQLUtility db = SQLUtility.prepareDataBase(context);
        try {
            return db.addToMemberSupplierProductTable(this.idProfile, suppliers);
        } finally {
            db.close();
        }
    }

    private boolean updateMemberInDB(Context context){
        SQLUtility db = SQLUtility.prepareDataBase(context);
        ContentValues values = new ContentValues();
        values.put("\"IDProfile\"", this.idProfile);
        values.put("\"Password\"", this.password);
        values.put("\"Surname\"", this.firstName);
        values.put("\"Name\"", this.surname);
        values.put("\"BU\"", this.bu);
        values.put("\"Commodity\"", "");
        values.put("\"Role\"", this.role);

        try {
            return ( db.updateMemberBasicInfo(this.idProfile, values) );
        }finally {
            db.close();
        }
    }

    public static boolean isThereANegotiationBetween(Context context, Member member, String supplier){

        SQLUtility db = SQLUtility.prepareDataBase(context);
        ArrayList<String> list = db.getElementFromDB("Member_Supplier_Product", "Negotiation", "Member = \""+member.idProfile+"\" AND Supplier = \""+supplier+"\"");
        db.close();
        if(list.size() == 0){
            //There is no such match in the DB
            return false;
        }
        if(list.size() != 1){
            System.out.println("Redundancy detected in the database ! Please check the Member_Supplier_Product table. Returning false");
            System.out.println("Location : method isThereANegotiationBetween in class Member");
            return false;
        }
        return list.get(0).equals("1");
    }

    public static boolean isThereANegotiationBetween(Context context, String member, String supplier){

        SQLUtility db = SQLUtility.prepareDataBase(context);
        ArrayList<String> list = db.getElementFromDB("Member_Supplier_Product", "Negotiation", "Member = \""+member+"\" AND Supplier = \""+supplier+"\"");
        db.close();
        if(list.size() == 0){
            //There is no such match in the DB
            return false;
        }
        if(list.size() != 1){
            System.out.println("Redundancy detected in the database ! Please check the Member_Supplier_Product table. Returning false");
            System.out.println("Location : method isThereANegotiationBetween in class Member");
            return false;
        }
        return list.get(0).equals("1");
    }

    public static boolean isThereACFTOn(Context context, String memberIDProfile, String supplier, String product){

        SQLUtility db = SQLUtility.prepareDataBase(context);
        ArrayList<String> list = db.getElementFromDB("Member_Supplier_Product", "CFT", "Member =\""+memberIDProfile+"\" AND Product = \""+product+"\" AND Supplier = \""+supplier+"\"");
        db.close();
        if(list.size() != 1){
            System.out.println("Redundancy detected in the database ! Please check the Member_Supplier_Product table. Returning false");
            System.out.println("Location : method isThereACFTOn in class Member");
            return false;
        }
        return list.get(0).equals("1");
    }

    public boolean isWorkingWith(Context context, String supplier){
        SQLUtility db = SQLUtility.prepareDataBase(context);
        ArrayList<String> suppliers = db.getAllMembersSuppliersNames(this.idProfile);
        db.close();
        return suppliers.contains(supplier);
    }
    public boolean isWorkingWith(String supplier){
        for (Supplier localSupplier : this.suppliers) {
            if(localSupplier.getName().equals(supplier)){
                return true;
            }
        }
        return false;
    }
    public static boolean isThereWorkOn(Context context, String memberIDProfile, String supplier, String product){
        SQLUtility db = SQLUtility.prepareDataBase(context);
        Cursor cursor = db.getEntriesFromDB("Member_supplier_Product", new String[]{"Member"}, "Member =\""+memberIDProfile+"\" AND Product = \""+product+"\" AND Supplier = \""+supplier+"\"", null);
        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public String getIdentifier(){return this.getLogin();}

    public String getLogin(){return idProfile;}
    public String getPassword(){return password;}
    public String getFirstName(){return firstName;}
    public String getName(){return surname;}
    public String getBu(){return bu;}
    //public String getCommodity(){return commodity;}
    public String getRole(){return role;}
    public ArrayList<Supplier> getSuppliers(){return this.suppliers;}

    public void setPassword(Context context, String password){
        this.password = password;
        if( ! this.updateMemberInDB(context)){
            System.err.println("Password updated in the instance but NOT in the DB !");
        }
    }
    public void setFirstName(Context context, String firstName){
        this.firstName = firstName;
        if( ! this.updateMemberInDB(context)){
            System.err.println("First Name updated in the instance but NOT in the DB !");
        }
    }
    public void setSurname(Context context, String surname){
        this.surname = surname;
        if( ! this.updateMemberInDB(context)){
            System.err.println("Surname updated in the instance but NOT in the DB !");
        }
    }
    public void setBu(Context context, String bu){
        this.bu = bu;
        if( ! this.updateMemberInDB(context)){
            System.err.println("BU updated in the instance but NOT in the DB !");
        }
    }
    //public void setCommodity(Context context, String commodity){
        //this.commodity = commodity;
        //if( ! this.updateMemberInDB(context)){
            //System.err.println("Commodity updated in the instance but NOT in the DB !");
        //}
    //}
    public void setRole(Context context, String role){
        this.role = role;
        if( ! this.updateMemberInDB(context)){
            System.err.println("Role updated in the instance but NOT in the DB !");
        }
    }
    public void setSuppliers(Context context, ArrayList<Supplier> suppliers){
        Collections.sort(suppliers);
        this.suppliers = suppliers;
        if( ! this.updateMemberInDB(context)){
            System.err.println("Suppliers list updated in the instance but NOT in the DB !");
        }
    }
    public void setSuppliers(ArrayList<Supplier> suppliers){
        this.suppliers = suppliers;
    }

    public String toString(){
        return this.idProfile;
    }
}
