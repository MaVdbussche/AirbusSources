package vandenbussche.airbussources.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vandenbussche.airbussources.R;
import vandenbussche.airbussources.core.Member;
import vandenbussche.airbussources.core.Namable;
import vandenbussche.airbussources.core.Product;
import vandenbussche.airbussources.core.Supplier;


public class RowAdapter extends ArrayAdapter<Namable> {

    //private String researchCategory;

    public RowAdapter(Context context, @NonNull List<Namable> elements){
        super(context, 0, elements);
        //this.researchCategory = researchCategory;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) //If this view has never been instantiated before
        {
            viewHolder = new ViewHolder();
            //viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.name = (TextView) convertView.findViewById(R.id.rowItemTextViewFullName);
            viewHolder.details = (TextView) convertView.findViewById(R.id.rowItemTextViewDetails);
            //viewHolder.nbr = (TextView) convertView.findViewById(R.id.textView4);
            //viewHolder.time = (TextView) convertView.findViewById(R.id.textView5);
            convertView.setTag(viewHolder);
        }

        //Gives the relevant values to the layout Views
        Namable element = getItem(position);
        if(element instanceof Member){
            String fullName = ((Member) element).getFirstName()+" "+((Member) element).getName();
            String details = ((Member) element).getBu()+" - "+((Member) element).getCommodity();
            viewHolder.name.setText(fullName);
            viewHolder.details.setText(details);
        } else if (element instanceof Supplier) {
            String id = element.getIdentifier();
            viewHolder.name.setText(id);
        } else if (element instanceof Product){
            String id = element.getIdentifier();
            viewHolder.name.setText(id);
        }

        return convertView;
    }

    /**
     * Stores important information about an element to be displayed in the Adapter
     */
     private class ViewHolder
    {
        //public ImageView image;
        private TextView name;
        private TextView details;
        //public TextView nbr;
        //public TextView time;
    }
}
