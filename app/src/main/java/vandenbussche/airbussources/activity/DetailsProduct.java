package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Nameable;
import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.core.Supplier;
import vandenbussche.airbussources.database.SQLUtility;

public class DetailsProduct extends AppCompatActivity {

    private TextView screenTitle;
    private TextView suppliersTitle;
    private TextView membersTitle;

    private ListView listSuppliers;
    private ExpandableListView listMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.product_screenTitle);
            ab.show();
        }

        Intent inputIntent = getIntent();
        String productName = inputIntent.getStringExtra("Product");

        screenTitle = (TextView) findViewById(R.id.productScreenTitle);
        suppliersTitle = (TextView) findViewById(R.id.productSupppliersTitle);
        membersTitle = (TextView) findViewById(R.id.productMembersTitle);

        listSuppliers = (ListView) findViewById(R.id.productSuppliersListView);
        listMembers = (ExpandableListView) findViewById(R.id.productMembersListView);

        screenTitle.setText(productName);

        SQLUtility db = SQLUtility.prepareDataBase(DetailsProduct.this);
        List<String> relevantSupplierNames = db.getAllProductsSuppliersNames(productName);
        Collections.sort(relevantSupplierNames);
        List<Nameable> relevantSuppliers = new ArrayList<>();
        for (String name : relevantSupplierNames){
            relevantSuppliers.add(new Supplier(name, null));
        }
        RowAdapterResearchResults adapterSuppliers = new RowAdapterResearchResults(DetailsProduct.this, relevantSuppliers);
        listSuppliers.setAdapter(adapterSuppliers);

        List<String> listDataHeader = relevantSupplierNames;
        HashMap<String, List<Member>> listDataChild = new HashMap<>();
        for(int i=0; i<relevantSupplierNames.size(); i++){
            ArrayList<String> relevantMembersIDProfiles = db.getAllSuppliersMembersIDProfiles(relevantSupplierNames.get(i));
            ArrayList<Member> relevantMembers = new ArrayList<>();
            for (String idProfile : relevantMembersIDProfiles) {
                relevantMembers.add(new Member(DetailsProduct.this, idProfile));
            }
            listDataChild.put(relevantSupplierNames.get(i), relevantMembers);
        }
        db.close();
        ExpandableListAdapterMembers adapterMembers = new ExpandableListAdapterMembers(DetailsProduct.this, listDataHeader, listDataChild, new Product(productName));
        listMembers.setAdapter(adapterMembers);
    }
}
