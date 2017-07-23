package vandenbussche.airbussources.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import vandenbussche.airbussources.R;

public class EditProfile extends AppCompatActivity {

    private TextView nameText;
    private TextView memberBU;
    private TextView memberCommodity;
    private TextView productsTitle;
    private TextView suppliersTitle;
    private Button editName;
    private Button editBU;
    private Button editCommodity;
    private Button editProducts;
    private Button editSuppliers;
    private ListView listProducts;
    private ListView listSuppliers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.editprofile_screen_title);
        ab.show();

        nameText = (TextView) findViewById(R.id.editMemberScreenTitle);
        memberBU = (TextView) findViewById(R.id.editMemberDetailsBU);
        memberCommodity = (TextView) findViewById(R.id.editMemberDetailsCommodity);
        productsTitle = (TextView) findViewById(R.id.editMemberProductsTitle);
        suppliersTitle = (TextView) findViewById(R.id.editMemberSuppliersTitle);

        editName = (Button) findViewById(R.id.buttonEditMemberName);
        editBU = (Button) findViewById(R.id.buttonEditMemberBU);
        editCommodity = (Button) findViewById(R.id.buttonEditMemberCommodity);
        editProducts = (Button) findViewById(R.id.buttonEditProducts);
        editSuppliers = (Button) findViewById(R.id.buttonEditSuppliers);

        listProducts = (ListView) findViewById(R.id.editMemberProductsListView);
        //TODO
        listSuppliers = (ListView) findViewById(R.id.editMemberSuppliersListView);
        //TODO

        editName.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //TODO
                    }
                }
        );
        editBU.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //TODO
                    }
                }
        );
        editCommodity.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //TODO
                    }
                }
        );
        editProducts.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //TODO
                    }
                }
        );
        editSuppliers.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //TODO
                    }
                }
        );
    }
}
