package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.database.SQLUtility;

public class SignUpActivityPageTwo extends AppCompatActivity {


    private Button toScreen3;

    private ScrollView businessUnitsScrollView;
    private ScrollView commoditiesScrollView;
    private ScrollView rolesScrollView;

    private RadioGroup businessUnits;
    private RadioGroup commodities;
    private RadioGroup roles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        final Intent inputIntent = getIntent();

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.signup_screen_title_2);
        ab.show();

        toScreen3 = (Button) findViewById(R.id.signupScreen2ButtonNext);

        businessUnitsScrollView = (ScrollView) findViewById(R.id.businessUnitsRadioGroupScrollView);
        commoditiesScrollView = (ScrollView) findViewById(R.id.commoditiesRadioGroupScrollView);
        rolesScrollView = (ScrollView) findViewById(R.id.rolesRadioGroupScrollView);

        businessUnits = (RadioGroup) findViewById(R.id.businessUnitsRadioGroup);
        commodities = (RadioGroup) findViewById(R.id.commoditiesRadioGroup);
        roles = (RadioGroup) findViewById(R.id.rolesRadioGroup);

        SQLUtility db = SQLUtility.prepareDataBase(this);
        final ArrayList<String> listBusinessUnits = db.getAllSuppliersNames();
        final ArrayList<String> listCommodities = db.getAllCommoditiesNames();
        final ArrayList<String> listRoles = db.getAllRolesNames();
        db.close();

        for (int i = 0; i < listBusinessUnits.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setText(listBusinessUnits.get(i));
            businessUnits.addView(button);
        }
        for (int i = 0; i < listCommodities.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setText(listCommodities.get(i));
            commodities.addView(button);
        }
        for (int i = 0; i < listRoles.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setText(listRoles.get(i));
            roles.addView(button);
        }

        toScreen3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioButton bu = (RadioButton) findViewById(businessUnits.getCheckedRadioButtonId());
                        RadioButton commodity = (RadioButton) findViewById(commodities.getCheckedRadioButtonId());
                        RadioButton role = (RadioButton) findViewById(roles.getCheckedRadioButtonId());
                        if( (businessUnits.getCheckedRadioButtonId()==-1) || (bu.getText().toString()).equals(getString(R.string.signup_businessUnits_default)) ){
                            Toast t = Toast.makeText(SignUpActivityPageTwo.this, "Please select a Business Unit", Toast.LENGTH_SHORT);
                            t.show();
                        } else if( (commodities.getCheckedRadioButtonId()==-1) || (commodity.getText().toString()).equals(getString(R.string.signup_commodities_default)) ){
                            Toast t = Toast.makeText(SignUpActivityPageTwo.this, "Please select a Commodity", Toast.LENGTH_SHORT);
                            t.show();
                        } else if( (roles.getCheckedRadioButtonId()==-1) || (role.getText().toString()).equals(getString(R.string.signup_roles_default)) ){
                            Toast t = Toast.makeText(SignUpActivityPageTwo.this, "Please select a Role", Toast.LENGTH_SHORT);
                            t.show();
                        } else {
                            Member m = Member.connectedMember;
                            Member.connectedMember = new Member(
                                    m.getLogin(),
                                    m.getPassword(),
                                    m.getFirstName(),
                                    m.getName(),
                                    bu.getText().toString(),
                                    commodity.getText().toString(),
                                    role.getText().toString()
                            );
                            Intent intent = new Intent(SignUpActivityPageTwo.this, SignUpActivityPageThree.class);

                            /**
                            if(inputIntent != null) {
                                String firstName = inputIntent.getStringExtra("First Name");
                                String name = inputIntent.getStringExtra("Name");
                                String password = inputIntent.getStringExtra("Password");
                                String login = inputIntent.getStringExtra("Login");

                                intent.putExtra("First Name", firstName);
                                intent.putExtra("Name", name);
                                intent.putExtra("Password", password);
                                intent.putExtra("Login", login);
                                intent.putExtra("Business Unit", bu.getText().toString());
                                intent.putExtra("Commodity", commodity.getText().toString());
                                intent.putExtra("Role", role.getText().toString());
                            } else {
                                System.out.println("Input Intent was null !!!!!!!!!!");
                            }
                             **/
                            startActivity(intent);
                        }
                    }
                }
        );
    }
}
