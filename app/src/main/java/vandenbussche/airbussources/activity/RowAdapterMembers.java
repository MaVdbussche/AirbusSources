package vandenbussche.airbussources.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.core.Supplier;


public class RowAdapterMembers extends ArrayAdapter<Member> {

    //TODO This class has no use right now

    private Product relevantProduct;
    private Supplier relevantSupplier;

    public RowAdapterMembers(Context context, @NonNull List<Member> elements, @Nullable Product product, @Nullable Supplier supplier){
        super(context, R.layout.row_item_check_tables_small, elements);
        this.relevantProduct = product;
        this.relevantSupplier = supplier;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent){

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_check_tables_small, parent, false);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) //If this view has never been instantiated before
        {
            viewHolder = new ViewHolder();
            //viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.name = (CheckedTextView) convertView.findViewById(R.id.rowItemCheckTablesSmallItemNameCheckedTextView);
            viewHolder.column3CheckBox = (CheckBox) convertView.findViewById(R.id.rowItemCheckTablesSmallCheckBox);
            //viewHolder.details = (TextView) convertView.findViewById(R.id.rowItemResearchResultsTextViewDetails);
            //viewHolder.nbr = (TextView) convertView.findViewById(R.id.textView4);
            //viewHolder.time = (TextView) convertView.findViewById(R.id.textView5);
            convertView.setTag(viewHolder);
        }
        //Gives the relevant values to the layout Views
        Member element = getItem(position);
        if(element != null) {
            if(relevantProduct==null && relevantSupplier!=null) {
                //This means we are in the DetailsSupplier activity
                String id = element.getFirstName()+" "+element.getName();
                viewHolder.name.setText(id);
                ((CheckedTextView) viewHolder.name).setChecked(element.isWorkingWith(getContext(), relevantSupplier.getName()));
                viewHolder.column3CheckBox.setChecked(Member.isThereANegotiationBetween(getContext(), element, relevantSupplier.getName()));
            } else {
                System.out.println("This should not have happened !");
                System.out.println("Location : RowAdapterMembers line "+new Exception().getStackTrace()[0].getLineNumber()+".");
            }
        }
        return convertView;
    }

    //There is no clickListener, as those are read-only views

    /**
     * Stores important information about an element to be displayed in the Adapter
     */
    private class ViewHolder
    {
        //public ImageView image;
        TextView name;
        TextView details;
        CheckBox column3CheckBox;
        //public TextView time;
    }
}
