package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    private Button toEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.mainMenu_screen_title);
            ab.show();
        }

        screenTitle = (TextView) findViewById(R.id.welcomeTitle);
        screenTitle.setText("Welcome, "+ Member.connectedMember.getFirstName()+" "+Member.connectedMember.getName()+" !");
        greetings = (TextView) findViewById(R.id.buttonsTitle);

        toMemberSearch = (Button) findViewById(R.id.buttonSearchByMember);
        toProductSearch = (Button) findViewById(R.id.buttonSearchByProduct);
        toSupplierSearch = (Button) findViewById(R.id.buttonSearchBySupplier);
        toEditProfile = (Button) findViewById(R.id.buttonEditProfile);

        toMemberSearch.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(MainMenu.this, SearchByMember.class);
                        startActivity(intent);
                    }
                }
        );
        toProductSearch.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(MainMenu.this, SearchByProduct.class);
                        startActivity(intent);
                    }
                }
        );
        toSupplierSearch.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(MainMenu.this, SearchBySupplier.class);
                        startActivity(intent);
                    }
                }
        );
        toEditProfile.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(MainMenu.this, EditProfile.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
