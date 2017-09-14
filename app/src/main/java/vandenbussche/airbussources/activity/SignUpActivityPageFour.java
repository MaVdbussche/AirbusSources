package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import vandenbussche.airbussources.BuildConfig;
import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.core.Supplier;
import vandenbussche.airbussources.database.SQLUtility;

public class SignUpActivityPageFour extends AppCompatActivity {

    Intent inputIntent;

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

        //inputIntent = getIntent();

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

        for (String supplier : listDataHeader) {
            tickedProducts.put(supplier, new ArrayList<Product>());
        }

        listProductsToTick.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                System.out.println("Click !");
                //String of the newly ticked product
                Product addedProduct = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                /**DEBUG CODE**/
                if(BuildConfig.DEBUG && !(addedProduct.getName().equals(((CheckedTextView) v).getText()))){
                    throw new AssertionError("Received HashMap not corresponding to text in CheckedTextView !\n");
                }

                if(v instanceof CheckedTextView && cfts[groupPosition][childPosition] == 0){
                    System.out.println("Clicked column 2, column 3 is empty");

                    if( ((CheckedTextView) v).isChecked() ){
                        tickedProducts.get(listDataHeader.get(groupPosition)).add(addedProduct);
                    } else {
                        tickedProducts.get(listDataHeader.get(groupPosition)).remove(addedProduct);
                    }
                }else if(v instanceof  CheckedTextView && cfts[groupPosition][childPosition] == 1){
                    System.out.println("Clicked column 2, column 3 is ticked");
                    if( ((CheckedTextView) v).isChecked() ){
                        System.err.println("How could this happen ? (line "+(new Exception().getStackTrace()[0].getLineNumber())+")");
                    } else {
                        tickedProducts.get(listDataHeader.get(groupPosition)).remove(addedProduct);
                        CheckBox column3 = (CheckBox) parent.getSelectedView().findViewById(R.id.rowItemCheckTablesCheckBox);
                        column3.setChecked(false);
                        cfts[groupPosition][childPosition] = 0;
                    }
                } else if(v instanceof CheckBox && cfts[groupPosition][childPosition] == 1){
                    //That means it is getting "un-ticked"
                    //Thus column 2 was ticked for sure
                    System.out.println("Clicked column 3, column 2 is ticked");
                    String supplierName = listDataHeader.get(groupPosition);
                    Product currentProduct = listDataChild.get(supplierName).get(childPosition);
                    currentProduct.setCFT(false);
                    cfts[groupPosition][childPosition] = 0;
                } else if(v instanceof CheckBox && cfts [groupPosition][childPosition] == 0){
                    //Here, column 3 got checked while column 2 is undetermined; we have to check both
                    System.out.println("Clicked column 3, column 2 empty or not");
                    CheckedTextView column2 = (CheckedTextView) parent.getSelectedView().findViewById(R.id.rowItemCheckTablesItemNameCheckedTextView);
                    column2.setChecked(true);
                    String supplierName = listDataHeader.get(groupPosition);
                    Product currentProduct = listDataChild.get(supplierName).get(childPosition);
                    currentProduct.setCFT(true);
                    cfts[groupPosition][childPosition] = 1;
                }
                return false;
            }
        });
        //ItemClickListener pour liste produits (voir SignUpActivityPageThree)
        //TODO Does it work ? (re-check logic conditions)

        toConfirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Member m = Member.connectedMember;
                            ArrayList<Supplier> newSuppliers = m.getSuppliers();
                            //We fill this ArrayList with the Suppliers+Products from this activity
                            for (Supplier supplier : newSuppliers) {
                                supplier.setProducts(new ArrayList<>(tickedProducts.get(supplier.getName())));
                            }
                            m.setSuppliers(newSuppliers);

                            Member.connectedMember = new Member(SignUpActivityPageFour.this, m.getLogin(), m.getPassword(), m.getFirstName(),
                                    m.getName(), m.getBu(), m.getCommodity(), m.getRole(), m.getSuppliers());
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
        listDataHeader.addAll(db.getAllMembersSuppliersNames(Member.connectedMember.getLogin()));
        for (String headerTitle : listDataHeader) {
            List<Product> list = db.getAllSuppliersProducts(headerTitle);
            Collections.sort(list);
            listDataChild.put(headerTitle, list);
        }
        cfts = new int[db.getAllSuppliersNames().size()][db.getAllProductsNames().size()];

        db.close();
        Collections.sort(listDataHeader);

        ExpandableListAdapter adapter = new ExpandableListAdapter(SignUpActivityPageFour.this, listDataHeader, listDataChild);
        listProductsToTick.setAdapter(adapter);
    }
}
