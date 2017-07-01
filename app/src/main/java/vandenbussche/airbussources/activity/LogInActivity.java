package vandenbussche.airbussources.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.exception.InvalidFieldException;
import vandenbussche.airbussources.exception.InvalidPasswordException;

public class LogInActivity extends AppCompatActivity {

    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.start_button_login);
            ab.show();
        }

        final EditText login = (EditText) findViewById(R.id.login_field);
        final EditText password = (EditText) findViewById(R.id.password_field);


        logInButton = (Button) findViewById(R.id.button_login);

        login.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        Context appcontext = getApplicationContext();

                        try {
                            Member.connectedMember = new Member(appcontext, login.getText().toString(), password.getText().toString());
                            Intent intent = new Intent(Loginscreen.this, StartMenu.class);
                            startActivity(intent);
                        } catch (InvalidPasswordException | InvalidFieldException | UnknownUserException e){
                            Toast t = Toast.makeText(appcontext, e.getMessage(), Toast.LENGTH_SHORT);
                            t.show();

                        }
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
