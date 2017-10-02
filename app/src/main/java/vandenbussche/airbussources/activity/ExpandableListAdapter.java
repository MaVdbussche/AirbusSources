package vandenbussche.airbussources.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.util.SparseArrayCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
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

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // header titles
    // child data in format : header title, child title
    private HashMap<String, List<Product>> listDataChild;

    //private boolean[] dataStorageProduct;
    //private boolean[] dataStorageCFT;

    private SparseArray<boolean[]> dataStorageProduct = new SparseArray<>();
    private SparseArray<boolean[]> dataStorageCFT = new SparseArray<>();

    private HashMap<Integer, boolean[]> mapDataStorageProduct = new HashMap<>();
    private HashMap<Integer, boolean[]> mapDataStorageCFT = new HashMap<>();

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Product>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        for(String supplier : listDataHeader) {
            updateDataStorage(supplier, listDataHeader.indexOf(supplier));
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        final Supplier currentSupplier = Member.connectedMember.getSuppliers().get(groupPosition);

        //updateDataStorage(listDataHeader.get(groupPosition), groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_check_tables, parent, false);
        }
        //viewHolder = (ViewHolder) convertView.getTag();
        //if (viewHolder == null) //If this view has never been instantiated before
        //{
        viewHolder = new ViewHolder();
        //viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
        viewHolder.name = (CheckedTextView) convertView.findViewById(R.id.rowItemCheckTablesItemNameCheckedTextView);
        viewHolder.column3CheckBox = (CheckBox) convertView.findViewById(R.id.rowItemCheckTablesCheckBox);
        //viewHolder.details = (TextView) convertView.findViewById(R.id.rowItemResearchResultsTextViewDetails);
        //viewHolder.nbr = (TextView) convertView.findViewById(R.id.textView4);
        //viewHolder.time = (TextView) convertView.findViewById(R.id.textView5);
        convertView.setTag(viewHolder);
        //}
        //Gives the relevant values to the layout Views
        final Product element = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
        if(element != null) {
            String id = element.getIdentifier();
            viewHolder.name.setText(id);
            viewHolder.name.setChecked(mapDataStorageProduct.get(groupPosition)[childPosition]);
            viewHolder.column3CheckBox.setChecked(mapDataStorageCFT.get(groupPosition)[childPosition]);
        }
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                System.out.println("Click !");
                if(viewHolder.name.isChecked()){
                    //Ticking OFF column 2
                    viewHolder.name.setChecked(false);
                    viewHolder.column3CheckBox.setChecked(false);
                    mapDataStorageProduct.get(groupPosition)[childPosition] = false;
                    mapDataStorageCFT.get(groupPosition)[childPosition] = false;

                    if(listDataChild.get(listDataHeader.get(groupPosition)).size() > 0)    //If there are Products associated with this Supplier in the HashMap
                    {
                        ArrayList<Product> currentProducts = currentSupplier.getProducts();
                        currentProducts = removeFromName(currentProducts, viewHolder.name.getText().toString());
                        currentSupplier.setProducts(currentProducts);                                        //We update the connectedMember products
                        Member.connectedMember.getSuppliers().set(groupPosition, currentSupplier);
                    } else {
                        System.err.println("How could this happen ? (line "+(new Exception().getStackTrace()[0].getLineNumber())+")");
                        System.err.println("Ticking off from a null Products list");
                    }
                } else if ( ! viewHolder.name.isChecked()) {
                    //Ticking ON column 2
                    viewHolder.name.setChecked(true);
                    viewHolder.column3CheckBox.setChecked(false);   //Since it could not be on yet anyways
                    mapDataStorageProduct.get(groupPosition)[childPosition] = true;
                    mapDataStorageCFT.get(groupPosition)[childPosition] = false;

                    if(currentSupplier.getProducts() == null){
                        currentSupplier.setProducts( new ArrayList<Product>() );
                    }
                    currentSupplier.getProducts().add( new Product(viewHolder.name.getText().toString(), false) );
                    Collections.sort(currentSupplier.getProducts());
                    Member.connectedMember.getSuppliers().set(groupPosition, currentSupplier);
                }
            }
        });

        viewHolder.column3CheckBox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if(viewHolder.column3CheckBox.isChecked()){
                    //Ticking ON column 3
                    if(viewHolder.name.isChecked()){
                        //Ticking ON column 3, column 2 was ON
                        mapDataStorageProduct.get(groupPosition)[childPosition] = true;
                        mapDataStorageCFT.get(groupPosition)[childPosition] = true;

                        ArrayList<Product> currentProducts = currentSupplier.getProducts();
                        currentProducts = alterFromName(currentProducts, viewHolder.name.getText().toString(), true);
                        currentSupplier.setProducts(currentProducts);
                        Member.connectedMember.getSuppliers().set(groupPosition, currentSupplier);
                        System.out.println("Situation 1");

                    } else {
                        //Ticking ON column 3, column 2 was OFF
                        viewHolder.name.setChecked(true);
                        mapDataStorageProduct.get(groupPosition)[childPosition] = true;
                        mapDataStorageCFT.get(groupPosition)[childPosition] = true;

                        ArrayList<Product> currentProducts = currentSupplier.getProducts();
                        if (currentProducts == null){
                            currentProducts = new ArrayList<Product>();
                        }
                        currentProducts.add(new Product(viewHolder.name.getText().toString(), true));
                        currentSupplier.setProducts(currentProducts);
                        Member.connectedMember.getSuppliers().set(groupPosition, currentSupplier);
                        System.out.println("Situation 2");
                    }
                } else {
                    //Ticking OFF column 3
                    if( ! viewHolder.name.isChecked()){
                        //Ticking OFF column 3, column 2 was OFF
                        //System.out.println("This is the situation when a row gets out of view, which in fact ticks both views off.");
                        //System.out.println("Hence we don't touch their value in dataStorage in order to recover it later on, when the getView will be called again.");
                        //System.out.println("This could also have happened because of column 2 being ticked off while column 3 was ticked.");
                        //mapDataStorageProduct.get(groupPosition)[childPosition] = false;
                        //mapDataStorageCFT.get(groupPosition)[childPosition] = false;
                        System.out.println("How could this happen ? (line "+(new Exception().getStackTrace()[0].getLineNumber())+")");
                        System.out.println("Ticking off column 3 while column 2 was OFF");
                    } else {
                        //Ticking OFF column 3, column 2 was ON
                        mapDataStorageProduct.get(groupPosition)[childPosition] = true;   //This one stays on
                        mapDataStorageCFT.get(groupPosition)[childPosition] = false;

                        ArrayList<Product> currentProducts = currentSupplier.getProducts();
                        currentProducts = alterFromName(currentProducts, viewHolder.name.getText().toString(), false);
                        currentSupplier.setProducts(currentProducts);
                        Member.connectedMember.getSuppliers().set(groupPosition, currentSupplier);
                        System.out.println("Situation 3");
                    }
                }
            }
        });

        /**
        viewHolder.column3CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    //Ticking ON column 3
                    if(viewHolder.name.isChecked()){
                        //Ticking ON column 3, column 2 was ON
                        mapDataStorageProduct.get(groupPosition)[childPosition] = true;
                        mapDataStorageCFT.get(groupPosition)[childPosition] = true;

                        ArrayList<Product> currentProducts = currentSupplier.getProducts();
                        currentProducts = alterFromName(currentProducts, viewHolder.name.getText().toString(), true);
                        currentSupplier.setProducts(currentProducts);
                        Member.connectedMember.getSuppliers().set(groupPosition, currentSupplier);

                    } else {
                        //Ticking ON column 3, column 2 was OFF
                        viewHolder.name.setChecked(true);
                        mapDataStorageProduct.get(groupPosition)[childPosition] = true;
                        mapDataStorageCFT.get(groupPosition)[childPosition] = true;

                        ArrayList<Product> currentProducts = currentSupplier.getProducts();
                        if (currentProducts == null){
                            currentProducts = new ArrayList<Product>();
                        }
                        currentProducts.add(new Product(viewHolder.name.getText().toString(), true));
                        currentSupplier.setProducts(currentProducts);
                        Member.connectedMember.getSuppliers().set(groupPosition, currentSupplier);
                    }
                } else {
                    //Ticking OFF column 3
                    if( ! viewHolder.name.isChecked()){
                        //Ticking OFF column 3, column 2 was OFF
                        System.out.println("This is the situation when a row gets out of view, which in fact ticks both views off.");
                        System.out.println("Hence we don't touch their value in dataStorage in order to recover it later on, when the getView will be called again.");
                        System.out.println("This could also have happened because of column 2 being ticked off while column 3 was ticked.");
                        //mapDataStorageProduct.get(groupPosition)[childPosition] = false;
                        //mapDataStorageCFT.get(groupPosition)[childPosition] = false;
                        //System.out.println("How could this happen ? (line "+(new Exception().getStackTrace()[0].getLineNumber())+")");
                        //System.out.println("Ticking off column 3 while column 2 was OFF");
                    } else {
                        //Ticking OFF column 3, column 2 was ON
                        mapDataStorageProduct.get(groupPosition)[childPosition] = true;   //This one stays on
                        mapDataStorageCFT.get(groupPosition)[childPosition] = false;

                        ArrayList<Product> currentProducts = currentSupplier.getProducts();
                        currentProducts = alterFromName(currentProducts, viewHolder.name.getText().toString(), false);
                        currentSupplier.setProducts(currentProducts);
                        Member.connectedMember.getSuppliers().set(groupPosition, currentSupplier);
                    }
                }
            }
        });
         **/
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_explistview_header, parent, false);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * Removes the first Product in the list whose name equals <#param>name</#param>
     * Note : There is no way to know if the returned ArrayList is actually
     * any different from the one passed in argument (i.e. if no Product was removed)
     * @param products The ArrayList<Product> to remove the name from
     * @param name The name of the Product we want to remove from the list
     * @return The updated ArrayList
     */
    private static ArrayList<Product> removeFromName(ArrayList<Product> products, String name){
        for (Product product : products) {
            if (product.getName().equals(name)){
                products.remove(product);
                return products;
            }
        }
        return products;
    }
    /**
     * Modifies the first Product in the list whose name equals <#param>name</#param>
     * to put its CFT state attribute to <#param>cftState</#param>
     * Note : There is no way to know if the returned ArrayList is actually
     * any different from the one passed in argument (i.e. if no Product was modified)
     * @param products The ArrayList<Product> to alter the Product from
     * @param name The name of the Product to alter
     * @param cftState the CFT state we want to apply to this Product
     * @return The updated ArrayList
     */
    private static ArrayList<Product> alterFromName(ArrayList<Product> products, String name, boolean cftState){
        for (Product product : products) {
            if (product.getName().equals(name)) {
                product.setCFT(cftState);
                return products;
            }
        }
        return products;
    }

    private void updateDataStorage(String supplier, Integer position){
        SQLUtility db = SQLUtility.prepareDataBase(this.context);

        ArrayList<String> allSuppliersProductsNames = db.getAllSuppliersProductsNames(supplier);
        ArrayList<String> allMembersSuppliersNames = db.getAllMembersSuppliersNames(Member.connectedMember.getLogin());
        ArrayList<Product> relevantProducts = db.getRelevantSuppliersProducts(Member.connectedMember.getLogin(), supplier);

        Collections.sort(allSuppliersProductsNames);
        Collections.sort(allMembersSuppliersNames);
        Collections.sort(relevantProducts);

        mapDataStorageProduct.put(position, new boolean[allSuppliersProductsNames.size()]);
        mapDataStorageCFT.put(position, new boolean[allSuppliersProductsNames.size()]);

        for(int i=0; i<mapDataStorageProduct.get(position).length; i++){
            for(int j=0; j<relevantProducts.size(); j++){
                if (allSuppliersProductsNames.get(i).equals(relevantProducts.get(j).getName())){
                    mapDataStorageProduct.get(position)[i] = true;
                    mapDataStorageCFT.get(position)[i] = relevantProducts.get(j).getIsOnCFT();
                }
            }
        }
    }

    private class ViewHolder
    {
        //public ImageView image;
        CheckedTextView name;
        //TextView details;
        CheckBox column3CheckBox;
        //public TextView time;
    }
}
