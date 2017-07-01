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

    private String login;
    private String password;
    private String firstName;
    private String name;

    public static Member connectedMember;

    /**
     * Constructor used when signing up for the first time
     */
    public Member(Context context, String login, String password1, String password2, String firstName, String name) throws InvalidPasswordException, InvalidFieldException, ExistingLoginException, SQLiteException {

        testPassword(context, password1, password2);
        testLogin(context, login);

        this.login = login;
        this.password = password1;
        this.firstName = firstName;
        this.name = name;

        if( ! this.addToDB(context)){
            throw new SQLException("The user could not be inserted in the database. Please try again");
        }

        connectedMember = this;
    }

    /**
     * Constructor used when logging in from an existing account
     */
    public Member(Context context, String login, String password) throws InvalidFieldException, InvalidPasswordException {

        SQLUtility db = SQLUtility.prepareDataBase(context);
        if(db.loginExistsInDB(login)){

            ArrayList<String> values = db.getMemberInfo(login);
            if(password.equals(values.get(1))){
                this.login = values.get(0);
                this.password = values.get(1);
                this.firstName = values.get(2);
                this.name = values.get(3);

                connectedMember = this;
            }
            throw new InvalidPasswordException(context.getString(R.string.login_password_incorrect));
        }
        throw new InvalidFieldException(context.getString(R.string.login_login_not_exist), "login");
    }

    private boolean addToDB(Context context){

        ContentValues values = new ContentValues(4);    //TODO adjust this value if table size changes in the future
        values.put("\"Login\"", this.login);
        values.put("\"Password\"", this.password);
        values.put("\"First Name\"", this.firstName);
        values.put("\"Name\"", this.name);

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

    private void testLogin(Context context, String login) throws ExistingLoginException, InvalidFieldException {

        SQLUtility db = SQLUtility.prepareDataBase(context);
        if(db.loginExistsInDB(login)){
            db.close();
            throw new ExistingLoginException(context.getString(R.string.login_login_already_used));
        }
        if (login.length()<5)
        {
            throw new InvalidFieldException(context.getString(R.string.login_login_too_short), "login");
        }
    }

    public String getLogin(){return login;}
    public String getPassword(){return password;}
    public String getFirstName(){return firstName;}
    public String getName(){return name;}
}
