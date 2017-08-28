package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Supplier;
import vandenbussche.airbussources.database.SQLUtility;

public class SignUpActivity3 extends AppCompatActivity {

    private TextView titleColumn1;
    private TextView titleColumn3;
    private ListView listSuppliersToTick;
    private Button toScreen4;

    Intent inputIntent =getIntent();

    Member inputMember = new Member(inputIntent.getStringExtra("Login"), inputIntent.getStringExtra("Password"),
            inputIntent.getStringExtra("First Name"), inputIntent.getStringExtra("Name"), inputIntent.getStringExtra("Business Unit"),
            inputIntent.getStringExtra("Commodity"), inputIntent.getStringExtra("Role"));

    ArrayList<String> tickedSuppliersNames = new ArrayList<>();
    ArrayList<Integer> tickedNegotiations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.signup_screen_title_3);
        ab.show();

        titleColumn1 = (TextView) findViewById(R.id.signupScreen3_Suppliers_TitleColumn1);
        titleColumn3 = (TextView) findViewById(R.id.signupScreen3_Suppliers_TitleColumn3);
        listSuppliersToTick = (ListView) findViewById(R.id.signupScreen3_listSuppliers_toTick);
        toScreen4 = (Button) findViewById(R.id.signupScreen3ButtonNext);

        displayAllSuppliersTickable();

        listSuppliersToTick.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                System.out.println("Click !");
                if(view instanceof CheckedTextView && tickedNegotiations.get(position) == 0) {
                    //If one ticks column 2 and column 3 is NOT checked
                    System.out.println("Click ! (column 2, column 3 is NOT checked yet");
                    if (((CheckedTextView) view).isChecked()) {
                        //If it is being "checked on"
                        ((CheckedTextView) view).setChecked(true);
                        tickedSuppliersNames.set(position, ((CheckedTextView) view).getText().toString());
                        tickedNegotiations.set(position, 0);
                    } else {
                        //If it is being "checked off"
                        ((CheckedTextView) view).setChecked(false);
                        tickedSuppliersNames.set(position, null);
                        tickedNegotiations.set(position, 0);
                    }
                } else if (view instanceof CheckedTextView && tickedNegotiations.get(position) != 0){
                    //If one ticks column 2 and column 3 IS already checked
                    System.out.println("Click ! (column 2, column 3 IS already checked");
                    if (((CheckedTextView) view).isChecked()) {
                        //If it is being "checked on"
                        System.out.println("How could this happen ?");
                    } else {
                        //If it is being "checked off"
                        ((CheckedTextView) view).setChecked(false);
                        CheckBox checkBox = (CheckBox) parent.getSelectedView().findViewById(R.id.rowItemCheckTablesCheckBox);
                        checkBox.setChecked(false);
                        tickedSuppliersNames.set(position, null);
                        tickedNegotiations.set(position, 0);
                    }
                } else if(view instanceof CheckBox && tickedSuppliersNames.get(position) != null){
                    //If one ticks column 3 and column 2 is already ticked
                    System.out.println("Click ! (column 3, column 2 is already ticked on)");
                    if(((CheckBox) view).isChecked()){
                        //If it is being "checked on"
                        tickedNegotiations.set(position, 1);
                    } else {
                        //If it is being "checked off"
                        tickedNegotiations.set(position, 0);
                    }
                } else if(view instanceof CheckBox && tickedSuppliersNames.get(position) == null){
                    //If one ticks column 3 while column 2 is NOT ticked
                    System.out.println("Click ! (column 3, column 2 is NOT ticked on yet)");
                    if(((CheckBox) view).isChecked()){
                        //If it is being "checked on"
                        CheckedTextView checkedTextView = (CheckedTextView) parent.getSelectedView().findViewById(R.id.rowItemCheckTablesItemNameCheckedTextView);
                        checkedTextView.setChecked(true);
                        tickedSuppliersNames.set(position, checkedTextView.getText().toString());
                        tickedNegotiations.set(position, 1);
                    } else {
                        //If it is being "checked off"
                        System.out.println("How could this happen -?");
                        tickedNegotiations.set(position, 0);
                    }
                }
            }
        });

        toScreen4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Supplier> selectedSuppliers = new ArrayList<>();
                        for(int i=0; i<tickedSuppliersNames.size(); i++){
                            selectedSuppliers.add(new Supplier(tickedSuppliersNames.get(i), null, (tickedNegotiations.get(i)==1)));
                        }
                        inputMember.setSuppliers(selectedSuppliers);

                        Intent intent = new Intent(SignUpActivity3.this, SignUpActivity4.class);
                        Member.connectedMember = new Member(inputMember.getLogin(), inputMember.getPassword(),
                                inputMember.getFirstName(), inputMember.getName(), inputMember.getBu(), inputMember.getCommodity(),
                                inputMember.getRole(), inputMember.getSuppliers());
                        startActivity(intent);
                    }
                }
        );
    }

    private void displayAllSuppliersTickable(){

        SQLUtility db = SQLUtility.prepareDataBase(SignUpActivity3.this);
        ArrayList<String> namesList = db.getAllSuppliersNames();
        ArrayList<Supplier> suppliersList = new ArrayList<>(namesList.size());
        for(int i=0; i<namesList.size(); i++){
            suppliersList.add(new Supplier(namesList.get(i), null, false));
        }
        RowAdapterSuppliers adapter = new RowAdapterSuppliers(SignUpActivity3.this, suppliersList, inputMember);
        listSuppliersToTick.setAdapter(adapter);
    }
}
