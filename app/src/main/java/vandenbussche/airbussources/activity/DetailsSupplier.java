package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Nameable;
import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.core.Supplier;
import vandenbussche.airbussources.database.SQLUtility;

public class DetailsSupplier extends AppCompatActivity {

    private TextView screenTitle;
    private TextView productsTitle;
    private TextView membersTitle;

    private ListView listProducts;
    private ListView listMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_supplier);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.supplier_screenTitle);
            ab.show();
        }

        Intent inputIntent = getIntent();
        String supplierName = inputIntent.getStringExtra("Supplier");

        screenTitle = (TextView) findViewById(R.id.supplierScreenTitle);
        productsTitle = (TextView) findViewById(R.id.supplierProductsTitle);
        membersTitle = (TextView) findViewById(R.id.supplierMembersTitle);

        listProducts = (ListView) findViewById(R.id.supplierProductsListView);
        listMembers = (ListView) findViewById(R.id.supplierMembersListView);

        screenTitle.setText(supplierName);

        SQLUtility db = SQLUtility.prepareDataBase(DetailsSupplier.this);
        List<Product> products = db.getAllSuppliersProducts(supplierName);
        List<Nameable> productsAsNameables = new ArrayList<Nameable>(products);

        RowAdapterResearchResults adapterProducts = new RowAdapterResearchResults(DetailsSupplier.this, productsAsNameables);
        listProducts.setAdapter(adapterProducts);

        ArrayList<String> membersIDProfiles = db.getAllSuppliersMembersIDProfiles(supplierName);
        ArrayList<Member> members = new ArrayList<>();
        for (String idProfile : membersIDProfiles) {
            members.add(new Member(DetailsSupplier.this, idProfile));
        }
        db.close();
        RowAdapterMembers adapterMembers = new RowAdapterMembers(DetailsSupplier.this, members, null, new Supplier(supplierName, null));
        listMembers.setAdapter(adapterMembers);
    }
}
