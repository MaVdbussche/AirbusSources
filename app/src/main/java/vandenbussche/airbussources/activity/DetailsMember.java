package vandenbussche.airbussources.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.core.Supplier;
import vandenbussche.airbussources.database.SQLUtility;

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

        Intent inputIntent = getIntent();
        String idProfile = inputIntent.getStringExtra("Member");

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.member_screenTitle);
            ab.show();
        }

        screenTitle = (TextView) findViewById(R.id.memberScreenTitle);
        details = (TextView) findViewById(R.id.memberDetails);
        productsTitle = (TextView) findViewById(R.id.memberProductsTitle);
        suppliersTitle = (TextView) findViewById(R.id.memberSuppliersTitle);
        listProducts = (ListView) findViewById(R.id.memberProductsListView);
        listSuppliers = (ListView) findViewById(R.id.memberSuppliersListView);

        SQLUtility db = SQLUtility.prepareDataBase(DetailsMember.this);
        ArrayList<String> basicInfo = db.getMemberBasicInfo(idProfile);//{IDProfile, password, name, surname, role, commodity, business unit}
        String fullName = basicInfo.get(2) + basicInfo.get(3);
        String fullDetails = basicInfo.get(6)+"-"+basicInfo.get(5)+"-"+basicInfo.get(4);

        ArrayList<String> namesList = db.getAllSuppliersNames();

        Collections.sort(namesList);

        ArrayList<Supplier> suppliersList = new ArrayList<>(namesList.size());
        for(int i=0; i<namesList.size(); i++){
            suppliersList.add(new Supplier(namesList.get(i), null, false));
        }
        RowAdapterSuppliers adapter = new RowAdapterSuppliers(DetailsMember.this, suppliersList);
        listSuppliers.setAdapter(adapter);
        screenTitle.setText(fullName);
        details.setText(fullDetails);

        ArrayList<String> listDataHeader = new ArrayList<>();
        listDataHeader.addAll(db.getAllSuppliersNames());   //TODO FAUX, on veut que les concernés)
        listDataHeader.trimToSize();
        HashMap<String, List<Product>> listDataChild = new HashMap<>();
        //TODO Ajouter le contenu du dataChild (Uniquement les concernés)

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(DetailsMember.this, listDataHeader, listDataChild);

        //listProducts.setAdapter(null);//TODO
        db.close();
    }
}
