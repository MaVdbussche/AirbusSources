package vandenbussche.airbussources.activity;


import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.database.SQLUtility;

public class SignUpActivity4 extends AppCompatActivity {

    private TextView titleColumn1;
    private TextView titleColumn3;
    private ListView listProductsToTick;
    private Button toConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.signup_screen_title_4);
        ab.show();

        titleColumn1 = (TextView) findViewById(R.id.signupScreen4_TitleColumn1);
        titleColumn3 = (TextView) findViewById(R.id.signupScreen4_TitleColumn3);
        listProductsToTick = (ListView) findViewById(R.id.signupScreen4_listProducts_toTick);
        toConfirm = (Button) findViewById(R.id.signupScreen4ButtonNext);

        displayAllProductsTickable();

        //ItemClickListener pour liste produits (voir SignUpActivity3)
        //TODO

        toConfirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Member m = Member.connectedMember;
                            Member.connectedMember = new Member(SignUpActivity4.this, m.getLogin(), m.getPassword(), m.getFirstName(),
                                    m.getName(), m.getBu(), m.getCommodity(), m.getRole(), m.getSuppliers());
                        } catch (SQLiteException e){
                            throw new SQLiteException(e.getMessage());
                        }
                    }
                }
        );
        /**
         *
          *
    try {
        Member.connectedMember = new Member(SignUpActivity.this, loginField.getText().toString(), passwordField.getText().toString(), password2Field.getText().toString(),
                nameField.getText().toString(), surnameField.getText().toString(), bu.getText().toString(), commodity.getText().toString(),
                role.getText().toString());
        Intent intent = new Intent(SignUpActivity.this, SignUpActivity3.class);
        startActivity(intent);
    } catch (Exception e){
        Toast t = Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
        t.show();
    }
         */
    }

    private void displayAllProductsTickable(){

        SQLUtility db = SQLUtility.prepareDataBase(SignUpActivity4.this);
        ArrayList<String> namesList = db.getAllProductsNames();
        db.close();
        Collections.sort(namesList);
        ArrayList<Product> productsList = new ArrayList<>(namesList.size());
        for(int i=0; i<namesList.size(); i++){
            productsList.add(new Product(SignUpActivity4.this, namesList.get(i), false));
        }
        RowAdapterProducts adapter = new RowAdapterProducts(SignUpActivity4.this, productsList, Member.connectedMember);
        listProductsToTick.setAdapter(adapter);
    }
}
