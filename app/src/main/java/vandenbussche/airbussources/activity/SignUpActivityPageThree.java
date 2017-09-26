package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Supplier;
import vandenbussche.airbussources.database.SQLUtility;

public class SignUpActivityPageThree extends AppCompatActivity {

    private TextView titleColumn1;
    private TextView titleColumn3;
    private ListView listSuppliersToTick;
    private Button toScreen4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.signup_screen_title_3);
            ab.show();
        }

        titleColumn1 = (TextView) findViewById(R.id.signupScreen3_Suppliers_TitleColumn1);
        titleColumn3 = (TextView) findViewById(R.id.signupScreen3_Suppliers_TitleColumn3);
        listSuppliersToTick = (ListView) findViewById(R.id.signupScreen3_listSuppliers_toTick);
        toScreen4 = (Button) findViewById(R.id.signupScreen3ButtonNext);

        displayAllSuppliersTickable();

        toScreen4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUpActivityPageThree.this, SignUpActivityPageFour.class);
                //Member.connectedMember = inputMember;
                startActivity(intent);
            }
        });
    }

    private void displayAllSuppliersTickable(){

        SQLUtility db = SQLUtility.prepareDataBase(SignUpActivityPageThree.this);
        ArrayList<String> namesList = db.getAllSuppliersNames();
        db.close();
        Collections.sort(namesList);
        ArrayList<Supplier> suppliersList = new ArrayList<>(namesList.size());
        for(int i=0; i<namesList.size(); i++){
            suppliersList.add(new Supplier(namesList.get(i), null, false));
        }
        RowAdapterSuppliers adapter = new RowAdapterSuppliers(SignUpActivityPageThree.this, suppliersList);
        listSuppliersToTick.setAdapter(adapter);
    }
}
