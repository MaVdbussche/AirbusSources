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
import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.core.Supplier;
import vandenbussche.airbussources.database.SQLUtility;

public class DetailsMember extends AppCompatActivity {

    private TextView screenTitle;
    private TextView details;
    private TextView productsTitle;
    private TextView suppliersTitle;

    private ExpandableListView listProducts;
    private ListView listSuppliers;

    private Member currentMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_member);

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
        listProducts = (ExpandableListView) findViewById(R.id.memberProductsListView);
        listSuppliers = (ListView) findViewById(R.id.memberSuppliersListView);

        Intent inputIntent = getIntent();
        String idProfile = inputIntent.getStringExtra("Member");
        SQLUtility db = SQLUtility.prepareDataBase(DetailsMember.this);
        ArrayList<String> basicInfo = db.getMemberBasicInfo(idProfile);//{IDProfile, password, name, surname, role, commodity, business unit}

        currentMember = new Member(idProfile,
                                    basicInfo.get(1),
                                    basicInfo.get(2),
                                    basicInfo.get(3),
                                    basicInfo.get(6),
                                    basicInfo.get(5),
                                    basicInfo.get(4));
        String fullName = currentMember.getFirstName()+" "+currentMember.getName();
        String fullDetails = currentMember.getBu()+" - "+currentMember.getCommodity()+" - "+currentMember.getRole();

        screenTitle.setText(fullName);
        details.setText(fullDetails);

        //Suppliers list
        ArrayList<String> suppliersNamesList = db.getAllMembersSuppliersNames(idProfile);
        Collections.sort(suppliersNamesList);
        ArrayList<Supplier> suppliersList = new ArrayList<Supplier>(suppliersNamesList.size());
        for(int i=0; i<suppliersNamesList.size(); i++){
            suppliersList.add(new Supplier(suppliersNamesList.get(i), null, db.getNegotiationState(idProfile, suppliersNamesList.get(i))));
        }
        RowAdapterSuppliers adapter = new RowAdapterSuppliers(DetailsMember.this, suppliersList, currentMember);
        listSuppliers.setAdapter(adapter);

        //Products list
        ArrayList<String> listDataHeader = new ArrayList<>();
        listDataHeader.addAll(suppliersNamesList);
        HashMap<String, List<Product>> listDataChild = new HashMap<>();
        for(int i=0; i<listDataHeader.size(); i++){
            listDataChild.put(suppliersNamesList.get(i), db.getRelevantSuppliersProducts(idProfile, suppliersNamesList.get(i)));
        }
        ExpandableListAdapterProducts expandableListAdapterProducts = new ExpandableListAdapterProducts(DetailsMember.this, listDataHeader, listDataChild);
        listProducts.setAdapter(expandableListAdapterProducts);
        db.close();
    }
}
