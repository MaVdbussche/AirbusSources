package vandenbussche.airbussources.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Supplier;

public class SignUpActivity extends AppCompatActivity {

    private RadioGroup businessUnits;
    private RadioGroup commodities;
    private RadioGroup roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.signup_screen_title);
        ab.show();

        businessUnits = (RadioGroup) findViewById(R.id.businessUnits);
        commodities = (RadioGroup) findViewById(R.id.commodities);
        roles = (RadioGroup) findViewById(R.id.roles);

        ArrayList<Supplier> listSuppliers = Supplier.getAllSuppliers();
        for (int i = 0; i < listSuppliers.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setText(listSuppliers.get(i).getName());
            businessUnits.addView(button);
        }
    }
}
