package hello.example.com.testtttt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by p2 on 27/10/2557.
 */
public class ListAdapter extends ArrayAdapter<OrderEntity> {

    LayoutInflater inflater;
    Context context;
    ArrayList<OrderEntity> orders;
    TextView name, number;

    public ListAdapter(Context context, int resource, ArrayList<OrderEntity> orders) {
        super(context, resource, orders);

        this.context = context;
        this.orders = orders;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view== null)
        {
            view = inflater.inflate(R.layout.order_layout, null);
            name = (TextView)view.findViewById(R.id.tvName);
            number = (TextView)view.findViewById(R.id.tvNumber);
        }
        OrderEntity order = orders.get(position);
        name.setText(order.getName());
        number.setText(order.getNumber().toString());
        return view;

    }
}
