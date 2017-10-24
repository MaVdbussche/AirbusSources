package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.core.Supplier;
import vandenbussche.airbussources.database.SQLUtility;

public class SignUpActivityPageFour extends AppCompatActivity {

    private TextView titleColumn1;
    private TextView titleColumn3;
    private ExpandableListView listProductsToTick;
    private List<String> listDataHeader;
    private HashMap<String, List<Product>> listDataChild;
    private Button toConfirm;

    private HashMap<String, List<Product>> tickedProducts = new HashMap<>();   //Supplier name, Products list
    private int[][] cfts;   //[groupPosition][childPosition]

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup4);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.signup_screen_title_4);
            ab.show();
        }

        titleColumn1 = (TextView) findViewById(R.id.signupScreen4_TitleColumn1);
        titleColumn3 = (TextView) findViewById(R.id.signupScreen4_TitleColumn3);
        listProductsToTick = (ExpandableListView) findViewById(R.id.signupScreen4_ExpListView);
        toConfirm = (Button) findViewById(R.id.signupScreen4ButtonNext);

        displayAllProductsTickable();

        toConfirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Member m = Member.connectedMember;
                            Member.connectedMember = new Member(SignUpActivityPageFour.this, m.getLogin(), m.getPassword(), m.getFirstName(), m.getName(), m.getBu(), m.getRole(), m.getSuppliers());
                            Intent intent = new Intent(SignUpActivityPageFour.this, MainMenu.class);
                            startActivity(intent);
                        } catch (SQLiteException e){
                            throw new SQLiteException(e.getMessage());
                        }
                    }
                }
        );
    }

    private void displayAllProductsTickable(){

        SQLUtility db = SQLUtility.prepareDataBase(SignUpActivityPageFour.this);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Product>>();

        if(Member.connectedMember.getSuppliers() != null) {
            for (Supplier supplier : Member.connectedMember.getSuppliers()) {
                listDataHeader.add(supplier.getName());
            }
        }
        //listDataHeader.addAll(db.getAllMembersSuppliersNames(Member.connectedMember.getLogin()));
        Collections.sort(listDataHeader);
        for (String headerTitle : listDataHeader) {
            List<Product> list = db.getAllSuppliersProducts(headerTitle);
            Collections.sort(list);
            listDataChild.put(headerTitle, list);
        }
        //cfts = new int[db.getAllSuppliersNames().size()][db.getAllProductsNames().size()];

        db.close();

        ExpandableListAdapterProductsTickable adapter = new ExpandableListAdapterProductsTickable(SignUpActivityPageFour.this, listDataHeader, listDataChild);
        listProductsToTick.setAdapter(adapter);
    }
}
