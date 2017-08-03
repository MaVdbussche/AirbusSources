package vandenbussche.airbussources.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import vandenbussche.airbussources.R;

public class DetailsMember extends AppCompatActivity {

    private TextView screenTitle;
    private TextView details;
    private TextView productsTitle;
    private TextView suppliersTitle;

    private ListView listProducts;
    private ListView listSuppliers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_member);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.member_screenTitle);
        ab.show();

        screenTitle = (TextView) findViewById(R.id.memberScreenTitle);
        details = (TextView) findViewById(R.id.memberDetails);
        productsTitle = (TextView) findViewById(R.id.memberProductsTitle);
        suppliersTitle = (TextView) findViewById(R.id.memberSuppliersTitle);

        listProducts = (ListView) findViewById(R.id.memberProductsListView);
        listSuppliers = (ListView) findViewById(R.id.memberSuppliersListView);

        //listProducts.setAdapter(null);//TODO
        //listSuppliers.setAdapter(null);//TODO
    }
}
