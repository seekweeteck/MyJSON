package my.edu.tarc.myjson;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TARC on 8/6/2015.
 */
public class CustomerAccountAdapter extends ArrayAdapter<CustomerAccount> {
    List<CustomerAccount> list;
    Activity context;

    public CustomerAccountAdapter(Activity context, List<CustomerAccount> l) {
        super(context, R.layout.customer_account, l);
        this.list = l;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.customer_account, parent, false);

        TextView textViewID, textViewName;

        textViewID = (TextView)rowView.findViewById(R.id.textViewCAID);
        textViewName = (TextView)rowView.findViewById(R.id.textViewCAName);

        textViewID.setText(list.get(position).getCa_id());
        textViewName.setText(list.get(position).getCa_name());

        return rowView;
    }
}
