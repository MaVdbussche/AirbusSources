package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Supplier;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameField;
    private EditText surnameField;
    private EditText loginField;
    private EditText passwordField;
    private EditText password2Field;

    private RadioGroup businessUnits;
    private RadioGroup commodities;
    private RadioGroup roles;

    private Button toProductsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.signup_screen_title);
        ab.show();

        nameField = (EditText) findViewById(R.id.name_field);
        surnameField = (EditText) findViewById(R.id.surname_field);
        loginField = (EditText) findViewById(R.id.login_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        password2Field = (EditText) findViewById(R.id.password_field2);

        businessUnits = (RadioGroup) findViewById(R.id.businessUnits);
        commodities = (RadioGroup) findViewById(R.id.commodities);
        roles = (RadioGroup) findViewById(R.id.roles);

        toProductsButton = (Button) findViewById(R.id.signup_button_toProductsScreen);

        ArrayList<Supplier> listSuppliers = Supplier.getAllSuppliers();
        for (int i = 0; i < listSuppliers.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setText(listSuppliers.get(i).getName());
            businessUnits.addView(button);
        }

        toProductsButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(businessUnits.getCheckedRadioButtonId() == -1){
                            Toast t = Toast.makeText(SignUpActivity.this, "Please select a Business Unit", Toast.LENGTH_SHORT);
                            t.show();
                        }
                        if(commodities.getCheckedRadioButtonId() == -1){
                            Toast t = Toast.makeText(SignUpActivity.this, "Please select a Commodity", Toast.LENGTH_SHORT);
                            t.show();
                        }if(roles.getCheckedRadioButtonId() == -1){
                            Toast t = Toast.makeText(SignUpActivity.this, "Please select a Role", Toast.LENGTH_SHORT);
                            t.show();
                        }
                        RadioButton bu = (RadioButton) findViewById(businessUnits.getCheckedRadioButtonId());
                        RadioButton commodity = (RadioButton) findViewById(commodities.getCheckedRadioButtonId());
                        RadioButton role = (RadioButton) findViewById(roles.getCheckedRadioButtonId());
                        try {
                            Member.connectedMember = new Member(SignUpActivity.this, loginField.getText().toString(), passwordField.getText().toString(), password2Field.getText().toString(),
                                                                nameField.getText().toString(), surnameField.getText().toString(), bu.getText().toString(), commodity.getText().toString(),
                                                                role.getText().toString());
                            Intent intent = new Intent(SignUpActivity.this, SignUpActivity2.class);
                            startActivity(intent);
                        } catch (Exception e){
                            Toast t = Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                }
        );
    }
}