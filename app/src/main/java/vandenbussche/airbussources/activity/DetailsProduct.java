package vandenbussche.airbussources.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import vandenbussche.airbussources.R;

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
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.product_screenTitle);
        ab.show();

        screenTitle = (TextView) findViewById(R.id.productScreenTitle);
        suppliersTitle = (TextView) findViewById(R.id.productSupppliersTitle);
        membersTitle = (TextView) findViewById(R.id.productMembersTitle);

        listSuppliers = (ListView) findViewById(R.id.productSuppliersListView);
        listMembers = (ExpandableListView) findViewById(R.id.productMembersListView);

        listSuppliers.setAdapter(null);//TODO
        listMembers.setAdapter(null);//TODO (ExpandableListAdapter) (The idea is to group members by Business Unit)
    }
}
