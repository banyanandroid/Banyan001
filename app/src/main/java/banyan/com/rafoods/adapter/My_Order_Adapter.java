package banyan.com.rafoods.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import banyan.com.rafoods.R;
import banyan.com.rafoods.activity.Fragment_My_Order;


public class My_Order_Adapter extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public My_Order_Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
          
        public View getView(int position, View convertView, ViewGroup parent) {
            View v=convertView;
            if(convertView==null)
                v = inflater.inflate(R.layout.list_row_my_order, null);

            TextView title = (TextView)v.findViewById(R.id.my_order_info);
            TextView date = (TextView)v.findViewById(R.id.my_order_date);
            TextView amount = (TextView)v.findViewById(R.id.my_order_amount);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            title.setText("Your Order Has been placed");
            date.setText(result.get(Fragment_My_Order.TAG_DATE));
            amount.setText(result.get(Fragment_My_Order.TAG_TOTAL));

            return v;
        
    }
    
}