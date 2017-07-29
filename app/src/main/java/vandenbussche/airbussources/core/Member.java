package vandenbussche.airbussources.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.database.SQLUtility;
import vandenbussche.airbussources.exception.ExistingLoginException;
import vandenbussche.airbussources.exception.InvalidFieldException;
import vandenbussche.airbussources.exception.InvalidPasswordException;

public class Member {

    private String idProfile;
    private String password;
    private String firstName;
    private String surname;
    private String bu;
    private String commodity;
    private String role;

    public static Member connectedMember;

    /**
     * Constructor used when signing up for the first time
     */
    public Member(Context context, String idProfile, String password1, String password2, String firstName, String surname, String bu, String commodity, String role)
            throws InvalidPasswordException, InvalidFieldException, ExistingLoginException, SQLiteException {

        testLogin(context, idProfile);
        testPassword(context, password1, password2);

        this.idProfile = idProfile;
        this.password = password1;
        this.firstName = firstName;
        this.surname = surname;
        this.bu = bu;
        this.commodity = commodity;
        this.role = role;

        if( ! this.addToDB(context)){
            throw new SQLException("The user could not be inserted in the database. Please try again");
        }
    }

    /**
     * Constructor used when logging in from an existing account
     */
    public Member(Context context, String idProfile, String password) throws InvalidFieldException, InvalidPasswordException {

        SQLUtility db = SQLUtility.prepareDataBase(context);
        if( ! db.idProfileExistsInDB(idProfile)) {
            throw new InvalidFieldException(context.getString(R.string.login_login_not_exist), "login");
        } else {

            ArrayList<String> values = db.getMemberInfo(idProfile);
            if(password.equals(values.get(1))){
                this.idProfile = values.get(0);
                this.password = values.get(1);
                this.firstName = values.get(2);
                this.surname = values.get(3);
            }
            throw new InvalidPasswordException(context.getString(R.string.login_password_incorrect));
        }
    }

    private boolean addToDB(Context context){

        ContentValues values = new ContentValues(4);    //TODO adjust this value if table size changes in the future
        values.put("\"Login\"", this.idProfile);
        values.put("\"Password\"", this.password);
        values.put("\"First Name\"", this.firstName);
        values.put("\"Name\"", this.surname);

        SQLUtility db = SQLUtility.prepareDataBase(context);
        return (db.addToMemberTable(values));
    }

    private void testPassword(Context context, String password1, String password2) throws InvalidPasswordException, InvalidFieldException {
        if ( ! password1.equals(password2)){
            throw new InvalidPasswordException(context.getString(R.string.login_password_different));
        }
        if(password1.length()<5){
            throw new InvalidFieldException(context.getString(R.string.login_password_too_short), "password");
        }
    }

    private void testLogin(Context context, String idProfile) throws ExistingLoginException, InvalidFieldException {

        SQLUtility db = SQLUtility.prepareDataBase(context);
        if(db.idProfileExistsInDB(idProfile)){
            db.close();
            throw new ExistingLoginException(context.getString(R.string.login_login_already_used));
        }
        if (idProfile.length()<5)
        {
            throw new InvalidFieldException(context.getString(R.string.login_login_too_short), "login");
        }
    }

    public String getLogin(){return idProfile;}
    public String getPassword(){return password;}
    public String getFirstName(){return firstName;}
    public String getName(){return surname;}
    public String getBu(){return bu;}
    public String getCommodity(){return commodity;}
    public String getRole(){return role;}
}
