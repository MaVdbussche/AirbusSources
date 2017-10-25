package vandenbussche.airbussources.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Supplier;
import vandenbussche.airbussources.database.SQLUtility;

public class RowAdapterSuppliersTickable extends ArrayAdapter<Supplier> {

    private boolean[] dataStorageSupplier;
    private boolean[] dataStorageNegotiation;

    public RowAdapterSuppliersTickable(Context context, @NonNull List<Supplier> elements){
        super(context, R.layout.row_item_check_tables, elements);
        updateDataStorage();
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent){
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_check_tables, parent, false);
        }
        viewHolder = new ViewHolder();
        viewHolder.name = (CheckedTextView) convertView.findViewById(R.id.rowItemCheckTablesItemNameCheckedTextView);
        viewHolder.column3CheckBox = (CheckBox) convertView.findViewById(R.id.rowItemCheckTablesCheckBox);
        convertView.setTag(viewHolder);

        //Gives the relevant values to the layout Views
        final Supplier element = getItem(position);
        if(element != null) {
            String id = element.getIdentifier();
            viewHolder.name.setText(id);
            viewHolder.name.setChecked(dataStorageSupplier[position]);
            viewHolder.column3CheckBox.setChecked(dataStorageNegotiation[position]);
        }
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                System.out.println("Click !");
                if(viewHolder.name.isChecked()){
                    //Ticking OFF column 2
                    viewHolder.name.setChecked(false);
                    viewHolder.column3CheckBox.setChecked(false);
                    dataStorageSupplier[position] = false;
                    dataStorageNegotiation[position] = false;
                    if(Member.connectedMember.getSuppliers() != null){
                        Member.connectedMember.setSuppliers(
                                removeFromName(Member.connectedMember.getSuppliers(),
                                                viewHolder.name.getText().toString()
                                )
                        );

                    } else {
                        System.err.println("How could this happen ? (line "+(new Exception().getStackTrace()[0].getLineNumber())+")");
                        System.err.println("Ticking off from a null Suppliers list");
                    }
                } else if ( ! viewHolder.name.isChecked()) {
                    //Ticking ON column 2
                    viewHolder.name.setChecked(true);
                    viewHolder.column3CheckBox.setChecked(false);   //Since it could not be on yet anyways
                    dataStorageSupplier[position] = true;
                    dataStorageNegotiation[position] = false;

                    ArrayList<Supplier> suppliers = Member.connectedMember.getSuppliers();
                    if(suppliers == null){
                        suppliers = new ArrayList<>();
                    }
                    suppliers.add( new Supplier(viewHolder.name.getText().toString(), null, false) );
                    Member.connectedMember.setSuppliers(suppliers);
                }
            }
        });

        viewHolder.column3CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    //Ticking ON column 3
                    if(viewHolder.name.isChecked()){
                        //Ticking ON column 3, column 2 was ON
                        dataStorageSupplier[position] = true;
                        dataStorageNegotiation[position] = true;
                        Member.connectedMember.setSuppliers(
                                alterFromName(Member.connectedMember.getSuppliers(),
                                                viewHolder.name.getText().toString(),
                                                true)
                        );
                    } else {
                        //Ticking ON column 3, column 2 was OFF
                        viewHolder.name.setChecked(true);
                        dataStorageSupplier[position] = true;
                        dataStorageNegotiation[position] = true;
                        if(Member.connectedMember.getSuppliers() == null){
                            Member.connectedMember.setSuppliers(new ArrayList<Supplier>());
                        }
                        Member.connectedMember.getSuppliers().add( new Supplier(viewHolder.name.getText().toString(), null, true) );
                    }
                } else {
                    //Ticking OFF column 3
                    if( ! viewHolder.name.isChecked()){
                        //Ticking OFF column 3, column 2 was OFF
                        System.out.println("This is the situation when a row gets out of view, which in fact ticks both views off.");
                        System.out.println("Hence we don't touch their value in dataStorage in rmvDuplicates to recover it later on, when the getView will be called again.");
                        //dataStorageSupplier[position] = true;
                        //dataStorageNegotiation[position] = true;
                        //System.out.println("How could this happen ? (line "+(new Exception().getStackTrace()[0].getLineNumber())+")");
                        //System.out.println("Ticking off column 3 while column 2 was OFF");
                    } else {
                        //Ticking OFF column 3, column 2 was ON
                        dataStorageSupplier[position] = true;   //This one stays on
                        dataStorageNegotiation[position] = false;
                        Member.connectedMember.setSuppliers(
                                alterFromName(Member.connectedMember.getSuppliers(),
                                        viewHolder.name.getText().toString(),
                                        false)
                        );
                    }
                }
            }
        });
        return convertView;
    }

    /**
     * Removes the first Supplier in the list whose name equals <#param>name</#param>
     * Note : There is no way to know if the returned ArrayList is actually
     * any different from the one passed in argument (i.e. if no Supplier was removed)
     * @param suppliers The ArrayList<Supplier> to remove the name from
     * @param name The name of the Supplier we want to remove from the list
     * @return The updated ArrayList
     */
    private ArrayList<Supplier> removeFromName(ArrayList<Supplier> suppliers, String name){
        for (Supplier supplier : suppliers) {
            if (supplier.getName().equals(name)){
                suppliers.remove(supplier);
                return suppliers;
            }
        }
        return suppliers;
    }
    /**
     * Modifies the first Supplier in the list whose name equals <#param>name</#param>
     * to put its negotiationState attribute to <#param>negotiationState</#param>
     * Note : There is no way to know if the returned ArrayList is actually
     * any different from the one passed in argument (i.e. if no Supplier was modified)
     * @param suppliers The ArrayList<Supplier> to alter the Supplier from
     * @param name The name of the Supplier to alter
     * @param negotiationState the negotiationState we want to apply to this Supplier
     * @return The updated ArrayList
     */
    private ArrayList<Supplier> alterFromName(ArrayList<Supplier> suppliers, String name, boolean negotiationState){
        for (Supplier supplier : suppliers) {
            if (supplier.getName().equals(name)) {
                supplier.setNegotiationState(negotiationState);
                return suppliers;
            }
        }
        return suppliers;
    }

    private void updateDataStorage(){
        SQLUtility db = SQLUtility.prepareDataBase(getContext());
        ArrayList<String> allSupplierNames = db.getAllSuppliersNames();
        ArrayList<String> relevantSuppliersNames = db.getAllMembersSuppliersNames(Member.connectedMember.getLogin());   //Will be empty during registering
        Collections.sort(allSupplierNames);
        Collections.sort(relevantSuppliersNames);

        db.close();

        dataStorageSupplier = new boolean[allSupplierNames.size()];
        dataStorageNegotiation = new boolean[allSupplierNames.size()];

        for(int i=0; i<dataStorageSupplier.length; i++){
            dataStorageSupplier[i] = relevantSuppliersNames.contains(allSupplierNames.get(i));
            dataStorageNegotiation[i] = Member.isThereANegotiationBetween(getContext(),
                    Member.connectedMember,
                    allSupplierNames.get(i));
        }
    }

    /**
     * Stores important information about an element to be displayed in the Adapter
     */
    private class ViewHolder
    {
        //public ImageView image;
        CheckedTextView name;
        //TextView details;
        CheckBox column3CheckBox;
        //public TextView time;
    }
}
