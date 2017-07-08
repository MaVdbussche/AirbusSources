package vandenbussche.airbussources.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Supplier;

public class SignUpActivity extends AppCompatActivity {

    private RadioGroup rgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        rgroup = (RadioGroup) findViewById(R.id.suppliers);

        ArrayList<Supplier> listSuppliers = Supplier.getAllSuppliers();
        for (int i = 0; i < listSuppliers.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setText(listSuppliers.get(i).getName());
            rgroup.addView(button);
        }
    }
}
