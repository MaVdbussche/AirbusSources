package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.database.SQLUtility;
import vandenbussche.airbussources.exception.ExistingLoginException;
import vandenbussche.airbussources.exception.InvalidFieldException;
import vandenbussche.airbussources.exception.InvalidPasswordException;

public class SignUpActivity extends AppCompatActivity {

    private TextView welcomeText;

    private EditText nameField;
    private EditText surnameField;
    private EditText loginField;
    private EditText passwordField;
    private EditText password2Field;

    private Button toScreen2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.signup_screen_title_1);
        ab.show();

        welcomeText = (TextView) findViewById(R.id.signupScreen1WelcomeTextView);

        nameField = (EditText) findViewById(R.id.name_field);
        surnameField = (EditText) findViewById(R.id.surname_field);
        loginField = (EditText) findViewById(R.id.login_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        password2Field = (EditText) findViewById(R.id.password_field_2);

        toScreen2 = (Button) findViewById(R.id.signupScreen1ButtonNext);

        toScreen2.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        try {
                            SQLUtility db = SQLUtility.prepareDataBase(SignUpActivity.this);
                            if(db.idProfileExistsInDB(loginField.getText().toString())){
                                db.close();
                                throw new ExistingLoginException(SignUpActivity.this.getString(R.string.login_login_already_used));
                            }
                            if (loginField.getText().toString().length()<5)
                            {
                                throw new InvalidFieldException(SignUpActivity.this.getString(R.string.login_login_too_short), "login");
                            }

                            String password1 = passwordField.getText().toString();
                            String password2 = password2Field.getText().toString();
                            if(password1.length() < 5){
                                throw new InvalidFieldException(SignUpActivity.this.getString(R.string.login_password_too_short), "password");
                            }
                            if ( ! password1.equals(password2)){
                                throw new InvalidPasswordException(SignUpActivity.this.getString(R.string.login_password_different));
                            }
                            Intent intent = new Intent(SignUpActivity.this, SignUpActivity2.class);
                            intent.putExtra("First Name", nameField.getText().toString());
                            intent.putExtra("Name", surnameField.getText().toString());
                            intent.putExtra("Password", passwordField.getText().toString());
                            intent.putExtra("Login", loginField.getText().toString());
                            startActivity(intent);
                            }
                        catch(InvalidFieldException|InvalidPasswordException|ExistingLoginException e){
                            Toast t = Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                }
        );
    }
}