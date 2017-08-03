package vandenbussche.airbussources.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import vandenbussche.airbussources.R;

public class DetailsSupplier extends AppCompatActivity {

    private TextView screenTitle;
    private TextView productsTitle;
    private TextView membersTitle;

    private ListView listProducts;
    private ExpandableListView listMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_supplier);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.supplier_screenTitle);
        ab.show();

        screenTitle = (TextView) findViewById(R.id.supplierScreenTitle);
        productsTitle = (TextView) findViewById(R.id.supplierProductsTitle);
        membersTitle = (TextView) findViewById(R.id.supplierMembersTitle);

        listProducts = (ListView) findViewById(R.id.supplierProductsListView);
        listMembers = (ExpandableListView) findViewById(R.id.supplierMembersListView);

        //listProducts.setAdapter(null);//TODO
        //listMembers.setAdapter(null);//TODO (ExpandableListAdapter) (The idea is to group members by Business Unit)
    }
}
