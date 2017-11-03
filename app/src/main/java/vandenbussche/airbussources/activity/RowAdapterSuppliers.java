package vandenbussche.airbussources.activity;

import android.content.Context;
import android.support.annotation.NonNull;
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
import vandenbussche.airbussources.core.Supplier;

/**
 * Adapter to be displayed in the details activities. Its views are NOT clickable in any way, they are just read-only values
 */
public class RowAdapterSuppliers extends ArrayAdapter<Supplier> {


    private Member relevantMember;

    RowAdapterSuppliers(Context context, @NonNull List<Supplier> elements, Member member){
        super(context, R.layout.row_item_check_tables_small, elements);
        this.relevantMember = member;
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
        Supplier element = getItem(position);
        if(element != null) {
            String id = element.getIdentifier();
            viewHolder.name.setText(id);
            ((CheckedTextView) viewHolder.name).setChecked(relevantMember.isWorkingWith(getContext(), element.getName()));
            viewHolder.column3CheckBox.setChecked(Member.isThereANegotiationBetween(getContext(), relevantMember, element.getName()));
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
