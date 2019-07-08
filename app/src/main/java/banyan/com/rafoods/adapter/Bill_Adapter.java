package banyan.com.rafoods.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import banyan.com.rafoods.R;
import banyan.com.rafoods.activity.Activity_Shop_Info;


public class Bill_Adapter extends BaseAdapter {
	private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    private String[] bgColors;

    public Bill_Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
                v = inflater.inflate(R.layout.list_row_collection, null);

            TextView bill_number = (TextView)v.findViewById(R.id.bill_number);
            TextView bill_date = (TextView)v.findViewById(R.id.bill_date);
            TextView total_amt = (TextView)v.findViewById(R.id.total_amt);
            TextView paid_amt = (TextView)v.findViewById(R.id.paid_amt);
            TextView balance_amt = (TextView)v.findViewById(R.id.balance_amt);

            HashMap<String, String> result = new HashMap<String, String>();
            result = data.get(position);

            bill_number.setText(result.get(Activity_Shop_Info.TAG_INVOICE_ID));
            bill_date.setText(result.get(Activity_Shop_Info.TAG_INVOICE_DATE));
            total_amt.setText(result.get(Activity_Shop_Info.TAG_TOTAL_AMOUNT));
            paid_amt.setText(result.get(Activity_Shop_Info.TAG_PAID_AMOUNT));
            balance_amt.setText(result.get(Activity_Shop_Info.TAG_BALANCE_AMOUNT));

            String color = bgColors[position % bgColors.length];
            bill_number.setBackgroundColor(Color.parseColor(color));

            return v;
        
    }
    
}