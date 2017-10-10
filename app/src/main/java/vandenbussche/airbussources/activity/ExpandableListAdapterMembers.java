package vandenbussche.airbussources.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
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

/**
 * Adapter to be displayed in the details activities. Its views are NOT clickable in any way, they are just read-only values
 */
public class ExpandableListAdapterMembers extends BaseExpandableListAdapter {

    private Context context;
    private Product relevantProduct;
    private List<String> listDataHeader; // header titles (Suppliers names)
    // child data in format : header title, child title
    private HashMap<String, List<Member>> listDataChild;

    private HashMap<Integer, boolean[]> mapDataStorageProduct = new HashMap<>();    //Should always be true
    private HashMap<Integer, boolean[]> mapDataStorageCFT = new HashMap<>();

    public ExpandableListAdapterMembers(Context context, List<String> listDataHeader,
                                            HashMap<String, List<Member>> listChildData,
                                            Product relevantProduct) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.relevantProduct = relevantProduct;
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_check_tables, parent, false);
        }
        viewHolder = new ViewHolder();
        viewHolder.name = (CheckedTextView) convertView.findViewById(R.id.rowItemCheckTablesItemNameCheckedTextView);
        viewHolder.column3CheckBox = (CheckBox) convertView.findViewById(R.id.rowItemCheckTablesCheckBox);
        convertView.setTag(viewHolder);

        //Gives the relevant values to the layout Views
        final Member element = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
        if(element != null) {
            String id = element.getIdentifier();
            viewHolder.name.setText(id);
            viewHolder.name.setChecked(mapDataStorageProduct.get(groupPosition)[childPosition]);
            viewHolder.column3CheckBox.setChecked(mapDataStorageCFT.get(groupPosition)[childPosition]);
        }

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

        TextView listHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);

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


    private void updateDataStorage(String supplier, Integer position){
        SQLUtility db = SQLUtility.prepareDataBase(this.context);

        ArrayList<String> allSuppliersMembersNames = db.getAllSuppliersMembersNames(supplier);
        ArrayList<String> allSuppliersMembersIDProfiles = db.getAllSuppliersMembersIDProfiles(supplier);

        Collections.sort(allSuppliersMembersNames);

        mapDataStorageProduct.put(position, new boolean[allSuppliersMembersNames.size()]);
        mapDataStorageCFT.put(position, new boolean[allSuppliersMembersNames.size()]);

        for(int i=0; i<mapDataStorageProduct.get(position).length; i++){
            mapDataStorageProduct.get(position)[i] = true;  //TODO
            mapDataStorageCFT.get(position)[i] = Member.isThereACFTOn(context, allSuppliersMembersIDProfiles.get(i), supplier, relevantProduct.getName());
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
