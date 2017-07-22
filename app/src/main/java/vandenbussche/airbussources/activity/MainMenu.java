package vandenbussche.airbussources.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;

public class MainMenu extends AppCompatActivity {

    private TextView screenTitle;
    private TextView greetings;
    private Button toMemberSearch;
    private Button toProductSearch;
    private Button toSupplierSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.mainMenu_screen_title);
        ab.show();

        screenTitle = (TextView) findViewById(R.id.welcomeTitle);
        screenTitle.setText("Welcome, "+ Member.connectedMember.getFirstName()+" "+Member.connectedMember.getName()+" !");
        greetings = (TextView) findViewById(R.id.buttonsTitle);


    }
}
