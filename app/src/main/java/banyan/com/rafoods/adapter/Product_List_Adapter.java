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
import banyan.com.rafoods.activity.Activity_MyOrder_Desc;


public class Product_List_Adapter extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Product_List_Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_row_products, null);

            TextView name = (TextView)v.findViewById(R.id.name);
            TextView qty = (TextView)v.findViewById(R.id.qty);
            TextView amount = (TextView)v.findViewById(R.id.Price);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            name.setText(result.get(Activity_MyOrder_Desc.TAG_NAME));
            qty.setText(result.get(Activity_MyOrder_Desc.TAG_QTY));
            amount.setText(result.get(Activity_MyOrder_Desc.TAG_PRICE));

            return v;
        
    }
    
}